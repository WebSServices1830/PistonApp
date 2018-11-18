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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Usuario;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class CrearPilotoView extends Activity {

    private Activity activityContext = this;

    private final static String TAG = "Log_CrearPiloto";

    private final static int STORAGE_PERMISSION = 1;
    private final static int CAMERA_PERMISSION = 2;

    static final int IMAGE_PICKER_REQUEST = 10;
    static final int IMAGE_CAPTURE_REQUEST = 20;

    private EditText editText_nombre, editText_fecha, editText_lugar, editText_podios, editText_puntos, editText_gp;
    private ImageView previewFoto;
    private ImageButton btn_seleccionarImagen, btn_tomarFoto;
    private Button agregarP;

    private Date fechaNacimiento_piloto = null;

    private String resultado="";
    private CrearPilotoView.WebMet_AgregarPiloto wm_agregarPiloto = null;
    private TextView campo = null;

    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_piloto_view);

        editText_nombre = (EditText) findViewById(R.id.editText_nombrePiloto);
        editText_lugar = (EditText) findViewById(R.id.editText_lugarNacimientoPiloto);
        editText_fecha = findViewById(R.id.editText_fechaPiloto);
        editText_podios   = (EditText) findViewById(R.id.editText_podiosTotales);
        editText_puntos = (EditText) findViewById(R.id.editText_puntosTotales);
        editText_gp = (EditText) findViewById(R.id.editText_ingresosGPTotales);

        btn_seleccionarImagen = findViewById(R.id.imageButton_seleccionarImagen_piloto);
        btn_tomarFoto = findViewById(R.id.imageButton_tomarFoto_piloto);

        previewFoto = findViewById(R.id.imageView_fotoPiloto);

        agregarP =(Button) findViewById(R.id.agregarPiloto);

        editText_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CrearPilotoView.this,
                        AlertDialog.THEME_HOLO_DARK,mDateSetListener,year,month,day);
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
                Log.d(TAG, "onDateSet: date:"+dayOfMonth+"/"+month+"/"+year);

                editText_fecha.setText(dayOfMonth+"/"+month+"/"+year);
            }
        };

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

        agregarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                wm_agregarPiloto = new CrearPilotoView.WebMet_AgregarPiloto();
                wm_agregarPiloto.execute();
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

    public void completarRegistro (String fechaPath){
        //CAMBIAR A DATOS REALES-> ..... ......... la fecha .......... la URL  ....................

        Date currentTime = Calendar.getInstance().getTime();
        //GregorianCalendar fechaNacimiento = new GregorianCalendar(calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH));
        et_fechaNacimiento.setText( calendario.get(Calendar.DAY_OF_MONTH) + BARRA + calendario.get(Calendar.MONTH) + BARRA + calendario.get(Calendar.YEAR));
        Usuario user = new Usuario(emailGlobal, passGlobal,et_fechaNacimiento, fechaPath, checkBox_admin.isChecked());

        String piloto_nombre = editText_nombre.getText().toString();
        Date piloto_fechaNacimiento = fechaNacimiento_piloto;
        String piloto_lugarNacimiento = editText_lugar.getText().toString();
        int piloto_podios = Integer.parseInt( editText_podios.getText().toString() );
        int piloto_puntos = Integer.parseInt( editText_puntos.getText().toString() );
        int piloto_participacionesGP = Integer.parseInt( editText_gp.getText().toString() );


        Piloto piloto = new Piloto();

        /*
        //  Como el servidor quiere consumir JSON entonces creo un JSON en base al objeto
        //  que quiero pasar. Siendo este 'user' de tipo Usuario.
         */
        JSONObject js = new JSONObject();
        try {
            js.put("user",user.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        //  Formo la petición: método a utilizar, path del servicio, el JSON creado, y un
        //  listener que está pendiente de la respuesta a la petición
         */
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest sr = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8080/myapp/PistonApp/pilotos",
                js,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("ResponseREST", "" + response);

                        Toast.makeText(getApplicationContext(),"Piloto Creado", Toast.LENGTH_SHORT).show();
                        finish();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.ResponseREST", "" + error.networkResponse.statusCode);
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Log.d("Error.ResponseREST", "A: " + obj.toString());
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                        Log.d("Error.ResponseREST", "B: " + e1.toString());
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                        Log.d("Error.ResponseREST", "C: " + e2.toString());
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
        };

        /*
        //  Agrego lo que armé para hacer la petición con Volley
        */
        Volley.newRequestQueue(this).add(sr);
    }

    private class WebMet_AgregarPiloto extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/infoCampeonato?wsdl";
            final String METHOD_NAME = "agregarPiloto";
            final String SOAP_ACTION = "http://webservice.javeriana.co/agregarPiloto";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            //request.addProperty("Hola", nro1.getText().toString());
            //request.addProperty("nro2", nro2.getText().toString());

            XMLGregorianCalendar fecha= null;


            GregorianCalendar fechaNacimiento = new GregorianCalendar();
            fechaNacimiento.getTime();

            request.addProperty("nombreCompleto", editText_nombre.getText().toString());
            request.addProperty("fecha_Nacimiento", null);
            request.addProperty("lugarNacimiento", editText_lugar.getText().toString());
            request.addProperty("cant_podiosTotales", Integer.parseInt(editText_podios.getText().toString()));
            request.addProperty("cant_puntosTotales", Integer.parseInt(editText_puntos.getText().toString()));
            request.addProperty("cant_granPremiosIngresado", Integer.parseInt(editText_gp.getText().toString()));


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                //resultado=response.toString();
                //Log.i("Resultado: ",resultado);
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

                //campo = (TextView) findViewById(R.id.conexion);

                //campo.setText(resultado);

                Toast.makeText(getApplicationContext(), 	"Piloto Creado", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }
}
