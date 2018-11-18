package co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.GranPremio;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class GranPremioAdapter extends ArrayAdapter<GranPremio> {

    TextView granPremioPista;
    TextView granPremioFecha;
    String ciudad= "";
    GranPremio gp;

    private WebMet_verPista wm_verPista = null;

    public GranPremioAdapter(Context context, List<GranPremio> datos) {
        super(context, R.layout.content_gran_premio_adapter, datos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        gp = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_gran_premio_adapter, parent, false);
        }

        granPremioFecha = (TextView) convertView.findViewById(R.id.granPremioFecha);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        granPremioFecha.setText(dateFormat.format(gp.getFecha()));

        granPremioPista = (TextView) convertView.findViewById(R.id.granPremioPista);
        /*wm_verPista = new WebMet_verPista();
        wm_verPista.execute();*/
        consumeRESTVolleyVerPista();

        return convertView;
    }

    private class WebMet_verPista extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL = "http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "verPista";
            final String SOAP_ACTION = "http://webservice.javeriana.co/verPista";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("id", gp.getPista());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);

            try {
                ht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();

                if (response != null) {
                    ciudad = response.getPrimitivePropertyAsString("ciudad");
                    return true;
                }
            } catch (Exception e) {
                Log.i("Error", e.getMessage());
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean aBoolean) {
            if(aBoolean){
                granPremioPista.setText(ciudad);
            }
        }
    }

    public void consumeRESTVolleyVerPista(){
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "pistas";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject obj = jsonArray.getJSONObject(a);

                                if(obj.getString("id_str").equals(gp.getPista())) {
                                    ciudad = obj.getString("ciudad");
                                    granPremioPista.setText(ciudad);
                                    a= jsonArray.length();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Debug_GranPremioAdapter", "Error handling rest invocation"+error.getCause());
                    }
                }
        );
        queue.add(req);
    }

}
