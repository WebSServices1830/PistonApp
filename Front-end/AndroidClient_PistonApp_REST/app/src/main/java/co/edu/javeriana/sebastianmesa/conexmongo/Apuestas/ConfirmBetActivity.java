package co.edu.javeriana.sebastianmesa.conexmongo.Apuestas;

import android.content.Intent;
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

import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.VerGPView;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.GranPremio;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import co.edu.javeriana.sebastianmesa.conexmongo.fragment.CalendarioFragment;

public class ConfirmBetActivity extends AppCompatActivity {

    Spinner spinner_granPremios;
    Button button_apostar;
    EditText editText_montoApostar;
    TextView textView_conductor;

    private String idCalendarioCampeonato = "";

    private List<GranPremio> listagranPremios = new ArrayList<>();

    ArrayAdapter<GranPremio> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bet);

        spinner_granPremios = findViewById(R.id.spinner_granPremios);
        editText_montoApostar = findViewById(R.id.editText_montoApuesta);
        textView_conductor = findViewById(R.id.textView_pilotoApostar);
        button_apostar = findViewById(R.id.button_apostar);

        Piloto piloto = (Piloto) getIntent().getSerializableExtra("Piloto");

        textView_conductor.setText(textView_conductor.getText() + piloto.getNombreCompleto());

        button_apostar.setEnabled(false);

        adapter = new ArrayAdapter<GranPremio>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, listagranPremios);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinner_granPremios.setAdapter(adapter);

        WebMet_verCampeonatoString wm_verCampeonatoString = new WebMet_verCampeonatoString();
        wm_verCampeonatoString.execute();
        //Log.i("idCampeonato",idCalendarioCampeonato);

        WebMet_GranPremiosOrdenadoPorFecha wm_granPremiosOrdenadoPorFecha = new WebMet_GranPremiosOrdenadoPorFecha();
        wm_granPremiosOrdenadoPorFecha.execute();

        spinner_granPremios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                button_apostar.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_apostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private class WebMet_GranPremiosOrdenadoPorFecha extends AsyncTask<Void, GranPremio, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "granPremiosOrdenadoPorFecha";
            final String SOAP_ACTION = "http://webservice.javeriana.co/granPremiosOrdenadoPorFecha";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("id_campeonato",idCalendarioCampeonato);

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);

                KvmSerializable ks = (KvmSerializable)envelope.bodyIn;
                for(int i = 0; i < ks.getPropertyCount(); ++i){
                    SoapObject granPremio = (SoapObject) ks.getProperty(i);

                    String id_string = granPremio.getPrimitivePropertyAsString("id_str");
                    Date fecha= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(granPremio.getPrimitivePropertyAsString("fecha"));
                    String idPista= granPremio.getPrimitivePropertyAsString("pista");
                    int cantVueltas= Integer.parseInt(granPremio.getPrimitivePropertyAsString("cantVueltas"));
                    Date mejorVuelta= null;
                    try {
                        mejorVuelta = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(granPremio.getPrimitivePropertyAsString("mejorVuelta"));
                    }catch (ParseException e){
                        mejorVuelta = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(granPremio.getPrimitivePropertyAsString("mejorVuelta"));
                    }
                    //List<String> id_clasificaciones= Collections.singletonList(granPremio.getPrimitivePropertyAsString("clasificaciones"));

                    GranPremio granPremioObjeto= new GranPremio();
                    granPremioObjeto.setId_str(id_string);
                    granPremioObjeto.setFecha(fecha);
                    granPremioObjeto.setPista(idPista);
                    granPremioObjeto.setCantVueltas(cantVueltas);
                    granPremioObjeto.setMejorVuelta(mejorVuelta);
                    //granPremioObjeto.setId_clasificaciones(id_clasificaciones);
                    publishProgress(granPremioObjeto);
                }
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
        protected void onProgressUpdate(GranPremio... values) {
            listagranPremios.add(values[0]);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getBaseContext(),"Error", Toast.LENGTH_SHORT).show();
            }
            else{
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getBaseContext(),"Error", Toast.LENGTH_LONG).show();
        }
    }

    private class WebMet_verCampeonatoString extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL = "http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "verCampeonato";
            final String SOAP_ACTION = "http://webservice.javeriana.co/verCampeonato";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("nombre", "Campeonato 2018");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);

            try {
                ht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();

                if (response != null) {
                    idCalendarioCampeonato = response.getPrimitivePropertyAsString("id_str");
                    return true;
                }
            } catch (Exception e) {
                Log.i("Error", e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
    }
}
