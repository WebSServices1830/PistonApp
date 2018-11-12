package co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.types.ObjectId;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.math.BigInteger;
import java.time.LocalTime;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class CrearGPView extends AppCompatActivity {

    private EditText fecha, cantVueltas, mejorVuelta, pista, campeonato;
    private Button agregarP;
    private String resultado="";
    private WebMet_CrearGP wm_agregarPiloto = null;
    private TextView campo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_gpview);

        agregarP =(Button) findViewById(R.id.agregarGP);
        agregarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_CrearGP();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_CrearGP extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_granPremio?wsdl";
            final String METHOD_NAME = "granPremio_create";
            final String SOAP_ACTION = "http://webservice.javeriana.co/granPremio_create";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


            fecha = (EditText) findViewById(R.id.fechaGP);
            cantVueltas = (EditText) findViewById(R.id.vueltasGP);
            mejorVuelta = (EditText) findViewById(R.id.mejorVueltaGP);
            pista   = (EditText) findViewById(R.id.pistaIdGP);
            campeonato = (EditText) findViewById(R.id.campeonatoIdGP);

            LocalTime timeLap = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                timeLap = LocalTime.of(2, 10, 10);
            }

            String paraPista =  String.format("%024x", new BigInteger(1, pista.getText().toString().getBytes(/*YOUR_CHARSET?*/)));
            String paraCampeonato =  String.format("%024x", new BigInteger(1, campeonato.getText().toString().getBytes(/*YOUR_CHARSET?*/)));

            ObjectId idPista = new ObjectId(paraPista);
            ObjectId idCampeonato = new ObjectId(paraCampeonato);

            request.addProperty("fecha", null);
            request.addProperty("cantVueltas", Integer.parseInt(cantVueltas.getText().toString()));
            request.addProperty("mejorVuelta", null);
            request.addProperty("pista",  idPista);
            request.addProperty("campeonato", idCampeonato);


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

                Toast.makeText(getApplicationContext(), 	"Escuderia Creada", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
