package co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.FileUpload;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.CrearPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class CrearEscuderiaView extends AppCompatActivity {

    private Activity activityContext = this;

    private final static String TAG = "Log_CrearEscuderia";

    private final static int STORAGE_PERMISSION = 1;
    private final static int CAMERA_PERMISSION = 2;

    static final int IMAGE_PICKER_REQUEST = 10;
    static final int IMAGE_CAPTURE_REQUEST = 20;

    private EditText editText_nombre, editText_lugarBase, editText_jefeEquipo, editText_jefeTecnico, editText_chasis, editText_cant_vecesEnPodio,
            editText_cant_TitulosCampeonato;
    private ImageView previewFoto;
    private ImageButton btn_seleccionarImagen, btn_tomarFoto;
    private Button button_agregarEscuderia;

    private boolean fotoCargada= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_escuderia_view);

        editText_nombre = findViewById(R.id.editText_escuderia_nombre);
        editText_lugarBase = findViewById(R.id.editText_escuderia_ubicacion);
        editText_jefeEquipo = findViewById(R.id.editText_escuderia_nombreJefeEquipo);
        editText_jefeTecnico = findViewById(R.id.editText_escuderia_nombreJefeTecnico);
        editText_chasis = findViewById(R.id.editText_escuderia_nombreChasis);
        editText_cant_vecesEnPodio = findViewById(R.id.editText_escuderia_cantPodios);
        editText_cant_TitulosCampeonato = findViewById(R.id.editText_escuderia_cantTitulos);

        previewFoto = findViewById(R.id.imageView_fotoEscuderia);

        btn_seleccionarImagen = findViewById(R.id.imageButton_seleccionarImagen_escuderia);
        btn_tomarFoto = findViewById(R.id.imageButton_tomarFoto_escuderia);

        btn_seleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(activityContext, Manifest.permission.READ_EXTERNAL_STORAGE, "Se necesita acceso al almacenamiento", STORAGE_PERMISSION);
            }
        });

        btn_tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(activityContext, Manifest.permission.CAMERA, "Se necesita acceso a la cámara", CAMERA_PERMISSION);
            }
        });

        button_agregarEscuderia = (Button) findViewById(R.id.agregarEscuderia);
        button_agregarEscuderia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(camposValidos()){
                    CrearEscuderiaAsync crearEscuderiaAsync = new CrearEscuderiaAsync();
                    crearEscuderiaAsync.execute();
                }
            }
        });

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
                        fotoCargada= true;
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
                    fotoCargada= true;
                }
        }
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
        }
    }

    private class CrearEscuderiaAsync extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            Drawable fotoEscuderia = previewFoto.getDrawable();

            String urlFoto = null;
            if(fotoEscuderia == null){
                urlFoto = "";
            }else{
                try {

                    urlFoto = FileUpload.saveImageIntoMongoDB(fotoEscuderia, editText_nombre.getText().toString());
                    consumeRESTVolleyCrearEscuderia(urlFoto);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Error_FileUpload: ",e.getMessage());
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Piloto Creado", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }

    public void consumeRESTVolleyCrearEscuderia(String urlFoto) {

        Escuderia escuderia = new Escuderia(editText_nombre.getText().toString(), editText_lugarBase.getText().toString(),
                editText_jefeTecnico.getText().toString(), editText_jefeEquipo.getText().toString(), editText_chasis.getText().toString(),
                Integer.parseInt(editText_cant_vecesEnPodio.getText().toString()),
                Integer.parseInt(editText_cant_TitulosCampeonato.getText().toString()), urlFoto);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String result = gson.toJson(escuderia);

        JSONObject js_2 = null;
        try {
            js_2 = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"JSONException",e);

            Toast.makeText(this,"Error creando el objeto JSON",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest sr = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8080/myapp/PistonApp/escuderias",
                js_2,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "" + response.toString());

                        Toast.makeText(getApplicationContext(), 	"escuderia creada", Toast.LENGTH_LONG).show();
                        finish();

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
        //queue.add(sr);
    }

    private boolean camposValidos() {

        boolean sonValidos = true;

        if (editText_nombre.getText().toString().isEmpty()) {
            editText_nombre.setError("No puede estar vacío");
            sonValidos = false;
        }

        if (editText_lugarBase.getText().toString().isEmpty()) {
            editText_lugarBase.setError("No puede estar vacío");
            sonValidos = false;
        }

        if (editText_jefeEquipo.toString().isEmpty()) {
            editText_jefeEquipo.setError("No puede estar vacío");
            sonValidos = false;
        }

        if (editText_jefeTecnico.getText().toString().isEmpty()) {
            editText_jefeTecnico.setError("No puede estar vacío");
            sonValidos = false;
        }

        if (editText_chasis.getText().toString().isEmpty()) {
            editText_chasis.setError("No puede estar vacío");
            sonValidos = false;
        }

        if (editText_cant_vecesEnPodio.getText().toString().isEmpty()) {
            editText_cant_vecesEnPodio.setError("No puede estar vacío");
            sonValidos = false;
        }

        if (editText_cant_TitulosCampeonato.getText().toString().isEmpty()) {
            editText_cant_TitulosCampeonato.setError("No puede estar vacío");
            sonValidos = false;
        }

        if(fotoCargada == false){
            Toast.makeText(this,"Por favor cargue una imagen",Toast.LENGTH_LONG);
            sonValidos = false;
        }

        return sonValidos;
    }

}
