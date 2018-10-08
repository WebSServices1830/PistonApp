package co.edu.javeriana.sebastianmesa.conexmongo.AutoPck;

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

import bolts.Bolts;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerAutoView extends AppCompatActivity {



    private EditText campoNombre;
    private Button consultaBtn;
    private String nombre, ruedas, combustible, foto_ref, motor_referencia, motor_cilindraje, motor_configuracion;
    private Double pesoEnKg;
    private Boolean motor_turbo;
    private int cant_vecesEnPodio, cant_TitulosCampeonato;
    private WebMet_VerAuto wm_agregarPiloto = null;
    private TextView campoRespuesta = null;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuedas() {
        return ruedas;
    }

    public void setRuedas(String ruedas) {
        this.ruedas = ruedas;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public String getFoto_ref() {
        return foto_ref;
    }

    public void setFoto_ref(String foto_ref) {
        this.foto_ref = foto_ref;
    }

    public String getMotor_referencia() {
        return motor_referencia;
    }

    public void setMotor_referencia(String motor_referencia) {
        this.motor_referencia = motor_referencia;
    }

    public String getMotor_cilindraje() {
        return motor_cilindraje;
    }

    public void setMotor_cilindraje(String motor_cilindraje) {
        this.motor_cilindraje = motor_cilindraje;
    }

    public String getMotor_configuracion() {
        return motor_configuracion;
    }

    public void setMotor_configuracion(String motor_configuracion) {
        this.motor_configuracion = motor_configuracion;
    }

    public Double getPesoEnKg() {
        return pesoEnKg;
    }

    public void setPesoEnKg(Double pesoEnKg) {
        this.pesoEnKg = pesoEnKg;
    }

    public Boolean getMotor_turbo() {
        return motor_turbo;
    }

    public void setMotor_turbo(Boolean motor_turbo) {
        this.motor_turbo = motor_turbo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_auto_view);

        consultaBtn =(Button) findViewById(R.id.btnVerAuto);
        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_VerAuto();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_VerAuto extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_auto?wsdl";
            final String METHOD_NAME = "auto_readByName";
            final String SOAP_ACTION = "http://webservice.javeriana.co/auto_readByName";

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
                    setRuedas(response.getPrimitivePropertyAsString("ruedas"));
                    setCombustible(response.getPrimitivePropertyAsString("combustible"));
                    setFoto_ref(response.getPrimitivePropertyAsString("foto_ref"));
                    //setMotor_referencia(response.getPrimitivePropertyAsString("motor_referencia"));
                    //setMotor_cilindraje(response.getPrimitivePropertyAsString("motor_cilindraje"));
                    //setMotor_configuracion(response.getPrimitivePropertyAsString("motor_configuracion"));
                    setPesoEnKg(Double.parseDouble(response.getPrimitivePropertyAsString("pesoEnKg")));
                    //setMotor_turbo(Boolean.parseBoolean((response.getPrimitivePropertyAsString("motor_turbo"))));

                    //campoRespuesta.setText(responseCode);

                }else{
                    campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                    campoRespuesta.setText("Auto no encontrado");
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
                                    "Nombre: "+getNombre()+"\n"+
                                    "Ruedas: "+getRuedas()+"\n"+
                                    "Combustible: "+getCombustible()+"\n"+
                                    "Ref Foto: "+getFoto_ref()+"\n"+
                                    "Peso: "+getPesoEnKg()+"\n"
//                                    "Tiene turbo?: "+getMotor_turbo()
//                                    "Ref Motor: "+getMotor_referencia()+"\n"+
//                                    "Cilindraje: "+getMotor_cilindraje()+"\n"+
//                                    "Configuración: "+getMotor_configuracion()
                    );

                    Toast.makeText(getApplicationContext(), "Viendo Auto", Toast.LENGTH_LONG).show();

                }
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
