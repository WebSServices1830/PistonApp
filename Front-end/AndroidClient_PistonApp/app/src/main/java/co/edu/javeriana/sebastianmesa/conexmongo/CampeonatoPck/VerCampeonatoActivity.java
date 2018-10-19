package co.edu.javeriana.sebastianmesa.conexmongo.CampeonatoPck;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.MainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerCampeonatoActivity extends AppCompatActivity {

    private WebMet_CarrerasOrdenadoPorFecha wm_carrerasOrdenadoPorFecha = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_campeonato);


    }

    private class WebMet_CarrerasOrdenadoPorFecha extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "carrerasOrdenadoPorFecha";
            final String SOAP_ACTION = "http://webservice.javeriana.co/carrerasOrdenadoPorFecha";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            /*
            request.addProperty("nombreUsuario", user);
            request.addProperty("contrasenia", contra_hash);
            */


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                //campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                //campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                if (response != null) {
                    String loginValido_string = response.toString();
                    boolean loginValido = Boolean.parseBoolean(loginValido_string);

                    /*
                    setNombreUsuario(response.getPrimitivePropertyAsString("nombreUsuario"));
                    setContra(response.getPrimitivePropertyAsString("contra"));
                    setEdad( Integer.parseInt(response.getPrimitivePropertyAsString("edad")));
                    setDescripcion(response.getPrimitivePropertyAsString("descripcion"));
                    setFoto(response.getPrimitivePropertyAsString("foto"));
                    setAdmin(Boolean.parseBoolean (response.getPrimitivePropertyAsString("admin")));
                    setBolsillo(Long.parseLong (response.getPrimitivePropertyAsString("bolsillo")));
                    */

                    Log.i("Login", ""+loginValido);

                    if(loginValido){
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        return true;

                    }else{
                        Toast.makeText(getApplicationContext(), "Revise su usuario/contraseña", Toast.LENGTH_SHORT).show();
                    }

                }


            }
            catch (Exception e)
            {
                Log.i("Error: ",e.getMessage());
                //Toast.makeText(getApplicationContext(), "No encontrado", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                e.printStackTrace();

            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_SHORT).show();
            }
            else{

//                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
//
//                if (getNombreUsuario() == null){
//                    campoRespuesta.setText("Usuario no encontrado");
//                    Toast.makeText(getApplicationContext(), "Prueba otro nombre", Toast.LENGTH_LONG).show();
//                }else{
//                    campoRespuesta.setText(
//                            "Nombre: "+getNombreUsuario()+/*getFecha()+*/"\n"+
//                                    "Edad: "+getEdad()+"\n"+
//                                    "Descripción: "+getDescripcion()+"\n"+
//                                    "Foto: "+getFoto()+"\n"+
//                                    "Admin: "+isAdmin()+"\n"+
//                                    "Bolsillo: "+getBolsillo()
//                    );
//
//                    Toast.makeText(getApplicationContext(), "Viendo Piloto", Toast.LENGTH_LONG).show();
//
//                }
            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getBaseContext(), LoginActivityView.class));
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
