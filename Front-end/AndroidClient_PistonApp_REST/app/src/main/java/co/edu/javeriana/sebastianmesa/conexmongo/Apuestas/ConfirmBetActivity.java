package co.edu.javeriana.sebastianmesa.conexmongo.Apuestas;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Apuesta;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.GranPremio;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class ConfirmBetActivity extends AppCompatActivity {

    private static final String TAG = "Log_ConfirmBetActivity";

    private String id_usuarioActual = "";

    Spinner spinner_granPremios;
    Button button_apostar;
    EditText editText_montoApostar;
    TextView textView_conductor;

    private FirebaseAuth mAuth;
    private FirebaseUser actualUser;

    private String idCalendarioCampeonato = "";

    private List<GranPremio> listagranPremios = new ArrayList<>();
    private List<String> listaNombresCiudades = new ArrayList<>();

    ArrayAdapter<String> adapter = null;

    private Piloto piloto;

    private int posicion_GranPremioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bet);

        mAuth = FirebaseAuth.getInstance();
        actualUser = mAuth.getCurrentUser();
        getUsuario(actualUser);

        spinner_granPremios = findViewById(R.id.spinner_granPremios);
        editText_montoApostar = findViewById(R.id.editText_montoApuesta);
        textView_conductor = findViewById(R.id.textView_pilotoApostar);
        button_apostar = findViewById(R.id.button_apostar);

        piloto = (Piloto) getIntent().getSerializableExtra("Piloto");

        textView_conductor.setText(textView_conductor.getText() + piloto.getNombreCompleto());

        button_apostar.setEnabled(false);

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listaNombresCiudades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_granPremios.setAdapter(adapter);

        this.consumeRESTVolleyVerCampeonatoString();
        //Log.i("idCampeonato",idCalendarioCampeonato);

        spinner_granPremios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                button_apostar.setEnabled(true);
                posicion_GranPremioSeleccionado = position;
                Log.d(TAG,"GranPremio seleccionado: "+listaNombresCiudades.get(posicion_GranPremioSeleccionado));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                button_apostar.setEnabled(false);
            }
        });

        button_apostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCampos()) {
                    GranPremio granPremio_seleccionado = listagranPremios.get(posicion_GranPremioSeleccionado);
                    double monto = Double.parseDouble(editText_montoApostar.getText().toString());

                    Apuesta apuesta = new Apuesta(id_usuarioActual,
                            piloto.getId_str(),
                            granPremio_seleccionado.getId_str(),
                            monto);
                }
            }
        });
    }


    private boolean comprobarCampos(){
        boolean esValido = true;

        if(id_usuarioActual.isEmpty()){
            Toast.makeText(this,"No se ha cargado el usuario actual",Toast.LENGTH_SHORT).show();
            esValido = false;
        }
        if(piloto == null){
            Toast.makeText(this,"No se ha cargado el usuario actual",Toast.LENGTH_SHORT).show();
            esValido = false;
        }
        if(editText_montoApostar.getText().toString().isEmpty()){
            editText_montoApostar.setError("Campo necesario");
            esValido = false;
        }

        return esValido;
    }

    public void apostar(Apuesta apuesta){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String result = gson.toJson(apuesta);

        JSONObject js_2 = null;
        try {
            js_2 = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"JSONException",e);

            return;
        }
        RequestQueue queue = Volley.newRequestQueue(ConfirmBetActivity.this);
        JsonObjectRequest sr = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8080/myapp/PistonApp/casino",
                js_2,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "" + response.toString());

                        Toast.makeText(getApplicationContext(), 	"apuesta creada", Toast.LENGTH_LONG).show();
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
    }

    public void getUsuario(FirebaseUser user){
        RequestQueue mRequestQueue;
        String url = "http://10.0.2.2:8080/myapp/PistonApp/usuarios/";
        String path= user.getEmail();

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            id_usuarioActual = jsonObject.getString("id_str");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error JSONException"+e.getCause());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error handling rest invocation"+error.getCause());
                    }
                }
        );

        mRequestQueue.add(req);

    }




    private class WebMet_GranPremiosOrdenadoPorFecha extends AsyncTask<Void, GranPremio, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL = "http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "granPremiosOrdenadoPorFecha";
            final String SOAP_ACTION = "http://webservice.javeriana.co/granPremiosOrdenadoPorFecha";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("id_campeonato", idCalendarioCampeonato);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);

                KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
                for (int i = 0; i < ks.getPropertyCount(); ++i) {
                    SoapObject granPremio = (SoapObject) ks.getProperty(i);

                    String id_string = granPremio.getPrimitivePropertyAsString("id_str");
                    Date fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(granPremio.getPrimitivePropertyAsString("fecha"));
                    String idPista = granPremio.getPrimitivePropertyAsString("pista");
                    int cantVueltas = Integer.parseInt(granPremio.getPrimitivePropertyAsString("cantVueltas"));
                    Date mejorVuelta = null;
                    try {
                        mejorVuelta = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(granPremio.getPrimitivePropertyAsString("mejorVuelta"));
                    } catch (ParseException e) {
                        mejorVuelta = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(granPremio.getPrimitivePropertyAsString("mejorVuelta"));
                    }
                    //List<String> id_clasificaciones= Collections.singletonList(granPremio.getPrimitivePropertyAsString("clasificaciones"));

                    GranPremio granPremioObjeto = new GranPremio();
                    granPremioObjeto.setId_str(id_string);
                    granPremioObjeto.setFecha(fecha);
                    granPremioObjeto.setPista(idPista);
                    granPremioObjeto.setCantVueltas(cantVueltas);
                    granPremioObjeto.setMejorVuelta(mejorVuelta);
                    //granPremioObjeto.setId_clasificaciones(id_clasificaciones);
                    publishProgress(granPremioObjeto);
                }
                return true;
            } catch (Exception e) {
                Log.i("Error: ", e.getMessage());
                e.printStackTrace();

            }

            return false;
        }

        @Override
        protected void onProgressUpdate(GranPremio... values) {
            listagranPremios.add(values[0]);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success == false) {
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
            } else {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
        }

    }

    public void consumeRESTVolleyVerCampeonatoString() {
        RequestQueue queue = Volley.newRequestQueue(ConfirmBetActivity.this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "campeonatos";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url + path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(jsonArray.length()-1);
                            idCalendarioCampeonato = obj.getString("id_str");
                            Log.d(TAG,idCalendarioCampeonato);

                            consumeRESTVolleyGranPremiosOrdenadoPorFecha();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "consumeRESTVolleyVerCampeonatoString: JSONException ", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error handling rest invocation" + error.getCause());
                    }
                }
        );
        queue.add(req);
    }

    public void consumeRESTVolleyGranPremiosOrdenadoPorFecha() {
        if (!listagranPremios.isEmpty()) {
            listagranPremios.clear();
        }
        RequestQueue queue = Volley.newRequestQueue(ConfirmBetActivity.this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "campeonatos/"+idCalendarioCampeonato+"/granPremios";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url + path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject granpremio_json = jsonArray.getJSONObject(a);
                                GranPremio g = new GranPremio();

                                String id_str = granpremio_json.getString("id_str");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date fecha = simpleDateFormat.parse(granpremio_json.getString("fecha"));
                                int cantVueltas = granpremio_json.getInt("cantVueltas");
                                Date mejorVuelta =simpleDateFormat.parse(granpremio_json.getString("mejorVuelta"));
                                String pista= granpremio_json.getString("pista");
                                String id_campeonato= granpremio_json.getString("id_campeonato");

                                GranPremio granPremio= new GranPremio(id_str,fecha,cantVueltas,mejorVuelta,pista,id_campeonato);



                                listagranPremios.add(granPremio);
                            }
                            getNombresPistas();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error handling rest invocation" + error.getCause());
                    }
                }
        );
        queue.add(req);
    }

    public void getNombresPistas(){
        if (!listaNombresCiudades.isEmpty()) {
            listaNombresCiudades.clear();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "pistas";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url + path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {

                            for (GranPremio granPremio:
                                    listagranPremios) {
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject pista_json = jsonArray.getJSONObject(a);

                                    Log.d(TAG, pista_json.toString());

                                    String id_str = pista_json.getString("id_str");

                                    if( id_str.equals(granPremio.getPista())){
                                        String ciudad = pista_json.getString("ciudad");
                                        listaNombresCiudades.add(ciudad);
                                    }
                                }
                            }

                            adapter.notifyDataSetChanged();
                            button_apostar.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG,"JSONException",e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error handling rest invocation" + error.getCause());
                    }
                }
        );
        queue.add(req);
    }
}




