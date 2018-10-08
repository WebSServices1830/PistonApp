package co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck;

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

public class CrearUsuarioView extends AppCompatActivity {


    private EditText nombreUsuario, contra, edad, descripcion, foto, bolsillo;
    private CheckBox admin;
    private Button agregarP, consultaP, accionesPiloto;
    private String resultado="";
    private WebMet_AgregarUsuario wm_agregarPiloto = null;
    private TextView campo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario_view);

        agregarP =(Button) findViewById(R.id.agregarUsuario);
        agregarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_AgregarUsuario();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_AgregarUsuario extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_usuario?wsdl";
            final String METHOD_NAME = "usuario_create";
            final String SOAP_ACTION = "http://webservice.javeriana.co/usuario_create";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            nombreUsuario = (EditText) findViewById(R.id.nomUsuario);
            contra = (EditText) findViewById(R.id.passUsuario);
            edad = (EditText) findViewById(R.id.edadUsuario);
            descripcion = (EditText) findViewById(R.id.descUsuario);
            foto = (EditText) findViewById(R.id.fotoUsuario);
            bolsillo = (EditText) findViewById(R.id.bolsilloUsuario);


            admin = (CheckBox) findViewById(R.id.adminUsuario);


            request.addProperty("nombreUsuario", nombreUsuario.getText().toString());
            request.addProperty("contra", contra.getText().toString());
            request.addProperty("edad", Integer.parseInt( edad.getText().toString()) );
            request.addProperty("descripcion", descripcion.getText().toString());
            request.addProperty("foto", foto.getText().toString());
            request.addProperty("admin", admin.isSelected());
            request.addProperty("bolsillo", Long.parseLong( bolsillo.getText().toString()) );


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

                Toast.makeText(getApplicationContext(), 	"Usuario Creado", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
