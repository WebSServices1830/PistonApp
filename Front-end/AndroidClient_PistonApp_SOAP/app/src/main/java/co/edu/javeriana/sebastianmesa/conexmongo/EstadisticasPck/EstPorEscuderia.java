package co.edu.javeriana.sebastianmesa.conexmongo.EstadisticasPck;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import co.edu.javeriana.sebastianmesa.conexmongo.AdminMainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class EstPorEscuderia extends AppCompatActivity {

    private String[][] datos = { { "1", "Sebas", "Ferrari", "20" },
            { "2", "Heikki", "Skyppy", "10" } };

    static String[] encabezado={"#","Escuderia","Podios"};
    private WebMet_ObtenerPiloto wm_validarLogin = null;
    private List<Escuderia> listaEscuderias = new ArrayList<>();
    public TableView<String[]> tableView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_por_escuderia);


        tableView = (TableView<String[]>) findViewById(R.id.tableView);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,encabezado));


        tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        tableView.setColumnCount(4);

        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                Toast.makeText(EstPorEscuderia.this, ((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
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
            final String METHOD_NAME = "verTodasLasEscuderias";
            final String SOAP_ACTION = "http://webservice.javeriana.co/verTodasLasEscuderias";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            //request.addProperty("textoBusquedaNombre","Lewis");

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);

                KvmSerializable ks = (KvmSerializable)envelope.bodyIn;


                for(int i = 0; i < ks.getPropertyCount(); ++i){

                    SoapObject escuderia = (SoapObject) ks.getProperty(i);

                    String id_str = escuderia.getPrimitivePropertyAsString("id_str");
                    String nombre = escuderia.getPrimitivePropertyAsString("nombre");
                    int cant_vecesEnPodio = Integer.parseInt(escuderia.getPrimitivePropertyAsString("cant_vecesEnPodio"));

                    Escuderia escuderiaObjeto= new Escuderia();
                    escuderiaObjeto.setId_str(id_str);
                    escuderiaObjeto.setNombre(nombre);
                    escuderiaObjeto.setCant_vecesEnPodio(cant_vecesEnPodio);

                    listaEscuderias.add(escuderiaObjeto);

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

                String[][] datos = new String[listaEscuderias.size()][4];

                for (int i = 0; i < listaEscuderias.size() ; i++){
                    for (int j = 0 ; j < 3 ; j++){
                        switch (j) {
                            case 0:
                                datos[i][j] = Integer.toString(i+1);
                                break;
                            case 1:
                                datos[i][j] = listaEscuderias.get(i).getNombre();
                                break;
                            case 3:
                                datos[i][j] = Integer.toString(listaEscuderias.get(i).getCant_vecesEnPodio());
                                break;

                        }
                    }

                }

//                Arrays.sort(datos, new Comparator() {
//                    public int compare(Object o1, Object o2) {
//                        Integer[] elt1 = (Integer[])o2;
//                        Integer[] elt2 = (Integer[])o1;
//                        return elt1[2].compareTo(elt2[2]);
//                    }
//                });

                tableView.setDataAdapter(new SimpleTableDataAdapter(getBaseContext(), datos));
                Toast.makeText(getBaseContext(),"Bien. Tam: " + listaEscuderias.size(), Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getBaseContext(), AdminMainActivity.class));
            Toast.makeText(getBaseContext(),"Error", Toast.LENGTH_LONG).show();
        }

    }


}
