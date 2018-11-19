package co.edu.javeriana.sebastianmesa.conexmongo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import co.edu.javeriana.sebastianmesa.conexmongo.Apuestas.BetActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.AutoPck.IndexAutoView;
import co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck.IndexEscuderiaView;
import co.edu.javeriana.sebastianmesa.conexmongo.EstadisticasPck.EstPorEscuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.EstadisticasPck.EstPorPiloto;
import co.edu.javeriana.sebastianmesa.conexmongo.EstadisticasPck.EstPorResultados;
import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.IndexGPView;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.BuscarPilotosView;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.IndexPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.Simulacion.GPSimulationActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.IndexUsuarioView;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.VerUsuarioView;
import co.edu.javeriana.sebastianmesa.conexmongo.fragment.CalendarioFragment;
import co.edu.javeriana.sebastianmesa.conexmongo.fragment.EstadisticasPilotosFragment;
import co.edu.javeriana.sebastianmesa.conexmongo.fragment.IndexFragment;
import co.edu.javeriana.sebastianmesa.conexmongo.fragment.MasFragment;

import co.edu.javeriana.sebastianmesa.conexmongo.helper.BottomNavigationBehavior;

public class AdminMainActivity extends AppCompatActivity {
    private EditText nombre, edad, equipo;
    private Button agregarP, consultaP, accionesPiloto;
    private String resultado="";
    private TextView campo = null;
    private ActionBar toolbar;

//    private WebMet_InicializarCampeonato wm_inicializarCampeonato = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default
        //toolbar.setTitle("Index");
        loadFragment(new IndexFragment());

    }

    public void simularGranPremio(View view){
        startActivity(new Intent(getBaseContext(), GPSimulationActivity.class));
    }

    public void accionesPiloto (View view){
        startActivity(new Intent(getBaseContext(), IndexPilotoView.class));
    }

    public void accionesEscuderia (View view){
        startActivity(new Intent(getBaseContext(), IndexEscuderiaView.class));
    }

    public void accionesAuto (View view){
        startActivity(new Intent(getBaseContext(), IndexAutoView.class));
    }

    public void accionesGP (View view){
        startActivity(new Intent(getBaseContext(), IndexGPView.class));
    }

    public void accionesUsuario (View view){
        startActivity(new Intent(getBaseContext(), IndexUsuarioView.class));
    }

    public void logout (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getBaseContext(), LoginActivityView.class));
    }

    public void cargarDatosFragmento (View view){
        inicializarDatos();
    }

    void inicializarDatos() {



        /*
        //  Como el servidor quiere consumir JSON entonces creo un JSON en base al objeto
        //  que quiero pasar. Siendo este 'user' de tipo Usuario.
         */
//            JSONObject js = new JSONObject();
//            try {
//                js.put("user",user.toJSON());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        /*
        //  Formo la petición: método a utilizar, path del servicio, el JSON creado, y un
        //  listener que está pendiente de la respuesta a la petición
         */
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest sr = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:8080/myapp/PistonApp",
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("ResponseRESTini", "" + response);

                            Toast.makeText(getApplicationContext(), 	"Datos Inicializados", Toast.LENGTH_LONG).show();
                            //finish();

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.ResponseRESTini", "" + error.networkResponse.statusCode);
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                            Log.d("Error.ResponseRESTini", "A: " + obj.toString());
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                            Log.d("Error.ResponseRESTini", "B: " + e1.toString());
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                            Log.d("Error.ResponseRESTini", "C: " + e2.toString());
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
            };

        /*
        //  Agrego lo que armé para hacer la petición con Volley
        */
            Volley.newRequestQueue(this).add(sr);




    }

    public void logoutFragmento (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getBaseContext(), LoginActivityView.class));
    }



//    private class WebMet_InicializarCampeonato extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//            //WebService - Opciones
//            final String NAMESPACE = "http://webservice.javeriana.co/";
//
//            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
//            final String METHOD_NAME = "inicializarCampeonato";
//            final String SOAP_ACTION = "http://webservice.javeriana.co/inicializarCampeonato";
//
//            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//
//            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            envelope.setOutputSoapObject(request);
//
//            HttpTransportSE ht = new HttpTransportSE(URL);
//            try {
//                ht.call(SOAP_ACTION, envelope);
//                Object response = envelope.getResponse();
//                return true;
//            }
//            catch (Exception e)
//            {
//                Log.i("Error: ",e.getMessage());
//                e.printStackTrace();
//
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            if(success==false){
//                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(getApplicationContext(),"Datos cargados", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            startActivity(new Intent(getBaseContext(), LoginActivityView.class));
//            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
//        }
//    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_index:
                    //toolbar.setTitle("Index");
                    fragment = new IndexFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_calendario:
                    //toolbar.setTitle("Calendario");
                    fragment = new CalendarioFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_estadisticas:
                    //toolbar.setTitle("Estadisticas");
                    fragment = new EstadisticasPilotosFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_mas:
                    //toolbar.setTitle("Más");
                    fragment = new MasFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    public void verTablaPilotos(View view){
        startActivity(new Intent(getBaseContext(), EstPorPiloto.class));
    }

    public void verTablaResultados(View view){
        startActivity(new Intent(getBaseContext(), EstPorResultados.class));
    }

    public void verTablaConstructores(View view){
        startActivity(new Intent(getBaseContext(), EstPorEscuderia.class));
    }

    public void verPilotos(View view){
        startActivity(new Intent(getBaseContext(), BuscarPilotosView.class));
    }

    public void hacerApuesta(View view){
        startActivity(new Intent(getBaseContext(), BetActivity.class));
    }

    public void verPerfil(View view){
        startActivity(new Intent(getBaseContext(), VerUsuarioView.class));
    }

}
