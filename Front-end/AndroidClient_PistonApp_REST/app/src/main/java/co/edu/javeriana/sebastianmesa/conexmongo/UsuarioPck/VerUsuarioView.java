package co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

import org.bson.types.ObjectId;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import bolts.Bolts;
import co.edu.javeriana.sebastianmesa.conexmongo.AdminMainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.Managers.ManagerUsuario;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Usuario;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.ClienteMongo;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import co.edu.javeriana.sebastianmesa.conexmongo.UserMenuActivity;

public class VerUsuarioView extends AppCompatActivity {


    private ImageView imagenPerfil;
    private TextView tv_nombreUsuario, tv_fechaNacimientoUsuario, tv_bolsilloUsuario;
    private WebMet_ConsultarUsuario wm_agregarPiloto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario_view);

        imagenPerfil = findViewById(R.id.imageView_perfilUsuario);
        tv_nombreUsuario = findViewById(R.id.textView_nombreUsuario);
        tv_fechaNacimientoUsuario = findViewById(R.id.textView_fechaNacimientoUsuario);
        tv_bolsilloUsuario = findViewById(R.id.textView_bolsilloUsuario);

        new WebMet_ConsultarUsuario().execute();

    }

    private class WebMet_ConsultarUsuario extends AsyncTask<Void, Void, Boolean> {



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/autenticacion?wsdl";
            final String METHOD_NAME = "validarLogin";
            final String SOAP_ACTION = "http://webservice.javeriana.co/validarLogin";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("nombreUsuario", ManagerUsuario.usuario.getNombreUsuario());
            request.addProperty("contrasenia", ManagerUsuario.usuario.getContra());


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                //campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                //campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();

                if (response != null) {

                    String nombreUsuario = response.getPrimitivePropertyAsString("nombreUsuario");
                    String contrasenia = response.getPrimitivePropertyAsString("contra");
                    Date fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(response.getPrimitivePropertyAsString("fechaNacimiento"));
                    String urlFoto = response.getPrimitivePropertyAsString("urlFoto");
                    boolean admin = Boolean.parseBoolean(response.getPrimitivePropertyAsString("admin"));
                    double bolsillo = Double.parseDouble(response.getPrimitivePropertyAsString("bolsillo"));

                    ManagerUsuario.usuario = new Usuario();

                    ManagerUsuario.usuario.setNombreUsuario(nombreUsuario);
                    ManagerUsuario.usuario.setContra(contrasenia);
                    ManagerUsuario.usuario.setFechaNacimiento(fechaNacimiento);
                    ManagerUsuario.usuario.setUrlFoto(urlFoto);
                    ManagerUsuario.usuario.setAdmin(admin);
                    ManagerUsuario.usuario.setBolsillo(bolsillo);

                    return true;
                }


            }
            catch (Exception e)
            {
                Log.i("Error",e.getMessage());
                //Toast.makeText(getApplicationContext(), "No encontrado", Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
            }
            else{

                tv_nombreUsuario.setText(tv_nombreUsuario.getText() + ManagerUsuario.usuario.getNombreUsuario());
                tv_fechaNacimientoUsuario.setText(tv_fechaNacimientoUsuario.getText() + ManagerUsuario.usuario.getFechaNacimiento().toString());
                tv_bolsilloUsuario.setText(tv_bolsilloUsuario.getText() + Double.toString(ManagerUsuario.usuario.getBolsillo()));

                if(!ManagerUsuario.usuario.getUrlFoto().equals("")){
                    new DownloadImageTask().execute(ManagerUsuario.usuario.getUrlFoto());
                }
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
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
