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
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class BuscarPilotosView extends AppCompatActivity {

    private ListView listView_pilotos;
    private List<Piloto> listaPilotos = new ArrayList<>();
    private PilotoAdapter pilotoAdapter;

    private EditText campo_nombrePiloto;
    private Button consultaBtn;

    private WebMet_VerPilotosPorNombre wm_verPilotosPorNombre = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_pilotos_view);

        campo_nombrePiloto = findViewById(R.id.input_nombrePiloto);
        consultaBtn =findViewById(R.id.agregarPiloto);
        listView_pilotos = findViewById(R.id.listView_pilotos);

        wm_verPilotosPorNombre = new WebMet_VerPilotosPorNombre();
        wm_verPilotosPorNombre.execute();

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

                wm_verPilotosPorNombre = new WebMet_VerPilotosPorNombre();
                wm_verPilotosPorNombre.execute();
            }
        });



    }

    private class WebMet_VerPilotosPorNombre extends AsyncTask<Void, Piloto, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "verPilotosPorNombre";
            final String SOAP_ACTION = "http://webservice.javeriana.co/verPilotosPorNombre";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("textoBusquedaNombre", campo_nombrePiloto.getText().toString());

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                ht.call(SOAP_ACTION, envelope);

                KvmSerializable ks = (KvmSerializable)envelope.bodyIn;
                for(int i = 0; i < ks.getPropertyCount(); ++i){
                    SoapObject driver = (SoapObject) ks.getProperty(i);

                    String id_str = driver.getPropertyAsString("id_str");
                    String nombreCompleto = driver.getPrimitivePropertyAsString("nombreCompleto");
                    Date fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(driver.getPrimitivePropertyAsString("fecha_Nacimiento"));
                    String lugarNacimiento = driver.getPrimitivePropertyAsString("lugarNacimiento");
                    String foto_ref = driver.getPrimitivePropertyAsString("foto_ref");
                    int cant_podiosTotales = Integer.parseInt( driver.getPrimitivePropertyAsString("cant_podiosTotales") );
                    int cant_puntosTotales = Integer.parseInt( driver.getPrimitivePropertyAsString("cant_puntosTotales") );
                    int cant_granPremiosIngresado = Integer.parseInt( driver.getPrimitivePropertyAsString("cant_granPremiosIngresado") );
                    float calificacion = Float.parseFloat( driver.getPrimitivePropertyAsString("calificacion") );

                    Log.i("Driver",nombreCompleto);

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

                    publishProgress(piloto);
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
        protected void onProgressUpdate(Piloto... values) {
            listaPilotos.add(values[0]);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
            }
            else{
                pilotoAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
        }
    }
}
