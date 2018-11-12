package co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class ActualizarPilotoView extends AppCompatActivity {

    private EditText nombre, fecha, lugar, foto, podios, puntos, gp;
    private Button updateP;
    private String resultado="";
    private WebMet_ActualizarPiloto wm_actualizarPiloto = null;
    private TextView campo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_piloto_view);

        updateP =(Button) findViewById(R.id.actPiloto);
        updateP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_actualizarPiloto = new WebMet_ActualizarPiloto();
                wm_actualizarPiloto.execute();
            }
        });

    }

    private class WebMet_ActualizarPiloto extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_piloto?wsdl";
            final String METHOD_NAME = "piloto_update";
            final String SOAP_ACTION = "http://webservice.javeriana.co/piloto_update";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            //request.addProperty("Hola", nro1.getText().toString());
            //request.addProperty("nro2", nro2.getText().toString());

            XMLGregorianCalendar fecha= null;

            nombre = (EditText) findViewById(R.id.nomPiloto);
            lugar = (EditText) findViewById(R.id.lugarNacimientoPiloto);
            foto = (EditText) findViewById(R.id.fotoRefPiloto);
            podios   = (EditText) findViewById(R.id.podiosTotales);
            puntos = (EditText) findViewById(R.id.puntosTotales);
            gp = (EditText) findViewById(R.id.gpTotales);


            //request.addProperty("id", "5bba54b01c08ef0495bc5676");
            request.addProperty("nombreCompleto", nombre.getText().toString());
            request.addProperty("fecha_Nacimiento", null);
            request.addProperty("lugarNacimiento", lugar.getText().toString());
            request.addProperty("foto_ref", foto.getText().toString());
            request.addProperty("cant_podiosTotales", Integer.parseInt(podios.getText().toString()));
            request.addProperty("cant_puntosTotales", Integer.parseInt(puntos.getText().toString()));
            request.addProperty("cant_granPremiosIngresado", Integer.parseInt(gp.getText().toString()));


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                //resultado=response.toString();
                //Log.i("Resultado: ",resultado);
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

                //campo = (TextView) findViewById(R.id.conexion);

                //campo.setText(resultado);

                Toast.makeText(getApplicationContext(), 	"Piloto Actualizado", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
