package co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.GranPremio;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Record;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.ClienteMongo;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.VerPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerGPView extends AppCompatActivity {

    private ImageView imagenViewGP2;
    TextView tituloPistaViewGP2;
    TextView textViewTiempoRecordViewGP2;
    TextView textViewNombrePilotoRecordViewGp2;
    TextView textViewFechaRecordViewGP2;
    TextView fechaCarreraViewGp2;
    TextView textViewKMViewGP2;
    TextView textViewCalificacionViewGP2;
    TextView textViewVueltasViewGP2;
    DownloadImageTask downloadImageTask;
    GranPremio gp;
    String ciudad= "";
    String foto_ref= "";
    float distanciaCarrera_km= 0;
    float calificacion= 0;
    Record record;
    WebMet_verPista wb_verPista= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_gpview);



        //wb_verPista= new WebMet_verPista();

        imagenViewGP2= findViewById(R.id.imagenViewGP);
        tituloPistaViewGP2= findViewById(R.id.tituloPistaViewGP);
        textViewTiempoRecordViewGP2= findViewById(R.id.textViewTiempoRecordViewGP);
        textViewNombrePilotoRecordViewGp2= findViewById(R.id.textViewNombrePilotoRecordViewGp);
        textViewFechaRecordViewGP2= findViewById(R.id.textViewFechaRecordViewGP);
        fechaCarreraViewGp2= findViewById(R.id.fechaCarreraViewGp);
        textViewKMViewGP2= findViewById(R.id.textViewKMViewGP);
        textViewCalificacionViewGP2= findViewById(R.id.textViewCalificacionViewGP);
        textViewVueltasViewGP2= findViewById(R.id.textViewVueltasViewGP);

        gp = (GranPremio) getIntent().getSerializableExtra("GranPremio");

        textViewVueltasViewGP2.setText( Integer.toString(gp.getCantVueltas()) );
        Date fecha = gp.getFecha();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(fecha);
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        fechaCarreraViewGp2.setText("Fecha de la carrera: "+anio+"/"+mes+"/"+dia);

        //wb_verPista.execute();
        consumeRESTVolleyVerPista();



    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        MongoClient mongoClient = ClienteMongo.getInstancia();

        // get handle to "PistonAppDB" database
        MongoDatabase database = mongoClient.getDatabase("PistonAppDB");

        // Create a gridFSBucket with a custom bucket name "files"
        GridFSBucket gridFSFilesBucket = GridFSBuckets.create(database, "almacenamiento");

        private Exception exception;

        protected Bitmap doInBackground(String... ids) {
            ObjectId fileId = new ObjectId(ids[0]); //The id of a file uploaded to GridFS, initialize to valid file id

            GridFSDownloadStream downloadStream = gridFSFilesBucket.openDownloadStream(fileId);
            int fileLength = (int) downloadStream.getGridFSFile().getLength();
            byte[] bytesToWriteTo = new byte[fileLength];
            downloadStream.read(bytesToWriteTo);
            downloadStream.close();

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytesToWriteTo, 0, bytesToWriteTo.length);


            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                imagenViewGP2.setImageBitmap(bitmap);
            }
        }
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
                    foto_ref= response.getPrimitivePropertyAsString("foto_ref");
                    distanciaCarrera_km= Float.parseFloat(response.getPrimitivePropertyAsString("distanciaCarrera_km"));
                    calificacion= Float.parseFloat(response.getPrimitivePropertyAsString("calificacion"));
                    SoapObject recordSoap = (SoapObject) response.getProperty("record");

                    record = new Record();

                    record.setRecordVuelta_anio( Integer.parseInt(recordSoap.getPrimitivePropertyAsString("recordVuelta_anio")) );
                    record.setRecordVuelta_piloto( recordSoap.getPrimitivePropertyAsString("recordVuelta_piloto"));
                    record.setRecordVuleta_tiempo( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(recordSoap.getPrimitivePropertyAsString("recordVuleta_tiempo")));

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

                tituloPistaViewGP2.setText(ciudad);
                textViewKMViewGP2.setText(distanciaCarrera_km+"km");
                textViewCalificacionViewGP2.setText(calificacion+" estrellas");
                textViewFechaRecordViewGP2.setText( Integer.toString(record.getRecordVuelta_anio()) );
                textViewNombrePilotoRecordViewGp2.setText( record.getRecordVuelta_piloto().toString() );
                textViewTiempoRecordViewGP2.setText( record.getRecordVuleta_tiempo().toString());

                downloadImageTask = new VerGPView.DownloadImageTask();
                downloadImageTask.execute(foto_ref);
            }
        }
    }

    public void consumeRESTVolleyVerPista(){
        RequestQueue queue = Volley.newRequestQueue(VerGPView.this);
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
                                    foto_ref= obj.getString("foto_ref");
                                    distanciaCarrera_km= Float.parseFloat(obj.getString("distanciaCarrera_km"));
                                    calificacion= Float.parseFloat(obj.getString("calificacion"));
                                    JSONObject recordJson= (JSONObject) obj.get("record");

                                    record = new Record();

                                    record.setRecordVuelta_anio( recordJson.getInt("recordVuelta_anio") );
                                    record.setRecordVuelta_piloto( recordJson.getString("recordVuelta_piloto"));
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    record.setRecordVuleta_tiempo( simpleDateFormat.parse(recordJson.getString("recordVuleta_tiempo")));

                                    tituloPistaViewGP2.setText(ciudad);
                                    textViewKMViewGP2.setText(distanciaCarrera_km+"km");
                                    textViewCalificacionViewGP2.setText(calificacion+" estrellas");
                                    textViewFechaRecordViewGP2.setText( Integer.toString(record.getRecordVuelta_anio()) );
                                    textViewNombrePilotoRecordViewGp2.setText( record.getRecordVuelta_piloto() );
                                    textViewTiempoRecordViewGP2.setText( record.getRecordVuleta_tiempo().toString());

                                    downloadImageTask = new VerGPView.DownloadImageTask();
                                    downloadImageTask.execute(foto_ref);

                                    a= jsonArray.length();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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
