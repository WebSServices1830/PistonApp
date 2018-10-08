package co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import bolts.Bolts;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerUsuarioView extends AppCompatActivity {


    private EditText campoId;
    private Button consultaBtn;
    private String nombreUsuario, contra, descripcion,foto;
    private int edad;
    private boolean admin;
    private long bolsillo;
    private WebMet_ConsultarUsuario wm_agregarPiloto = null;
    private TextView campoRespuesta = null;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public long getBolsillo() {
        return bolsillo;
    }

    public void setBolsillo(long bolsillo) {
        this.bolsillo = bolsillo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario_view);

        consultaBtn =(Button) findViewById(R.id.verUsuario);
        consultaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new WebMet_ConsultarUsuario();
                wm_agregarPiloto.execute();
            }
        });

    }

    private class WebMet_ConsultarUsuario extends AsyncTask<Void, Void, Boolean> {



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_usuario?wsdl";
            final String METHOD_NAME = "usuario_readByName";
            final String SOAP_ACTION = "http://webservice.javeriana.co/usuario_readByName";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            campoId = (EditText) findViewById(R.id.nomUusario);
            request.addProperty("nombreUsuario", campoId.getText().toString());


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject)envelope.getResponse();

                if (response != null) {


                    setNombreUsuario(response.getPrimitivePropertyAsString("nombreUsuario"));
                    setContra(response.getPrimitivePropertyAsString("contra"));
                    setEdad( Integer.parseInt(response.getPrimitivePropertyAsString("edad")));
                    setDescripcion(response.getPrimitivePropertyAsString("descripcion"));
                    setFoto(response.getPrimitivePropertyAsString("foto"));
                    setAdmin(Boolean.parseBoolean (response.getPrimitivePropertyAsString("admin")));
                    setBolsillo(Long.parseLong (response.getPrimitivePropertyAsString("bolsillo")));

                    //campoRespuesta.setText(responseCode);

                }else{
                    campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                    campoRespuesta.setText("Usuario no encontrado");
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

                if (getNombreUsuario() == null){
                    campoRespuesta.setText("Usuario no encontrado");
                    Toast.makeText(getApplicationContext(), "Prueba otro nombre", Toast.LENGTH_LONG).show();
                }else{
                    campoRespuesta.setText(
                            "Nombre: "+getNombreUsuario()+/*getFecha()+*/"\n"+
                                    "Edad: "+getEdad()+"\n"+
                                    "Descripción: "+getDescripcion()+"\n"+
                                    "Foto: "+getFoto()+"\n"+
                                    "Admin: "+isAdmin()+"\n"+
                                    "Bolsillo: "+getBolsillo()
                    );

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
