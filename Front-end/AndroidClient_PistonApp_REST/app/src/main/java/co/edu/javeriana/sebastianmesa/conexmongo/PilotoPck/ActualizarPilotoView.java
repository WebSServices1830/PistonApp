package co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.ClienteMongo;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.FileUpload;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class ActualizarPilotoView extends AppCompatActivity {

    private final static String TAG = "Log_ActualizarPiloto";
    private final static int STORAGE_PERMISSION = 1;
    private final static int CAMERA_PERMISSION = 2;

    static final int IMAGE_PICKER_REQUEST = 10;
    static final int IMAGE_CAPTURE_REQUEST = 20;

    private ImageView previewFoto;
    private ImageButton buttonCamara, buttonGaleria;

    private EditText nombre, fecha, lugar, podios, puntos, gp;
    private Button updateP;
    private Piloto pilotoIntent = null;
    DownloadImageTask downloadImageTask;

    private Date fechaNacimiento_piloto = null;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_piloto_view);

        pilotoIntent = (Piloto) getIntent().getSerializableExtra("Piloto");

        previewFoto= (ImageView) findViewById(R.id.imageViewActualizarPilotoImage);
        buttonCamara = (ImageButton) findViewById(R.id.imageButtonActualizarPilotoCamara);
        buttonGaleria = (ImageButton) findViewById(R.id.imageButtonActualizarPilotoGaleria);
        nombre = (EditText) findViewById(R.id.editText_nombrePiloto);
        lugar = (EditText) findViewById(R.id.editText_lugarNacimientoPiloto);
        foto = null;
        podios   = (EditText) findViewById(R.id.editText_podiosTotales);
        puntos = (EditText) findViewById(R.id.editText_puntosTotales);
        gp = (EditText) findViewById(R.id.editText_ingresosGPTotales);
        fecha = (EditText) findViewById(R.id.editText_fechaPiloto);

        downloadImageTask = new DownloadImageTask();
        downloadImageTask.execute(pilotoIntent.getFoto_ref());
        nombre.setText(pilotoIntent.getNombreCompleto());
        lugar.setText(pilotoIntent.getLugarNacimiento());
        podios.setText(Integer.toString( pilotoIntent.getCant_podiosTotales() ));
        puntos.setText(Integer.toString( pilotoIntent.getCant_puntosTotales() ));
        gp.setText(Integer.toString( pilotoIntent.getCant_granPremiosIngresado() ));

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(pilotoIntent.getFecha_Nacimiento());

        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        fecha.setText(dia+"/"+mes+"/"+anio);

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ActualizarPilotoView.this,
                        AlertDialog.THEME_HOLO_DARK, mDateSetListener, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaNacimiento_piloto = new GregorianCalendar(year, month, dayOfMonth).getTime();

                month += 1;
                Log.d(TAG, "onDateSet: date:" + dayOfMonth + "/" + month + "/" + year);

                fecha.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };

        buttonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(ActualizarPilotoView.this, Manifest.permission.READ_EXTERNAL_STORAGE, "Se necesita acceso al almacenamiento", STORAGE_PERMISSION);
            }
        });

        buttonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(ActualizarPilotoView.this, Manifest.permission.CAMERA, "Se necesita acceso a la cámara", CAMERA_PERMISSION);
            }
        });

        updateP =(Button) findViewById(R.id.actPiloto);
        updateP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                /*wm_actualizarPiloto = new WebMet_ActualizarPiloto();
                wm_actualizarPiloto.execute();*/
                ActualizarPilotoAsync actualizarPilotoAsync= new ActualizarPilotoAsync();
                actualizarPilotoAsync.execute();
            }
        });

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
                previewFoto.setImageBitmap(bitmap);
            }
        }
    }

    private void requestPermission(Activity context, String permission, String explanation, int requestId) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?   
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                Toast.makeText(context, explanation, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
        } else {
            switch (requestId) {
                case STORAGE_PERMISSION: {
                    Intent pickImage = new Intent(Intent.ACTION_PICK);
                    pickImage.setType("image/*");
                    startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
                    break;

                }
                case CAMERA_PERMISSION: {
                    takePicture();
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION: {
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST);
                break;

            }
            case CAMERA_PERMISSION: {
                takePicture();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IMAGE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        previewFoto.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case IMAGE_CAPTURE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    previewFoto.setImageBitmap(imageBitmap);
                }
        }
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
        }
    }

    private class ActualizarPilotoAsync extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            Drawable fotoPiloto = previewFoto.getDrawable();

            String urlFoto = null;
            if (fotoPiloto == null) {
                urlFoto = "";
            } else {
                try {

                    urlFoto = FileUpload.saveImageIntoMongoDB(fotoPiloto, nombre.getText().toString());
                    consumeRESTVolleyActualizarPiloto(urlFoto, pilotoIntent);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Error_FileUpload: ", e.getMessage());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success == false) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG,"Foto subida");
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void consumeRESTVolleyActualizarPiloto (String urlFoto, Piloto piloto) throws ParseException {

        Log.d(TAG,"Consume");
        piloto.setNombreCompleto(nombre.getText().toString());
        if(fechaNacimiento_piloto != null){
            piloto.setFecha_Nacimiento(fechaNacimiento_piloto);
        }
        piloto.setLugarNacimiento(lugar.getText().toString());
        piloto.setCant_podiosTotales(Integer.parseInt(podios.getText().toString()));
        piloto.setCant_puntosTotales(Integer.parseInt(puntos.getText().toString()));
        piloto.setCant_granPremiosIngresado(Integer.parseInt(gp.getText().toString()));
        piloto.setFoto_ref(urlFoto);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String result = gson.toJson(piloto);
        Log.d(TAG,result);

        JSONObject js = null;
        try {
            js = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"JSONException",e);

            Toast.makeText(this,"Error creando el objeto JSON",Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        //  Formo la petición: método a utilizar, path del servicio, el JSON creado, y un
        //  listener que está pendiente de la respuesta a la petición
         */
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest sr = new JsonObjectRequest(
                Request.Method.PUT,
                "http://10.0.2.2:8080/myapp/PistonApp/pilotos/"+piloto.getId_str(),
                js,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "" + response.toString());

                        Toast.makeText(getApplicationContext(), 	"Piloto Actualizado", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ActualizarPilotoView.this, BuscarPilotosView.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error.ResponseREST " + error.networkResponse.statusCode);
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Log.d(TAG, "Error.ResponseREST A: " + obj.toString());
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                        Log.d(TAG, "Error.ResponseREST B: " + e1.toString());
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                        Log.d(TAG, "Error.ResponseREST C: " + e2.toString());
                    }
                }
            }
        }){

            /*
            //  Esto (getParams) no lo estoy usando como tal pero entiendo que sirve para mapear los
            //  parametros según el tipo de dato.
            */

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                return params;
            }

            /*
            //  Esto (getHeaders) da condiciones a la solicitud con el encabezado de http.
            //  Como el servidor consume JSON, especifico que ese es el tipo de contenido que
            //  voy a utilizar. Y utf-8 porque... eso decía internet jaja.. Supongo que es lo
            //  mas estándar y hace que cosas mas de ASCII no molesten
            */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode= response.statusCode;
                Log.d(TAG,"STATUS CODE: "+mStatusCode);
                return super.parseNetworkResponse(response.);
            }*/
        };

        /*
        //  Agrego lo que armé para hacer la petición con Volley
        */
        Volley.newRequestQueue(this).add(sr);
    }


}
