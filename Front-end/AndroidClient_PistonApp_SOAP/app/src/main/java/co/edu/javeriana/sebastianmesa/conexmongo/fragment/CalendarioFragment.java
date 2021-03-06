package co.edu.javeriana.sebastianmesa.conexmongo.fragment;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.GranPremioAdapter;
import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.VerGPView;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.GranPremio;
import co.edu.javeriana.sebastianmesa.conexmongo.R;


public class CalendarioFragment extends Fragment {

    private Spinner spinner;
    private List<GranPremio> listagranPremios = new ArrayList<>();
    private String idCalendarioCampeonato= "";
    private String calendarioSeleccionado= "";
    private ListView listaView;
    private GranPremioAdapter gpAdapter;

    private WebMet_GranPremiosOrdenadoPorFecha wm_granPremiosOrdenadoPorFecha = null;
    private WebMet_verCampeonatoString wm_verCampeonatoString = null;

    public CalendarioFragment() {
        // Required empty public constructor
    }

    public static CalendarioFragment newInstance(String param1, String param2) {
        CalendarioFragment fragment = new CalendarioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        spinner = view.findViewById(R.id.calendarioCampeonato);
        listaView = (ListView) view.findViewById(R.id.listCalendarioCampeonato);

        gpAdapter = new GranPremioAdapter(view.getContext(), listagranPremios);
        listaView.setAdapter(gpAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calendarioSeleccionado= (String) spinner.getSelectedItem();
                //Log.i("calendarioSeleccionado",calendarioSeleccionado);
                wm_verCampeonatoString= new WebMet_verCampeonatoString();
                wm_verCampeonatoString.execute();
                //Log.i("idCampeonato",idCalendarioCampeonato);

                wm_granPremiosOrdenadoPorFecha = new WebMet_GranPremiosOrdenadoPorFecha();
                wm_granPremiosOrdenadoPorFecha.execute();
                listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(view.getContext(), VerGPView.class);
                        intent.putExtra("GranPremio", listagranPremios.get(position));
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }


    private class WebMet_GranPremiosOrdenadoPorFecha extends AsyncTask<Void, GranPremio, Boolean>{


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
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
            }
            else{
                gpAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getActivity(), LoginActivityView.class));
            Toast.makeText(getActivity(),"Error", Toast.LENGTH_LONG).show();
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

            request.addProperty("nombre", calendarioSeleccionado);

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
