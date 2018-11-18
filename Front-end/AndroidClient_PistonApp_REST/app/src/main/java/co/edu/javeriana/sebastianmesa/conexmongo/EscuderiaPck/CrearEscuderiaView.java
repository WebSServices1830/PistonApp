package co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class CrearEscuderiaView extends AppCompatActivity {

    private EditText nombre, lugarBase, jefeEquipo, jefeTecnico, chasis, fotoEscudo_ref, cant_vecesEnPodio, cant_TitulosCampeonato;
    private Button agregarP;
    private String resultado="";
    private WebMet_AgregarEscuderia wm_agregarPiloto = null;
    private TextView campo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_escuderia_view);

        agregarP =(Button) findViewById(R.id.agregarEscuderia);
        agregarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_AgregarEscuderia();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_AgregarEscuderia extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/admin?wsdl";
            final String METHOD_NAME = "registrarEscuderia";
            final String SOAP_ACTION = "http://webservice.javeriana.co/registrarEscuderia";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            XMLGregorianCalendar fecha= null;


            nombre = (EditText) findViewById(R.id.nomEscuderia);
            lugarBase = (EditText) findViewById(R.id.nomBase);
            jefeEquipo = (EditText) findViewById(R.id.nomJEquipo);
            jefeTecnico   = (EditText) findViewById(R.id.nomJTecnico);
            chasis = (EditText) findViewById(R.id.nomChasis);
            fotoEscudo_ref = (EditText) findViewById(R.id.refImagen);
            cant_vecesEnPodio = (EditText) findViewById(R.id.numPodios);
            cant_TitulosCampeonato = (EditText) findViewById(R.id.numTitulos);


            request.addProperty("nombre", nombre.getText().toString());
            request.addProperty("lugarBase", lugarBase.getText().toString());
            request.addProperty("jefeEquipo", jefeEquipo.getText().toString());
            request.addProperty("jefeTecnico", jefeTecnico.getText().toString());
            request.addProperty("chasis", chasis.getText().toString());
            request.addProperty("cant_vecesEnPodio", Integer.parseInt(cant_vecesEnPodio.getText().toString()));
            request.addProperty("cant_TitulosCampeonato", Integer.parseInt(cant_TitulosCampeonato.getText().toString()));
            request.addProperty("fotoEscudo_ref", fotoEscudo_ref.getText().toString());


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
    public void consumeRESTVolleyCrearEscuderia(){
        RequestQueue queue = Volley.newRequestQueue(CrearEscuderiaView.this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://10.0.2.2:8080/myapp/PistonApp/escuderias", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mPostCommentResponse.requestEndedWithError(error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                nombre = (EditText) findViewById(R.id.nomEscuderia);
                lugarBase = (EditText) findViewById(R.id.nomBase);
                jefeEquipo = (EditText) findViewById(R.id.nomJEquipo);
                jefeTecnico   = (EditText) findViewById(R.id.nomJTecnico);
                chasis = (EditText) findViewById(R.id.nomChasis);
                fotoEscudo_ref = (EditText) findViewById(R.id.refImagen);
                cant_vecesEnPodio = (EditText) findViewById(R.id.numPodios);
                cant_TitulosCampeonato = (EditText) findViewById(R.id.numTitulos);

                Map<String,String> params = new HashMap<String, String>();
                params.put("nombre", nombre.getText().toString());
                params.put("lugarBase", lugarBase.getText().toString());
                params.put("jefeEquipo", jefeEquipo.getText().toString());
                params.put("jefeTecnico", jefeTecnico.getText().toString());
                params.put("chasis", chasis.getText().toString());
                //params.put("cant_vecesEnPodio", Integer.parseInt(cant_vecesEnPodio.getText().toString()));
                //params.put("cant_TitulosCampeonato", Integer.parseInt(cant_TitulosCampeonato.getText().toString()));
                params.put("fotoEscudo_ref", fotoEscudo_ref.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

}
