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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerEscuderiaView extends AppCompatActivity {


    private EditText campoNombre;
    private Button consultaBtn;
    private String nombre, lugarBase, jefeEquipo, jefeTecnico, chasis, fotoEscudo_ref;
    private int cant_vecesEnPodio, cant_TitulosCampeonato;
    private WebMet_ConsultarEscuderia wm_agregarPiloto = null;
    private TextView campoRespuesta = null;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugarBase() {
        return lugarBase;
    }

    public void setLugarBase(String lugarBase) {
        this.lugarBase = lugarBase;
    }

    public String getJefeEquipo() {
        return jefeEquipo;
    }

    public void setJefeEquipo(String jefeEquipo) {
        this.jefeEquipo = jefeEquipo;
    }

    public String getJefeTecnico() {
        return jefeTecnico;
    }

    public void setJefeTecnico(String jefeTecnico) {
        this.jefeTecnico = jefeTecnico;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getFotoEscudo_ref() {
        return fotoEscudo_ref;
    }

    public void setFotoEscudo_ref(String fotoEscudo_ref) {
        this.fotoEscudo_ref = fotoEscudo_ref;
    }

    public int getCant_vecesEnPodio() {
        return cant_vecesEnPodio;
    }

    public void setCant_vecesEnPodio(int cant_vecesEnPodio) {
        this.cant_vecesEnPodio = cant_vecesEnPodio;
    }

    public int getCant_TitulosCampeonato() {
        return cant_TitulosCampeonato;
    }

    public void setCant_TitulosCampeonato(int cant_TitulosCampeonato) {
        this.cant_TitulosCampeonato = cant_TitulosCampeonato;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_escuderia_view);

        consultaBtn =(Button) findViewById(R.id.btnVerEscuderia);
        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_ConsultarEscuderia();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_ConsultarEscuderia extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_escuderia?wsdl";
            final String METHOD_NAME = "escuderia_readByName";
            final String SOAP_ACTION = "http://webservice.javeriana.co/escuderia_readByName";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            campoNombre = (EditText) findViewById(R.id.nombreEscuderia);
            request.addProperty("nombre", campoNombre.getText().toString());


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject)envelope.getResponse();

                if (response != null) {


                    setNombre(response.getPrimitivePropertyAsString("nombre"));
                    setLugarBase(response.getPrimitivePropertyAsString("lugarBase"));
                    setJefeEquipo(response.getPrimitivePropertyAsString("jefeEquipo"));
                    setJefeTecnico(response.getPrimitivePropertyAsString("jefeTecnico"));
                    setChasis(response.getPrimitivePropertyAsString("chasis"));
                    setFotoEscudo_ref(response.getPrimitivePropertyAsString("fotoEscudo_ref"));
                    setCant_vecesEnPodio(Integer.parseInt(response.getPrimitivePropertyAsString("cant_vecesEnPodio")));
                    setCant_TitulosCampeonato(Integer.parseInt(response.getPrimitivePropertyAsString("cant_TitulosCampeonato")));

                    //campoRespuesta.setText(responseCode);

                }else{
                    campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                    campoRespuesta.setText("Escuderia no encontrada");
                }


            }
            catch (Exception e)
            {
                Log.i("Error: ",e.getMessage());
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
            }
            else{

                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);

                if (getNombre() == null){
                    campoRespuesta.setText("Escuderia no encontrada");
                    Toast.makeText(getApplicationContext(), "Prueba otro nombre", Toast.LENGTH_LONG).show();
                }else{
                    campoRespuesta.setText(
                            "Nombre: "+getNombre()+"\n"+
                            "Lugar base: "+getLugarBase()+"\n"+
                            "Jefe equipo: "+getJefeEquipo()+"\n"+
                            "Jefe técnico: "+getJefeTecnico()+"\n"+
                            "chasis: "+getChasis()+"\n"+
                            "Podios: "+getCant_vecesEnPodio()+"\n"+
                            "Titulos: "+getCant_TitulosCampeonato()+"\n"+
                            "Ref fot: "+getFotoEscudo_ref()
                    );

                    Toast.makeText(getApplicationContext(), "Viendo Escuderia", Toast.LENGTH_LONG).show();

                }
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
