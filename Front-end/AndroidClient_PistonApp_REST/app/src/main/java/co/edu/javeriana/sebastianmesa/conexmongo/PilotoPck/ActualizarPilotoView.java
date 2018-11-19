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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class ActualizarPilotoView extends AppCompatActivity {

    private final static String TAG = "Log_ActualizarPiloto";

    private EditText nombre, fecha, lugar, foto, podios, puntos, gp;
    private Button updateP;
    private String resultado="";
    private WebMet_ActualizarPiloto wm_actualizarPiloto = null;
    private TextView campo = null;
    private List<Piloto> listaPilotos= new ArrayList<Piloto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_piloto_view);

        nombre = (EditText) findViewById(R.id.editText_nombrePiloto);
        lugar = (EditText) findViewById(R.id.editText_lugarNacimientoPiloto);
        foto = (EditText) findViewById(R.id.fotoRefPiloto);
        podios   = (EditText) findViewById(R.id.editText_podiosTotales);
        puntos = (EditText) findViewById(R.id.editText_puntosTotales);
        gp = (EditText) findViewById(R.id.editText_ingresosGPTotales);
        fecha = (EditText) findViewById(R.id.editText_fechaPiloto);

        updateP =(Button) findViewById(R.id.actPiloto);
        updateP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                /*wm_actualizarPiloto = new WebMet_ActualizarPiloto();
                wm_actualizarPiloto.execute();*/
                consumeRESTVolleyGetPilotos(nombre.getText().toString());
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

    public void consumeRESTVolleyGetPilotos(String campoNombrePiloto){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp";
        String path = "/pilotos/"+campoNombrePiloto;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject obj = jsonArray.getJSONObject(a);

                                String id_str = obj.getString("id_str");
                                String nombreCompleto = obj.getString("nombreCompleto");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date fechaNacimiento= simpleDateFormat.parse(obj.getString("fecha_Nacimiento"));
                                String lugarNacimiento = obj.getString("lugarNacimiento");
                                String foto_ref = obj.getString("foto_ref");
                                int cant_podiosTotales = obj.getInt("cant_podiosTotales") ;
                                int cant_puntosTotales = obj.getInt("cant_puntosTotales") ;
                                int cant_granPremiosIngresado = obj.getInt("cant_granPremiosIngresado");
                                float calificacion = Float.parseFloat( obj.getString("calificacion") );

                                Piloto piloto = new Piloto();
                                piloto.setId_str(id_str);
                                piloto.setNombreCompleto(nombreCompleto);
                                piloto.setFecha_Nacimiento(fechaNacimiento);
                                piloto.setLugarNacimiento(lugarNacimiento);
                                piloto.setFoto_ref(foto_ref);
                                piloto.setCant_podiosTotales(cant_podiosTotales);
                                piloto.setCant_puntosTotales(cant_puntosTotales);
                                piloto.setCant_granPremiosIngresado(cant_granPremiosIngresado);
                                piloto.setCalificacion(calificacion);

                                // adding movie to movies array
                                listaPilotos.add(piloto);
                            }
                            Log.d(TAG,listaPilotos.get(0).getNombreCompleto());
                            consumeRESTVolleyActualizarPiloto(listaPilotos.get(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "consumeRESTVolleyGranPremiosOrdenadosPorFecha: JSONException ", e);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d(TAG, "consumeRESTVolleyGranPremiosOrdenadosPorFecha: ParseException ", e);
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

    public void consumeRESTVolleyActualizarPiloto (Piloto piloto) throws ParseException {

        if(nombre.getText().toString().length() > 0)
            piloto.setNombreCompleto(nombre.getText().toString());
        if(fecha.getText().toString().length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            piloto.setFecha_Nacimiento(simpleDateFormat.parse(fecha.getText().toString()));
        }
        if(lugar.getText().toString().length() > 0)
            piloto.setLugarNacimiento(lugar.getText().toString());
        if(podios.getText().toString().length() > 0)
            piloto.setCant_podiosTotales(Integer.parseInt(podios.getText().toString()));
        if(puntos.getText().toString().length() > 0)
            piloto.setCant_puntosTotales(Integer.parseInt(puntos.getText().toString()));
        if(gp.getText().toString().length() > 0)
            piloto.setCant_granPremiosIngresado(Integer.parseInt(gp.getText().toString()));

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String result = gson.toJson(piloto);

        JSONObject js = null;
        try {
            js = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"JSONException",e);

            Toast.makeText(this,"Error creando el objeto JSON",Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        //  Formo la petición: método a utilizar, path del servicio, el JSON creado, y un
        //  listener que está pendiente de la respuesta a la petición
         */
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest sr = new JsonObjectRequest(
                Request.Method.PUT,
                "http://10.0.2.2:8080/myapp/PistonApp/pilotos/"+piloto.getId_str(),
                js,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "" + response.toString());

                        Toast.makeText(getApplicationContext(), 	"Piloto Actualizado", Toast.LENGTH_LONG).show();
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
    }


}
