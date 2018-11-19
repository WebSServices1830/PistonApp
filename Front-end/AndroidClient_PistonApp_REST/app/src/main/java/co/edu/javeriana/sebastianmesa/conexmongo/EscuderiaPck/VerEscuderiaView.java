package co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerEscuderiaView extends AppCompatActivity {

    private final static String TAG = "Log_VerEscueria";


    private EditText campoNombre;
    private Button consultaBtn;

    private ListView listView_escuderias;
    private EscuderiaAdapter adapterEscuderia;
    private List<Escuderia> escuderias = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_escuderia_view);

        campoNombre = findViewById(R.id.nombreEscuderia);
        consultaBtn = findViewById(R.id.btnVerEscuderia);
        listView_escuderias = findViewById(R.id.listView_escuderias);

        getEscuderias(campoNombre.getText().toString());

        adapterEscuderia = new EscuderiaAdapter(this, escuderias);
        listView_escuderias.setAdapter(adapterEscuderia);

        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                escuderias.clear();
                adapterEscuderia.notifyDataSetChanged();

                getEscuderias(campoNombre.getText().toString());
            }
        });

    }

    public void getEscuderias(String textoFiltro_nombreEscuderia){
        if(!escuderias.isEmpty()){
            escuderias.clear();
        }
        RequestQueue queue = Volley.newRequestQueue(VerEscuderiaView.this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "escuderias/"+textoFiltro_nombreEscuderia;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject escuderia_json = jsonArray.getJSONObject(a);
                                String id_str = escuderia_json.getString("id_str");
                                String nombre = escuderia_json.getString("nombre");
                                String lugarBase = escuderia_json.getString("lugarBase");
                                String jefeEquipo = escuderia_json.getString("jefeEquipo");
                                String jefeTecnico = escuderia_json.getString("jefeTecnico");
                                String chasis = escuderia_json.getString("chasis");
                                String fotoEscudo_ref = escuderia_json.getString("fotoEscudo_ref");
                                int cant_vecesEnPodio = Integer.parseInt(escuderia_json.getString("cant_vecesEnPodio"));
                                int cant_TitulosCampeonato = Integer.parseInt(escuderia_json.getString("cant_TitulosCampeonato"));
                                
                                Escuderia escuderia = new Escuderia();

                                escuderia.setId(new ObjectId(id_str));
                                escuderia.setId_str(id_str);
                                escuderia.setNombre(nombre);
                                escuderia.setLugarBase(lugarBase);
                                escuderia.setJefeEquipo(jefeEquipo);
                                escuderia.setJefeTecnico(jefeTecnico);
                                escuderia.setChasis(chasis);
                                escuderia.setFotoEscudo_ref(fotoEscudo_ref);
                                escuderia.setCant_vecesEnPodio(cant_vecesEnPodio);
                                escuderia.setCant_TitulosCampeonato(cant_TitulosCampeonato);

                                escuderias.add( escuderia );

                            }
                            adapterEscuderia.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "JSONException ", e);
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
