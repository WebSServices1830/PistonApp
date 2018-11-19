package co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.GranPremio;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.VerUsuarioView;

public class BuscarPilotosView extends AppCompatActivity {

    private static final String TAG = "Log_BuscarPilotos";

    private ListView listView_pilotos;
    private List<Piloto> listaPilotos = new ArrayList<>();
    private PilotoAdapter pilotoAdapter;

    private EditText campo_nombrePiloto;
    private Button consultaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_pilotos_view);

        campo_nombrePiloto = findViewById(R.id.input_nombrePiloto);
        consultaBtn =findViewById(R.id.agregarPiloto);
        listView_pilotos = findViewById(R.id.listView_pilotos);

        consumeRESTVolleyGetPilotos(campo_nombrePiloto.getText().toString());

        /*wm_verPilotosPorNombre = new WebMet_VerPilotosPorNombre();
        wm_verPilotosPorNombre.execute();*/

        pilotoAdapter = new PilotoAdapter(this, listaPilotos);
        listView_pilotos.setAdapter(pilotoAdapter);

        listView_pilotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(),VerPilotoView.class);
                intent.putExtra("Piloto", listaPilotos.get(position));
                startActivity(intent);
            }
        });

        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                listaPilotos.clear();
                pilotoAdapter.notifyDataSetChanged();

                /*wm_verPilotosPorNombre = new WebMet_VerPilotosPorNombre();
                wm_verPilotosPorNombre.execute();*/
                consumeRESTVolleyGetPilotos(campo_nombrePiloto.getText().toString());
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
                            pilotoAdapter.notifyDataSetChanged();
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

}
