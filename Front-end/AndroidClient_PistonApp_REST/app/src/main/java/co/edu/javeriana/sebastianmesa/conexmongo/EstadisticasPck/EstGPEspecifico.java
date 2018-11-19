

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import co.edu.javeriana.sebastianmesa.conexmongo.AdminMainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.Managers.ManagerUsuario;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Campeonato;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.EstadisticasGeneral;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.EstadisticasResultados;
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

public class EstGPEspecifico extends AppCompatActivity {


    static String[] encabezado={"Pos", "Nombre", "Tiempo"};
    //    private WebMet_ObtenerPiloto wm_validarLogin = null;
    private List<EstadisticasGeneral> listaPilotos = new ArrayList<>();
    public TableView<String[]> tableView;
    private String idCalendarioCampeonato= "";
    private String calendarioSeleccionado= "";
    private List<GranPremio> listagranPremios = new ArrayList<>();
    private List<EstadisticasResultados> listaResultados = new ArrayList<>();
    private HashMap<String, String> relacionPista = new HashMap<>();
    private TextView campoNombre;

    private String codigoGlob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_gpespecifico);

        Intent intent = getIntent();
        String codRecibido = intent.getStringExtra("cod");
        String nomRecibido = intent.getStringExtra("nom");

        campoNombre = (TextView) findViewById(R.id.nombrePista);
        campoNombre.setText(nomRecibido);

        codigoGlob = codRecibido;

        Toast.makeText(EstGPEspecifico.this, codRecibido, Toast.LENGTH_SHORT).show();

        tableView = (TableView<String[]>) findViewById(R.id.tableView);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,encabezado));


        tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tableView.setColumnCount(3);

        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                String cod = relacionPista.get(((String[])clickedData)[1]);

//                Toast.makeText(EstPorResultados.this, cod, Toast.LENGTH_SHORT).show();
            }
        });


        consumeRESTVolleyVerCampeonatoString("Campeonato 2018");

    }


    public void consumeRESTVolleyVerCampeonatoString(final String nombre){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "campeonatos";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject obj = jsonArray.getJSONObject(a);

                                if(obj.getString("nombre").equals(nombre)) {
                                    idCalendarioCampeonato = obj.getString("id_str");
                                    Log.i("RevEstPorResiltados",idCalendarioCampeonato);
                                    consumeRESTVolleyGranPremiosOrdenadosPorFecha();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("RevEstPorResiltados", "Error handling rest invocation"+error.getCause());
                    }
                }
        );
        queue.add(req);
    }

    public void consumeRESTVolleyGranPremiosOrdenadosPorFecha(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp";
        String path = "/granPremios/"+idCalendarioCampeonato;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject obj = jsonArray.getJSONObject(a);

                                GranPremio granPremioObjeto= new GranPremio();
                                granPremioObjeto.setId_str(obj.getString("id_str"));
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                granPremioObjeto.setFecha(simpleDateFormat.parse(obj.getString("fecha")));
                                granPremioObjeto.setPista(obj.getString("pista"));
                                granPremioObjeto.setCantVueltas(obj.getInt("cantVueltas"));
                                granPremioObjeto.setMejorVuelta(simpleDateFormat.parse(obj.getString("mejorVuelta")));

                                JSONArray clasificacion = obj.getJSONArray("id_clasificaciones");

                                ArrayList<String> listClas = new ArrayList<String>();
                                if (clasificacion != null) {
                                    int len = clasificacion.length();
                                    for (int i=0;i<len;i++){
                                        listClas.add(clasificacion.get(i).toString());
                                    }
                                }

                                Log.i("clasificacionVerlo", listClas.toString() + " " + clasificacion.toString());

                                granPremioObjeto.setId_clasificaciones(listClas);

                                // adding movie to movies array
                                listagranPremios.add(granPremioObjeto);
                            }

                            Log.i("RevEstPorResiltados", listagranPremios.toString());
                            Log.i("RevEstPorResiltados id ", idCalendarioCampeonato);

                            consumeRESTVolleyGranPremiosEnDetalle(listagranPremios);

                            //                            String[][] datos = new String[listagranPremios.size()][4];
                            //
                            //                            for (int i = 0 ; i < listagranPremios.size() ; i++){
                            //                                for (int j = 0 ; j < 4 ; j++){
                            //                                    switch (j) {
                            //                                        case 0:
                            //                                            datos[i][j] = listagranPremios.get(i).getFecha().toString();
                            //                                            break;
                            //                                        case 1:
                            //                                            datos[i][j] = listagranPremios.get(i).getPista();
                            //                                            break;
                            ////                                case 2:
                            ////                                    datos[i][j] = "equipo";
                            ////                                    break;
                            //                                        case 2:
                            //                                            datos[i][j] = listagranPremios.get(i).getPista();
                            //                                            break;
                            //
                            //                                    }
                            //                                }
                            //
                            //                            }
                            //
                            //
                            //
                            //                            tableView.setDataAdapter(new SimpleTableDataAdapter(getBaseContext(), datos));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("RevEstPorResiltados", "consumeRESTVolleyGranPremiosOrdenadosPorFecha: JSONException ", e);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d("RevEstPorResiltados", "consumeRESTVolleyGranPremiosOrdenadosPorFecha: ParseException ", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("RevEstPorResiltados", "Error handling rest invocation"+error.getCause());
                    }
                }
        );
        queue.add(req);
    }


    public void consumeRESTVolleyGranPremiosEnDetalle(final List<GranPremio> listagranPremios){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp";
        String path = "/pistas";
        Log.i("RevEstPorResiltados","previo");
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject obj = jsonArray.getJSONObject(a);

                                String codeJSON = obj.getString("id_str");

//                                Log.i("RevEstPorResiltados",codeJSON);

                                for (int i = 0; i<listagranPremios.size() ; i++){

//                                    Log.i("RevEstPorResiltados -",listagranPremios.get(i).getId_str() + " y: " + codeJSON);

                                    for (int j=0; j<listagranPremios.get(i).getId_clasificaciones().size(); j++){

                                        if(codigoGlob.equals(listagranPremios.get(i).getId_clasificaciones().get(j))){

                                        Log.i("verPistaDetalle",listagranPremios.get(i).getId_clasificaciones().get(j));


//                                            EstadisticasResultados e = new EstadisticasResultados(
//                                                    codeJSON,
//                                                    listagranPremios.get(i).getFecha(),
//                                                    obj.getString("ciudad"),
//                                                    obj.getString("nombreUltimoGanador"));
//
//                                            listaResultados.add(e);
//
//                                            relacionPista.put(obj.getString("ciudad"),codeJSON );

                                        }

                                    }

                                }
                            }

//                            String[][] datos = new String[EstPorResultados.this.listaResultados.size()][4];
//
//                            for (int i = 0; i < EstPorResultados.this.listaResultados.size() ; i++){
//                                for (int j = 0 ; j < 4 ; j++){
//                                    switch (j) {
//                                        case 0:
//                                            datos[i][j] = EstPorResultados.this.listaResultados.get(i).getFecha();
//                                            break;
//                                        case 1:
//                                            datos[i][j] = EstPorResultados.this.listaResultados.get(i).getLugar();
//                                            break;
////                                case 2:
////                                    datos[i][j] = "equipo";
////                                    break;
//                                        case 2:
//                                            datos[i][j] = EstPorResultados.this.listaResultados.get(i).getUltimoEnGanar();
//                                            break;
//
//                                    }
//                                }
//
//                            }
//
//                            tableView.setDataAdapter(new SimpleTableDataAdapter(getBaseContext(), datos));


                            String[][] datos = new String[listaResultados.size()][4];

                            for (int i = 0 ; i < listaResultados.size() ; i++){
                                for (int j = 0 ; j < 4 ; j++){
                                    switch (j) {
                                        case 0:

                                            //DateFormat.getDateInstance().format(listaResultados.get(i).getFecha().toString());
                                            //Log.i("pruebafecha",DateFormat.getDateInstance().format(listaResultados.get(i).getFecha().toString()));

                                            datos[i][j] = DateFormat.getDateInstance().format(listaResultados.get(i).getFecha());;
                                            break;
                                        case 1:
                                            datos[i][j] = listaResultados.get(i).getLugar();
                                            break;
//                                case 2:
//                                    datos[i][j] = "equipo";
//                                    break;
                                        case 2:

                                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                            Date date = new Date();

                                            String ganador = null;

                                            if (listaResultados.get(i).getFecha().compareTo(date) > 0){
                                                ganador = "-";
                                            }else{
                                                ganador = listaResultados.get(i).getUltimoEnGanar();
                                            }

                                            datos[i][j] = ganador;
                                            break;

                                    }
                                }

                            }



                            tableView.setDataAdapter(new SimpleTableDataAdapter(getBaseContext(), datos));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("RevEstPorResiltados", "consumeRESTVolleyGranPremiosOrdenadosPorFecha: JSONException ", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("RevEstPorResiltados", "Error handling rest invocation"+error.getCause());
                    }
                }
        );
        queue.add(req);
    }




}


