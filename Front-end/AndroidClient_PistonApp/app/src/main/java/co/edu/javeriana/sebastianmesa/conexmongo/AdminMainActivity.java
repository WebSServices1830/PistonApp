package co.edu.javeriana.sebastianmesa.conexmongo;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.edu.javeriana.sebastianmesa.conexmongo.AutoPck.IndexAutoView;
import co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck.IndexEscuderiaView;
import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.IndexGPView;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.Managers.ManagerUsuario;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Usuario;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.IndexPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.IndexUsuarioView;

public class AdminMainActivity extends Activity {
    private EditText nombre, edad, equipo;
    private Button agregarP, consultaP, accionesPiloto;
    private String resultado="";
    private TextView campo = null;

    private WebMet_InicializarCampeonato wm_inicializarCampeonato = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void accionesPiloto (View view){
        startActivity(new Intent(getBaseContext(), IndexPilotoView.class));
    }

    public void accionesEscuderia (View view){
        startActivity(new Intent(getBaseContext(), IndexEscuderiaView.class));
    }

    public void accionesAuto (View view){
        startActivity(new Intent(getBaseContext(), IndexAutoView.class));
    }

    public void accionesGP (View view){
        startActivity(new Intent(getBaseContext(), IndexGPView.class));
    }

    public void accionesUsuario (View view){
        startActivity(new Intent(getBaseContext(), IndexUsuarioView.class));
    }

    public void logout (View view){
        startActivity(new Intent(getBaseContext(), LoginActivityView.class));
    }

    public void cargarDatos (View view){
        wm_inicializarCampeonato = new WebMet_InicializarCampeonato();
        wm_inicializarCampeonato.execute();
    }

    private class WebMet_InicializarCampeonato extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";

            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "inicializarCampeonato";
            final String SOAP_ACTION = "http://webservice.javeriana.co/inicializarCampeonato";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();
                return true;
            }
            catch (Exception e)
            {
                Log.i("Error: ",e.getMessage());
                e.printStackTrace();

            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Datos cargados", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getBaseContext(), LoginActivityView.class));
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
        }
    }

}
