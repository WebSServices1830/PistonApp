package co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.VerGPView;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.ClienteMongo;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.FileUpload;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.VerUsuarioView;

public class VerPilotoView extends AppCompatActivity {

    private final static String TAG = "Log_VerPiloto";
    private ImageView imageView_fotoPiloto;
    TextView textView_nombreCompleto;
    TextView textView_fechaNacimiento;
    TextView textView_lugarNacimiento;
    TextView textView_podiosTotales;
    TextView textView_puntosTotales;
    TextView textView_granPremios;
    TextView textView_calificacion;
    Button botonEditar, botonEliminar;

    DownloadImageTask downloadImageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_piloto_view);

        botonEditar= findViewById(R.id.buttonVerPiloto_Editar);
        botonEliminar= findViewById(R.id.buttonVerPiloto_Eliminar);
        imageView_fotoPiloto = findViewById(R.id.imageView_perfilPiloto);
        textView_nombreCompleto = findViewById(R.id.textView_nombreCompleto);
        textView_fechaNacimiento = findViewById(R.id.textView_fechaNacimientoPiloto);
        textView_lugarNacimiento = findViewById(R.id.textView_lugarNacimiento);
        textView_podiosTotales = findViewById(R.id.textView_podiosTotales);
        textView_puntosTotales = findViewById(R.id.textView_puntosTotales);
        textView_granPremios = findViewById(R.id.textView_ingresosGranPremios);
        textView_calificacion = findViewById(R.id.textView_calificacion);

        final Piloto piloto = (Piloto) getIntent().getSerializableExtra("Piloto");

        downloadImageTask = new DownloadImageTask();
        downloadImageTask.execute(piloto.getFoto_ref());

        textView_nombreCompleto.setText(piloto.getNombreCompleto());

        Date fechaNacimiento = piloto.getFecha_Nacimiento();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(fechaNacimiento);
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        textView_fechaNacimiento.setText(anio+"/"+mes+"/"+dia);

        textView_lugarNacimiento.setText(piloto.getLugarNacimiento());

        textView_podiosTotales.setText( Integer.toString(piloto.getCant_podiosTotales()) );

        textView_puntosTotales.setText( Integer.toString(piloto.getCant_puntosTotales()) );

        textView_granPremios.setText( Integer.toString(piloto.getCant_granPremiosIngresado()) );

        textView_calificacion.setText( Float.toString(piloto.getCalificacion()) );

        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerPilotoView.this, ActualizarPilotoView.class);
                intent.putExtra("Piloto", piloto);
                startActivity(intent);
            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumeRESTVolleyEliminarPiloto(piloto.getId_str());
            }
        });

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
                imageView_fotoPiloto.setImageBitmap(bitmap);
            }
        }
    }

    public void consumeRESTVolleyEliminarPiloto (String id_str)  {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.DELETE,
                "http://10.0.2.2:8080/myapp/PistonApp/pilotos/"+id_str,
                new Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        Log.d(TAG, "" + response.toString());

                        Toast.makeText(getApplicationContext(), 	"Piloto Eliminado", Toast.LENGTH_LONG).show();
                        finish();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error.ResponseREST " + error.networkResponse.statusCode);
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Log.d(TAG, "Error.ResponseREST A: " + obj.toString());
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                        Log.d(TAG, "Error.ResponseREST B: " + e1.toString());
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                        Log.d(TAG, "Error.ResponseREST C: " + e2.toString());
                    }
                }
            }
        });
        Volley.newRequestQueue(this).add(sr);
    }

}
