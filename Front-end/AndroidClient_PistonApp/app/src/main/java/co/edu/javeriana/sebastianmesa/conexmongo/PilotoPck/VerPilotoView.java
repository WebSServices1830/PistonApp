package co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck;

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
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerPilotoView extends AppCompatActivity {


    private EditText campoId;
    private Button consultaBtn;
    private String Nombre, lugar, foto;
    private Date fecha;
    private int podios, puntos, premios;
    private WebMet_ConsultarPiloto wm_agregarPiloto = null;
    private TextView campoRespuesta = null;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getPodios() {
        return podios;
    }

    public void setPodios(int podios) {
        this.podios = podios;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getPremios() {
        return premios;
    }

    public void setPremios(int premios) {
        this.premios = premios;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_piloto_view);

        consultaBtn =(Button) findViewById(R.id.agregarPiloto);
        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_ConsultarPiloto();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_ConsultarPiloto extends AsyncTask<Void, Void, Boolean> {



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_piloto?wsdl";
            final String METHOD_NAME = "piloto_readByName";
            final String SOAP_ACTION = "http://webservice.javeriana.co/piloto_readByName";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            campoId = (EditText) findViewById(R.id.idPiloto);
            request.addProperty("nombreCompleto", campoId.getText().toString());


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject)envelope.getResponse();

                if (response != null) {

                    String string = "January 2, 2010";
                    DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                    //Date date = format.parse(string);

                    setNombre(response.getPrimitivePropertyAsString("nombreCompleto"));
                    //setFecha( format.parse(response.getPrimitivePropertyAsString("fecha_Nacimiento")));
                    setLugar(response.getPrimitivePropertyAsString("lugarNacimiento"));
                    //setFoto(response.getPrimitivePropertyAsString("foto_ref"));
                    setPodios( Integer.parseInt (response.getPrimitivePropertyAsString("cant_podiosTotales")));
                    setPuntos(Integer.parseInt (response.getPrimitivePropertyAsString("cant_puntosTotales")));
                    setPremios(Integer.parseInt (response.getPrimitivePropertyAsString("cant_granPremiosIngresado")));

                    //campoRespuesta.setText(responseCode);

                }else{
                    campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                    campoRespuesta.setText("Piloto no encontrado");
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
                    campoRespuesta.setText("Piloto no encontrado");
                    Toast.makeText(getApplicationContext(), "Prueba otro nombre", Toast.LENGTH_LONG).show();
                }else{
                    campoRespuesta.setText(
                            "Nombre: "+getNombre()+/*getFecha()+*/"\n"+
                                    "Lugar nacimiento: "+getLugar()+"\n"+
//                                "Foto path:"+getFoto()+"\n"+
                                    "Podios: "+getPodios()+"\n"+
                                    "Puntos: "+getPuntos()+"\n"+
                                    "Premios: "+getPremios());

                    Toast.makeText(getApplicationContext(), "Viendo Piloto", Toast.LENGTH_LONG).show();

                }
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
