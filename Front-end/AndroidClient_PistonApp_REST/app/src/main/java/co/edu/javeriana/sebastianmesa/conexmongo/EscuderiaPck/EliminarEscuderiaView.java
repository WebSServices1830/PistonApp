package co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;

import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class EliminarEscuderiaView extends AppCompatActivity {

    private EditText campoId;
    private Button consultaBtn;
    private String Nombre, lugar, foto;
    private Date fecha;
    private int podios, puntos, premios;
    private WebMet_EliminarEscuderia wm_agregarPiloto = null;
    private TextView campoRespuesta = null;
    private boolean completado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_escuderia_view);

        completado = false;

        consultaBtn =(Button) findViewById(R.id.btnEliminarEscuderia);
        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_EliminarEscuderia();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_EliminarEscuderia extends AsyncTask<Void, Void, Boolean> {



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_escuderia?wsdl";
            final String METHOD_NAME = "escuderia_deleteByName";
            final String SOAP_ACTION = "http://webservice.javeriana.co/escuderia_deleteByName";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            campoId = (EditText) findViewById(R.id.nombreEscuderia);
            request.addProperty("nombre", campoId.getText().toString());


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();

            }
            catch (Exception e)
            {
                Log.i("Error: ",e.getMessage());
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
            }
            else{

                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);

            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
