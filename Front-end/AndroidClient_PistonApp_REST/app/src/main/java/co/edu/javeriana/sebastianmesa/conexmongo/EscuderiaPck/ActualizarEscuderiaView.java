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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class ActualizarEscuderiaView extends AppCompatActivity {
    private final static String TAG = "Log_ActualizarEscuderia";
    private  List<Escuderia> escuderias = new ArrayList<>();
    private EditText nombre, lugarBase, jefeEquipo, jefeTecnico, chasis, fotoEscudo_ref, cant_vecesEnPodio, cant_TitulosCampeonato;
    private Button updateEscuderia;
    private String resultado="";
    private WebMet_ActualizarEscuderia wm_actualizarPiloto = null;
    private TextView campo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_escuderia_view);

        updateEscuderia = (Button) findViewById(R.id.actEscuderia);
        updateEscuderia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm_actualizarPiloto = new WebMet_ActualizarEscuderia();
                wm_actualizarPiloto.execute();
            }
        });


    }

    private class WebMet_ActualizarEscuderia extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_escuderia?wsdl";
            final String METHOD_NAME = "updateFromAndroid";
            final String SOAP_ACTION = "http://webservice.javeriana.co/updateFromAndroid";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            XMLGregorianCalendar fecha= null;

            nombre = (EditText) findViewById(R.id.editText_escuderia_nombre);
            lugarBase = (EditText) findViewById(R.id.editText_escuderia_ubicacion);
            jefeEquipo = (EditText) findViewById(R.id.editText_escuderia_nombreJefeEquipo);
            jefeTecnico   = (EditText) findViewById(R.id.editText_escuderia_nombreJefeTecnico);
            chasis = (EditText) findViewById(R.id.editText_escuderia_nombreChasis);
            fotoEscudo_ref = (EditText) findViewById(R.id.refImagen);
            cant_vecesEnPodio = (EditText) findViewById(R.id.editText_escuderia_cantPodios);
            cant_TitulosCampeonato = (EditText) findViewById(R.id.editText_escuderia_cantTitulos);


            request.addProperty("nombre", nombre.getText().toString());
            request.addProperty("lugarBase", lugarBase.getText().toString());
            request.addProperty("jefeEquipo", jefeEquipo.getText().toString());
            request.addProperty("jefeTecnico", jefeTecnico.getText().toString());
            request.addProperty("chasis",chasis.getText().toString() );
            request.addProperty("fotoEscudo_ref", fotoEscudo_ref.getText().toString());
            request.addProperty("cant_vecesEnPodio", Integer.parseInt(cant_vecesEnPodio.getText().toString()));
            request.addProperty("cant_TitulosCampeonato", Integer.parseInt(cant_TitulosCampeonato.getText().toString()));


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

                Toast.makeText(getApplicationContext(), 	"Escuderia Actualizado", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }

    public void consumeRESTVolleyActualizarEscuderia(String id){
        nombre = (EditText) findViewById(R.id.editText_escuderia_nombre);
        lugarBase = (EditText) findViewById(R.id.editText_escuderia_ubicacion);
        jefeEquipo = (EditText) findViewById(R.id.editText_escuderia_nombreJefeEquipo);
        jefeTecnico = (EditText) findViewById(R.id.editText_escuderia_nombreJefeTecnico);
        chasis = (EditText) findViewById(R.id.editText_escuderia_nombreChasis);
        fotoEscudo_ref = (EditText) findViewById(R.id.refImagen);
        cant_vecesEnPodio = (EditText) findViewById(R.id.editText_escuderia_cantPodios);
        cant_TitulosCampeonato = (EditText) findViewById(R.id.editText_escuderia_cantTitulos);

        Escuderia escuderia = new Escuderia(nombre.getText().toString(), lugarBase.getText().toString(),
                jefeTecnico.getText().toString(), jefeEquipo.getText().toString(), chasis.getText().toString(),
                Integer.parseInt(cant_vecesEnPodio.getText().toString()),
                Integer.parseInt(cant_TitulosCampeonato.getText().toString()), fotoEscudo_ref.getText().toString());

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String result = gson.toJson(escuderia);

        JSONObject js_2 = null;
        try {
            js_2 = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"JSONException",e);

            Toast.makeText(this,"Error creando el objeto JSON",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest sr = new JsonObjectRequest(
                Request.Method.PUT,
                "http://10.0.2.2:8080/myapp/PistonApp/escuderias/"+id,
                js_2,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "" + response.toString());

                        Toast.makeText(getApplicationContext(), 	"escuderia creada", Toast.LENGTH_LONG).show();
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
        }){

            /*
            //  Esto (getParams) no lo estoy usando como tal pero entiendo que sirve para mapear los
            //  parametros según el tipo de dato.
            */

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                return params;
            }

            /*
            //  Esto (getHeaders) da condiciones a la solicitud con el encabezado de http.
            //  Como el servidor consume JSON, especifico que ese es el tipo de contenido que
            //  voy a utilizar. Y utf-8 porque... eso decía internet jaja.. Supongo que es lo
            //  mas estándar y hace que cosas mas de ASCII no molesten
            */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode= response.statusCode;
                Log.d(TAG,"STATUS CODE: "+mStatusCode);
                return super.parseNetworkResponse(response.);
            }*/
        };

        /*
        //  Agrego lo que armé para hacer la petición con Volley
        */
        Volley.newRequestQueue(this).add(sr);
        //queue.add(sr);

    }

    public void getListaEscuderias(){
        if(!escuderias.isEmpty()){
            escuderias.clear();
        }
        RequestQueue queue = Volley.newRequestQueue(ActualizarEscuderiaView.this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "escuderias";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {

                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject obj = jsonArray.getJSONObject(a);
                                Escuderia e = new Escuderia(obj.getString("nombre"),
                                        obj.getString("lugarBase"),
                                        obj.getString("jefeTecnico"),
                                        obj.getString("jefeEquipo"),
                                        obj.getString("chasis"),
                                        Integer.parseInt(obj.getString("cant_vecesEnPodio")),
                                        Integer.parseInt(obj.getString("cant_TitulosCampeonato")),
                                        obj.getString("fotoEscudo_ref"));
                                escuderias.add(e);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error handling rest invocation"+error.getCause());
                    }
                }
        );
        queue.add(req);
    }

}
