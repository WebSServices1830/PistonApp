package co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import bolts.Bolts;
import co.edu.javeriana.sebastianmesa.conexmongo.AdminMainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.Managers.ManagerUsuario;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Usuario;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.ClienteMongo;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import co.edu.javeriana.sebastianmesa.conexmongo.UserMenuActivity;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class VerUsuarioView extends AppCompatActivity {

    private final static String TAG = "Log_VerUsuario";

    private ImageView imagenPerfil;
    private TextView tv_nombreUsuario, tv_fechaNacimientoUsuario, tv_bolsilloUsuario;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario_view);

        imagenPerfil = findViewById(R.id.imageView_perfilUsuario);
        tv_nombreUsuario = findViewById(R.id.textView_nombreUsuario);
        tv_fechaNacimientoUsuario = findViewById(R.id.textView_fechaNacimientoUsuario);
        tv_bolsilloUsuario = findViewById(R.id.textView_bolsilloUsuario);

        //new WebMet_ConsultarUsuario().execute();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        getUsuario(currentUser);

    }

    public void getUsuario(FirebaseUser user){
        RequestQueue mRequestQueue;
        String url = "http://10.0.2.2:8080/myapp/PistonApp/usuarios/";
        String path= user.getEmail();

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            String nombreJSON = jsonObject.getString("nombreUsuario");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date fechaJSON = simpleDateFormat.parse(jsonObject.getString("fechaNacimiento"));
                            Log.d(TAG, "fechaNacimiento");
                            String urlJSON = jsonObject.getString("urlFoto");
                            double bolsilloJSON = jsonObject.getDouble("bolsillo");

                            tv_nombreUsuario = (TextView) findViewById(R.id.campo_nombreUsuario);
                            tv_fechaNacimientoUsuario = (TextView) findViewById(R.id.campo_fechaNacimientoUsuario);
                            tv_bolsilloUsuario = (TextView) findViewById(R.id.campo_bolsilloUsuario);

                            tv_nombreUsuario.setText(nombreJSON);

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(fechaJSON);

                            int anio = calendar.get(Calendar.YEAR);
                            int mes = calendar.get(Calendar.MONTH) + 1;
                            int dia = calendar.get(Calendar.DAY_OF_MONTH);
                            tv_fechaNacimientoUsuario.setText(dia+"/"+mes+"/"+anio);

                            tv_bolsilloUsuario.setText("$" + bolsilloJSON);

                            if(!urlJSON.equals("")){
                                new DownloadImageTask().execute(urlJSON);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error JSONException"+e.getCause());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error ParseException"+e.getCause());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error handling rest invocation"+error.getCause());
                    }
                }
        );

        mRequestQueue.add(req);

    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        MongoClient mongoClient = ClienteMongo.getInstancia();

        // get handle to "PistonAppDB" database
        MongoDatabase database = mongoClient.getDatabase("PistonAppDB");

        // Create a gridFSBucket with a custom bucket name "files"
        GridFSBucket gridFSFilesBucket = GridFSBuckets.create(database, "almacenamiento");

        private Exception exception;

        protected Bitmap doInBackground(String... ids) {
            ObjectId fileId = new ObjectId(ids[0]); //The id of a file uploaded to GridFS, initialize to valid file id

            GridFSDownloadStream downloadStream = gridFSFilesBucket.openDownloadStream(fileId);
            int fileLength = (int) downloadStream.getGridFSFile().getLength();
            byte[] bytesToWriteTo = new byte[fileLength];
            downloadStream.read(bytesToWriteTo);
            downloadStream.close();

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytesToWriteTo, 0, bytesToWriteTo.length);

            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                imagenPerfil.setImageBitmap(bitmap);
            }
        }
    }
}
