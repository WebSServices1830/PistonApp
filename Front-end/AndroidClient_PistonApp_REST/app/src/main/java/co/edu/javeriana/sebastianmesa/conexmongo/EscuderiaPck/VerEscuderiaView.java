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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerEscuderiaView extends AppCompatActivity {

    private final static String TAG = "Log_VerEscueria";


    private EditText campoNombre;
    private Button consultaBtn;
    private List<Escuderia> escuderias = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_escuderia_view);

        campoNombre = findViewById(R.id.nombreEscuderia);

        consultaBtn =(Button) findViewById(R.id.btnVerEscuderia);
        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                /*
                wm_agregarPiloto = new WebMet_ConsultarEscuderia();
                wm_agregarPiloto.execute();
                */
                consumeRESTVolleyVerEscuderiaString();
            }
        });

    }

    public void consumeRESTVolleyVerEscuderiaString(){
        if(!escuderias.isEmpty()){
            escuderias.clear();
        }
        RequestQueue queue = Volley.newRequestQueue(VerEscuderiaView.this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "escuderias";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject escuderia_json = jsonArray.getJSONObject(a);
                                String nombre = escuderia_json.getString("nombre");
                                String lugarBase = escuderia_json.getString("lugarBase");
                                String jefeEquipo = escuderia_json.getString("jefeEquipo");
                                String jefeTecnico = escuderia_json.getString("jefeTecnico");
                                String chasis = escuderia_json.getString("chasis");
                                String fotoEscudo_ref = escuderia_json.getString("fotoEscudo_ref");
                                int cant_vecesEnPodio = Integer.parseInt(escuderia_json.getString("cant_vecesEnPodio"));
                                int cant_TitulosCampeonato = Integer.parseInt(escuderia_json.getString("cant_TitulosCampeonato"));
                                escuderias.add(new Escuderia(
                                        nombre,
                                        lugarBase,
                                        jefeTecnico,
                                        jefeEquipo,
                                        chasis,
                                        cant_vecesEnPodio,
                                        cant_TitulosCampeonato,
                                        fotoEscudo_ref));
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
