package co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.ClienteMongo;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.VerPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerEscuderiaView extends AppCompatActivity {

    private final static String TAG = "Log_VerEscueria";


    private EditText campoNombre;
    private Button consultaBtn;

    private ListView listView_escuderias;
    private EscuderiaAdapter adapterEscuderia;
    private List<Escuderia> escuderias = new ArrayList<>();

    private ImageView imageView_fotoEscuderia;
    TextView textView_nombreCompleto;
    TextView textView_lugarBase;
    TextView textView_jefeTecnico;
    TextView textView_jefeEquipo;

    DownloadImageTask downloadImageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_escuderia_view);

        imageView_fotoEscuderia = findViewById(R.id.imageView_perfilPiloto);
        textView_nombreCompleto = findViewById(R.id.textView_nombreCompleto);
        textView_lugarBase = findViewById(R.id.textView_lugarBase);
        textView_jefeTecnico = findViewById(R.id.textView_jefeTecnico);
        textView_jefeEquipo = findViewById(R.id.textView_jefeEquipo);

        final Escuderia escuderia = (Escuderia) getIntent().getSerializableExtra("Escuderia");

        downloadImageTask = new VerEscuderiaView.DownloadImageTask();
        downloadImageTask.execute(escuderia.getFotoEscudo_ref());

//        downloadImageTask = new VerPilotoView.DownloadImageTask();
//        downloadImageTask.execute(piloto.getFoto_ref());

        textView_nombreCompleto.setText(escuderia.getNombre());
        textView_lugarBase.setText(escuderia.getLugarBase());
        textView_jefeTecnico.setText(escuderia.getJefeTecnico());
        textView_jefeEquipo.setText(escuderia.getJefeEquipo());

        //getEscuderias(campoNombre.getText().toString());

//        adapterEscuderia = new EscuderiaAdapter(this, escuderias);
//        listView_escuderias.setAdapter(adapterEscuderia);
//
//        consultaBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                escuderias.clear();
//                adapterEscuderia.notifyDataSetChanged();
//
//                getEscuderias(campoNombre.getText().toString());
//            }
//        });

    }

    public void getEscuderias(String textoFiltro_nombreEscuderia){
        if(!escuderias.isEmpty()){
            escuderias.clear();
        }
        RequestQueue queue = Volley.newRequestQueue(VerEscuderiaView.this);
        String url = "http://10.0.2.2:8080/myapp/PistonApp/";
        String path = "escuderias/"+textoFiltro_nombreEscuderia;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url+path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject escuderia_json = jsonArray.getJSONObject(a);
                                String id_str = escuderia_json.getString("id_str");
                                String nombre = escuderia_json.getString("nombre");
                                String lugarBase = escuderia_json.getString("lugarBase");
                                String jefeEquipo = escuderia_json.getString("jefeEquipo");
                                String jefeTecnico = escuderia_json.getString("jefeTecnico");
                                String chasis = escuderia_json.getString("chasis");
                                String fotoEscudo_ref = escuderia_json.getString("fotoEscudo_ref");
                                int cant_vecesEnPodio = Integer.parseInt(escuderia_json.getString("cant_vecesEnPodio"));
                                int cant_TitulosCampeonato = Integer.parseInt(escuderia_json.getString("cant_TitulosCampeonato"));
                                
                                Escuderia escuderia = new Escuderia();

                                escuderia.setId(new ObjectId(id_str));
                                escuderia.setId_str(id_str);
                                escuderia.setNombre(nombre);
                                escuderia.setLugarBase(lugarBase);
                                escuderia.setJefeEquipo(jefeEquipo);
                                escuderia.setJefeTecnico(jefeTecnico);
                                escuderia.setChasis(chasis);
                                escuderia.setFotoEscudo_ref(fotoEscudo_ref);
                                escuderia.setCant_vecesEnPodio(cant_vecesEnPodio);
                                escuderia.setCant_TitulosCampeonato(cant_TitulosCampeonato);

                                escuderias.add( escuderia );

                            }
                            adapterEscuderia.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "JSONException ", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error handling rest invocation"+error.getCause());
                    }
                }
        );
        queue.add(req);
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
                imageView_fotoEscuderia.setImageBitmap(bitmap);
            }
        }
    }

}
