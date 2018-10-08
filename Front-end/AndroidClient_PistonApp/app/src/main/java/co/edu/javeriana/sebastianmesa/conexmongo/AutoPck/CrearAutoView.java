package co.edu.javeriana.sebastianmesa.conexmongo.AutoPck;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class CrearAutoView extends AppCompatActivity {


    private EditText nombre, pesoEnKg, ruedas, combustible, foto_ref, motor_referencia, motor_cilindraje, motor_configuracion;
    private CheckBox motor_turbo;
    private Button agregarP;
    private String resultado="";
    private WebMet_AgregarAuto wm_agregarPiloto = null;
    private TextView campo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_auto_view);

        agregarP =(Button) findViewById(R.id.agregarAuto);
        agregarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_AgregarAuto();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_AgregarAuto extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_auto?wsdl";
            final String METHOD_NAME = "auto_create";
            final String SOAP_ACTION = "http://webservice.javeriana.co/auto_create";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            XMLGregorianCalendar fecha= null;


            nombre = (EditText) findViewById(R.id.nomAuto);
            pesoEnKg = (EditText) findViewById(R.id.pesoEnKgAuto);
            ruedas = (EditText) findViewById(R.id.ruedasAuto);
            combustible   = (EditText) findViewById(R.id.combustibleAuto);
            foto_ref = (EditText) findViewById(R.id.foto_refAuto);
            motor_referencia = (EditText) findViewById(R.id.motor_referenciaAuto);
            motor_cilindraje = (EditText) findViewById(R.id.motor_cilindrajeAuto);
            motor_configuracion = (EditText) findViewById(R.id.motor_configuracionAuto);
            motor_turbo = (CheckBox) findViewById(R.id.motor_turboAuto);


            request.addProperty("nombre", nombre.getText().toString());
            request.addProperty("pesoEnKg", /*Double.parseDouble(pesoEnKg.getText().toString())*/ null);
            request.addProperty("ruedas", ruedas.getText().toString());
            request.addProperty("combustible", combustible.getText().toString());
            request.addProperty("foto_ref", foto_ref.getText().toString());
            request.addProperty("motor_referencia", motor_referencia.getText().toString());
            request.addProperty("motor_cilindraje", motor_cilindraje.getText().toString());
            request.addProperty("motor_configuracion", motor_configuracion.getText().toString());
            request.addProperty("motor_turbo", motor_turbo.isSelected() );


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

                Toast.makeText(getApplicationContext(), 	"Auto Creado", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
