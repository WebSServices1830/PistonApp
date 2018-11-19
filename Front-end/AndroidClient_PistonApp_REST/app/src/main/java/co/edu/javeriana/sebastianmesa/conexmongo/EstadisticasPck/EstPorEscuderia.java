package co.edu.javeriana.sebastianmesa.conexmongo.EstadisticasPck;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import co.edu.javeriana.sebastianmesa.conexmongo.AdminMainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.Managers.ManagerUsuario;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Campeonato;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.EstadisticasGeneral;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.GranPremio;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Usuario;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import co.edu.javeriana.sebastianmesa.conexmongo.UserMenuActivity;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class EstPorEscuderia extends AppCompatActivity {

    private String[][] datos = { { "1", "Sebas", "Ferrari", "20" },
            { "2", "Heikki", "Skyppy", "10" } };

    static String[] encabezado={"Escuderia","Puntos", "Campeonatos"};
//    private WebMet_ObtenerPiloto wm_validarLogin = null;
    private List<Escuderia> listaEscuderias = new ArrayList<>();
    public TableView<String[]> tableView;
    private List<EstadisticasGeneral> listaPilotos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_por_escuderia);


        tableView = (TableView<String[]>) findViewById(R.id.tableView);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,encabezado));


        tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tableView.setColumnCount(3);

        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                Toast.makeText(EstPorEscuderia.this, ((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
            }
        });


        obtenerDatos();


    }


    public void obtenerDatos (){

        Log.i("ViendoEscuderias", "Entrando");

        RequestQueue mRequestQueue;
        String url = "http://10.0.2.2:8080/myapp/PistonApp/escuderias";

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for (int l = 0; l < response.length(); l++) {

                        JSONObject escuderia = response.getJSONObject(l);


                        String id_str = escuderia.getString("id_str");
                        String nombreEscuderia = escuderia.getString("nombre");
                        int campeonatosTotales = escuderia.getInt("cant_TitulosCampeonato");
                        int cant_podiosTotales = escuderia.getInt("cant_vecesEnPodio");
                        String lugarBaseEquipo = escuderia.getString("lugarBase");
                        String fotoEquipo_ref = escuderia.getString("fotoEscudo_ref");
                        String JFTecnico = escuderia.getString("jefeTecnico");
                        String JFEquipo = escuderia.getString("jefeEquipo");
                        String chasisAuto = escuderia.getString("chasis");




                        Escuderia escuderiaObjeto= new Escuderia();

                        escuderiaObjeto.setId_str(id_str);
                        escuderiaObjeto.setNombre(nombreEscuderia);
                        escuderiaObjeto.setCant_TitulosCampeonato(campeonatosTotales);
                        escuderiaObjeto.setCant_vecesEnPodio(cant_podiosTotales);
                        escuderiaObjeto.setFotoEscudo_ref(fotoEquipo_ref);
                        escuderiaObjeto.setLugarBase(lugarBaseEquipo);
                        escuderiaObjeto.setJefeTecnico(JFTecnico);
                        escuderiaObjeto.setJefeEquipo(JFEquipo);
                        escuderiaObjeto.setChasis(chasisAuto);

                        Log.i("ViendoEscuderias", escuderia.toString());


                        listaEscuderias.add(escuderiaObjeto);

                        String[][] datos = new String[listaEscuderias.size()][3];

                        for (int i = 0 ; i < listaEscuderias.size() ; i++){
                            for (int j = 0 ; j < 3 ; j++){
                                switch (j) {
                                    case 0:
                                        datos[i][j] = listaEscuderias.get(i).getNombre();
                                        break;
                                    case 1:
                                        datos[i][j] = listaEscuderias.get(i).getChasis();
                                        break;
                                    case 2:
                                        datos[i][j] = Integer.toString(listaEscuderias.get(i).getCant_TitulosCampeonato());
                                        break;

                                    case 3:

                                        break;
                                }
                            }
                        }



//                        Arrays.sort(datos, new Comparator() {
//                            public int compare(Object o1, Object o2) {
//                                String[] elt1 = (String[])o2;
//                                String[] elt2 = (String[])o1;
//                                return elt1[1].compareTo(elt2[1]);
//                            }
//                        });

                        tableView.setDataAdapter(new SimpleTableDataAdapter(getBaseContext(), datos));
                        //Toast.makeText(getBaseContext(),"Bien. Tam: " + listaEscuderias.size(), Toast.LENGTH_SHORT).show();

                        Log.i("ViendoEscuderias",nombreEscuderia + " con " + campeonatosTotales +"Campeonatos");

                    }

//                    String nombreJSON = response.get("nombreUsuario").toString();
//                    String passJSON   = response.get("contra").toString();
//
//                    Boolean adminJSON  = Boolean.parseBoolean(response.get("admin").toString());

                    //Log.i(TAG,"Es admin:" + adminJSON.toString() );

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ViendoEscuderias","Error pero con respuesta",e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("ViendoEscuderias","Error pero con respuesta",error);
            }
        });

        mRequestQueue.add(jsonArrayRequest);
    }


}
