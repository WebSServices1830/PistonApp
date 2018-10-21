package co.edu.javeriana.sebastianmesa.conexmongo.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.R;


public class CalendarioFragment extends Fragment {

    private Button button_test;

    private WebMet_GranPremiosOrdenadoPorFecha wm_granPremiosOrdenadoPorFecha = null;

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

        button_test = view.findViewById(R.id.button_test);

        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm_granPremiosOrdenadoPorFecha = new WebMet_GranPremiosOrdenadoPorFecha();
                wm_granPremiosOrdenadoPorFecha.execute();
            }
        });

        return view;
    }

    private class WebMet_GranPremiosOrdenadoPorFecha extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "granPremiosOrdenadoPorFecha";
            final String SOAP_ACTION = "http://webservice.javeriana.co/granPremiosOrdenadoPorFecha";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);

                KvmSerializable ks = (KvmSerializable)envelope.bodyIn;
                for(int i = 0; i < ks.getPropertyCount(); ++i){
                    SoapObject granPremio = (SoapObject) ks.getProperty(i);

                    String id_string = granPremio.getPropertyAsString("id_str");
                    Log.i("Gran premio",id_string);
                }
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
                startActivity(new Intent(getActivity(), LoginActivityView.class));
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
            }
            else{
            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getActivity(), LoginActivityView.class));
            Toast.makeText(getActivity(),"Error", Toast.LENGTH_LONG).show();
        }
    }

}
