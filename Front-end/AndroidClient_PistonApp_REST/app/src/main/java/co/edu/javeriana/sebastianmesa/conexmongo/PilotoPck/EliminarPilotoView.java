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

import com.android.volley.AuthFailureError;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class EliminarPilotoView extends AppCompatActivity {

    private final static String TAG = "Log_EliminarPiloto";

    private EditText campoId;
    private Button consultaBtn;
    private String Nombre, lugar, foto;
    private Date fecha;
    private int podios, puntos, premios;
    private EliminarPilotoView.WebMet_EliminarPiloto wm_agregarPiloto = null;
    private TextView campoRespuesta = null;
    private boolean completado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_piloto_view);

        completado = false;

        consultaBtn =(Button) findViewById(R.id.agregarPiloto);
        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new EliminarPilotoView.WebMet_EliminarPiloto();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_EliminarPiloto extends AsyncTask<Void, Void, Boolean> {



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_piloto?wsdl";
            final String METHOD_NAME = "piloto_deleteByName";
            final String SOAP_ACTION = "http://webservice.javeriana.co/piloto_deleteByName";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            campoId = (EditText) findViewById(R.id.idPiloto);
            request.addProperty("id", campoId.getText().toString());


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();

                boolean resp = new Boolean(response.toString());

                if ( resp ) {

                    completado = true;
                    campoRespuesta.setText("Piloto encontrado");
                    //Toast.makeText(getApplicationContext(), "Piloto NO eliminado", Toast.LENGTH_LONG).show();

                }else{
                    campoRespuesta.setText("No encontrado");
                    //Toast.makeText(getApplicationContext(), "Piloto eliminado", Toast.LENGTH_LONG).show();
                }


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
    /*
    public void consumeRESTVolleyEliminarPiloto ()  {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.DELETE,
                "http://10.0.2.2:8080/myapp/PistonApp/pilotos/"+editt,
                new Response.Listener<StringRequest>() {

                    @Override
                    public void onResponse(StringRequest response) {

                        Log.d(TAG, "" + response.toString());

                        Toast.makeText(getApplicationContext(), 	"Usuario Eliminado", Toast.LENGTH_LONG).show();
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
    }*/

}
