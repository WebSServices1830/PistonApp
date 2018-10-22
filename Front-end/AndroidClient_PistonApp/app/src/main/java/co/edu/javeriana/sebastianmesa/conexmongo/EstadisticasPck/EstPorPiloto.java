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
import java.util.Date;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.AdminMainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.Managers.ManagerUsuario;
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

public class EstPorPiloto extends AppCompatActivity {

    private String[][] datos = { { "1", "Sebas", "Ferrari", "20" },
            { "2", "Heikki", "Skyppy", "10" } };

    static String[] encabezado={"#","Nombre","Escuderia","Puntos"};
    private WebMet_ObtenerPiloto wm_validarLogin = null;
    private List<Piloto> listaPilotos = new ArrayList<>();
    public TableView<String[]> tableView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_por_piloto);


        tableView = (TableView<String[]>) findViewById(R.id.tableView);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,encabezado));


        tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tableView.setColumnCount(4);

        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                Toast.makeText(EstPorPiloto.this, ((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
            }
        });



        wm_validarLogin = new WebMet_ObtenerPiloto();
        wm_validarLogin.execute();

    }


    private class WebMet_ObtenerPiloto extends AsyncTask<Void, Piloto, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "verTodosLosPilotos";
            final String SOAP_ACTION = "http://webservice.javeriana.co/verTodosLosPilotos";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            //request.addProperty("textoBusquedaNombre","Lewis");

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);

                KvmSerializable ks = (KvmSerializable)envelope.bodyIn;


                for(int i = 0; i < ks.getPropertyCount(); ++i){

                    SoapObject piloto = (SoapObject) ks.getProperty(i);

                    String id_str = piloto.getPrimitivePropertyAsString("id_str");
                    String nombreCompleto = piloto.getPrimitivePropertyAsString("nombreCompleto");
                    Date fecha_Nacimiento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(piloto.getPrimitivePropertyAsString("fecha_Nacimiento"));
                    String lugarNacimiento = piloto.getPrimitivePropertyAsString("lugarNacimiento");;
                    String foto_ref = piloto.getPrimitivePropertyAsString("foto_ref");;
                    int cant_podiosTotales = 0;
                    int cant_puntosTotales = 0;
                    int cant_granPremiosIngresado = 0;
                    float calificacion = 10;

                    Piloto pilotoObjeto= new Piloto();
                    pilotoObjeto.setId_str(id_str);
                    pilotoObjeto.setNombreCompleto(nombreCompleto);
                    pilotoObjeto.setFecha_Nacimiento(fecha_Nacimiento);
                    pilotoObjeto.setLugarNacimiento(lugarNacimiento);
                    pilotoObjeto.setFoto_ref(foto_ref);
                    pilotoObjeto.setCant_podiosTotales(cant_podiosTotales);
                    pilotoObjeto.setCant_puntosTotales(cant_puntosTotales);
                    pilotoObjeto.setCant_granPremiosIngresado(cant_granPremiosIngresado);
                    pilotoObjeto.setCalificacion(calificacion);

                    listaPilotos.add(pilotoObjeto);

                    publishProgress();

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
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getBaseContext(),"Error", Toast.LENGTH_SHORT).show();
            }
            else{

                String[][] datosp = { { "1", "Sebas", "Ferrari", "20" },
                        { "2", listaPilotos.get(0).getNombreCompleto(), "Skyppy", "10" } };

                tableView.setDataAdapter(new SimpleTableDataAdapter(getBaseContext(), datosp));
                Toast.makeText(getBaseContext(),"Bien. Tam: " + listaPilotos.size(), Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getBaseContext(), AdminMainActivity.class));
            Toast.makeText(getBaseContext(),"Error", Toast.LENGTH_LONG).show();
        }

    }


}
