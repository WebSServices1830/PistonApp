package co.edu.javeriana.sebastianmesa.conexmongo.Simulacion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.GranPremio;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.CrearPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class GPSimulationActivity extends AppCompatActivity {

    private final static String TAG = "Log_GPSimulation";

    private Spinner spinner_granPremios;
    private Button button_simular;

    private List<GranPremio> listaGranPremios = new ArrayList<>();
    private ArrayAdapter<GranPremio> adapter_spinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsimulation);

        spinner_granPremios = findViewById(R.id.spinner_simulation_granPremios);

        button_simular = findViewById(R.id.button_simular);

        adapter_spinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listaGranPremios);
        adapter_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_granPremios.setAdapter(adapter_spinner);

        cargarGranPremios();
    }

    public void cargarGranPremios() {
        if (!listaGranPremios.isEmpty()) {
            listaGranPremios.clear();
        }
        RequestQueue queue = Volley.newRequestQueue(GPSimulationActivity.this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "escuderias";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url + path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject granpremio_json = jsonArray.getJSONObject(a);

                                Log.d(TAG, granpremio_json.toString());

                                String id_str = granpremio_json.getString("id_str");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Log.d(TAG,"Fecha: "+granpremio_json.getString("fecha"));
                                Date fecha = simpleDateFormat.parse(granpremio_json.getString("fecha"));
                                int cantVueltas = granpremio_json.getInt("cantVueltas");
                                Date mejorVuelta =simpleDateFormat.parse(granpremio_json.getString("mejorVuelta"));
                                String pista= granpremio_json.getString("pista");
                                String id_campeonato= granpremio_json.getString("id_campeonato");

                                GranPremio granPremio= new GranPremio();

                                granPremio.setId(new ObjectId(id_str));
                                granPremio.setId_str(id_str);
                                granPremio.setFecha(fecha);
                                granPremio.setCantVueltas(cantVueltas);
                                granPremio.setMejorVuelta(mejorVuelta);
                                granPremio.setPista(pista);
                                granPremio.setId_campeonato(id_campeonato);

                                Log.d(TAG,granPremio.toString());

                                listaGranPremios.add(granPremio);
                            }
                            adapter_spinner.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG,"JSONException",e);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d(TAG,"ParseException",e);
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
