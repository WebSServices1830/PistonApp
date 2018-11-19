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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bson.types.ObjectId;
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

import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.VerGPView;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class EliminarPilotoView extends AppCompatActivity {

    private final static String TAG = "Log_EliminarPiloto";

    private EditText campoId;
    private Button consultaBtn;
    private boolean completado = false;
    private List<Piloto> listaPilotos= new ArrayList<Piloto>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_piloto_view);

        completado = false;

        campoId = (EditText) findViewById(R.id.idPiloto);
        consultaBtn =(Button) findViewById(R.id.agregarPiloto);
        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                /*wm_agregarPiloto = new EliminarPilotoView.WebMet_EliminarPiloto();
                wm_agregarPiloto.execute();*/
                consumeRESTVolleyGetPilotos(campoId.getText().toString());
            }
        });

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
                                piloto.setId(new ObjectId(id_str));
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
                            consumeRESTVolleyEliminarPiloto(listaPilotos.get(0).getId_str());
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

    public void consumeRESTVolleyEliminarPiloto (String id_str)  {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(
                Request.Method.DELETE,
                "http://10.0.2.2:8080/myapp/PistonApp/pilotos/"+id_str,
                new Response.Listener() {

                    @Override
                    public void onResponse(Object response) {

                        Log.d(TAG, "" + response.toString());

                        Toast.makeText(getApplicationContext(), 	"Piloto Eliminado", Toast.LENGTH_LONG).show();
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
    }

}
