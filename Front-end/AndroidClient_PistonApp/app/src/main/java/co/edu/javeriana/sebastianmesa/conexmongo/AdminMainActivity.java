package co.edu.javeriana.sebastianmesa.conexmongo;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import co.edu.javeriana.sebastianmesa.conexmongo.AutoPck.IndexAutoView;
import co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck.IndexEscuderiaView;
import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.IndexGPView;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.Managers.ManagerUsuario;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Usuario;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.IndexPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.IndexUsuarioView;
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

    private WebMet_InicializarCampeonato wm_inicializarCampeonato = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        startActivity(new Intent(getBaseContext(), LoginActivityView.class));
    }

    public void cargarDatos (View view){
        wm_inicializarCampeonato = new WebMet_InicializarCampeonato();
        wm_inicializarCampeonato.execute();
    }

    private class WebMet_InicializarCampeonato extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";

            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "inicializarCampeonato";
            final String SOAP_ACTION = "http://webservice.javeriana.co/inicializarCampeonato";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();
                return true;
            }
            catch (Exception e)
            {
                Log.i("Error: ",e.getMessage());
                e.printStackTrace();

            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Datos cargados", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getBaseContext(), LoginActivityView.class));
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
        }
    }

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
                    toolbar.setTitle("Index");
                    fragment = new IndexFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_calendario:
                    toolbar.setTitle("Calendario");
                    fragment = new CalendarioFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_estadisticas:
                    toolbar.setTitle("Estadisticas");
                    fragment = new EstadisticasPilotosFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_mas:
                    toolbar.setTitle("Más");
                    fragment = new MasFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

}
