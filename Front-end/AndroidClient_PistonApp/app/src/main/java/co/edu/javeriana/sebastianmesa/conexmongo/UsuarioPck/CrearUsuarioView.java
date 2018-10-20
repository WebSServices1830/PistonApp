package co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.Persistencia.FileUpload;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class CrearUsuarioView extends AppCompatActivity {

    private Activity activityContext = this;

    private EditText et_nombreUsuario, et_contra, et_fechaNacimiento;
    private ImageButton btn_calendario, btn_seleccionarImagen, btn_tomarFoto;
    private ImageView previewFoto;
    private CheckBox checkBox_admin;
    private Button btn_agregarUsuario;

    private String resultado="";
    private WebMet_RegistrarUsuario wm_registrarUsuario = null;
    private TextView campo = null;

    private final static int STORAGE_PERMISSION = 1;
    private final static int CAMERA_PERMISSION = 2;

    static final int IMAGE_PICKER_REQUEST = 10;
    static final int IMAGE_CAPTURE_REQUEST = 20;

    //Calendario para obtener fecha & hora
    private final Calendar calendario = Calendar.getInstance();

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Variables para obtener la fecha
    final int mes = calendario.get(Calendar.MONTH);
    final int dia = calendario.get(Calendar.DAY_OF_MONTH);
    final int anio = calendario.get(Calendar.YEAR);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario_view);

        et_nombreUsuario = findViewById(R.id.nomUsuario);
        et_contra = findViewById(R.id.passUsuario);
        et_fechaNacimiento = findViewById(R.id.editText_fechaNacimiento);
        btn_calendario = findViewById(R.id.imageButton_calendario);
        checkBox_admin = findViewById(R.id.adminUsuario);
        btn_seleccionarImagen = findViewById(R.id.imageButton_seleccionarImagen);
        btn_tomarFoto = findViewById(R.id.imageButton_tomarFoto);
        previewFoto = findViewById(R.id.imageView_fotoPerfil);

        btn_agregarUsuario = findViewById(R.id.agregarUsuario);
        btn_agregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(camposValidos()){
                    wm_registrarUsuario = new WebMet_RegistrarUsuario();
                    wm_registrarUsuario.execute();
                }
            }
        });

        btn_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        btn_seleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(activityContext, Manifest.permission.READ_EXTERNAL_STORAGE,"Se necesita acceso al almacenamiento",STORAGE_PERMISSION);
            }
        });

        btn_tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(activityContext, Manifest.permission.CAMERA, "Se necesita acceso a la cámara", CAMERA_PERMISSION);
            }
        });



    }

    private boolean camposValidos(){

        boolean sonValidos = true;

        String nombreUsuario = et_nombreUsuario.getText().toString();
        String contra = et_contra.getText().toString();
        String fechaNacimiento = et_fechaNacimiento.getText().toString();

        if( nombreUsuario.isEmpty()){
            et_nombreUsuario.setError("No puede estar vacío");
            sonValidos = false;
        }

        if( contra.isEmpty() ){
            et_contra.setError("No puede estar vacío");
            sonValidos = false;
        }

        if( fechaNacimiento.isEmpty() ){
            et_fechaNacimiento.setError("No puede estar vacío");
            sonValidos = false;
        }

        return sonValidos;
    }

    private class WebMet_RegistrarUsuario extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/autenticacion?wsdl";
            final String METHOD_NAME = "registrarUsuario";
            final String SOAP_ACTION = "http://webservice.javeriana.co/registrarUsuario";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            Drawable fotoPerfil = previewFoto.getDrawable();

            String urlFoto = null;
            if(fotoPerfil == null){
                urlFoto = "";
            }else{
                try {
                    urlFoto = FileUpload.saveImageIntoMongoDB(fotoPerfil, et_nombreUsuario.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("Error: ",e.getMessage());
                }
            }

            String contrasenia = et_contra.getText().toString();

            String contra_hash = null;
            try {
                contra_hash = computeHash(contrasenia);
                Log.i("Password:", contra_hash);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.i("Error: ",e.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.i("Error: ",e.getMessage());
            }

            GregorianCalendar fechaNacimiento = new GregorianCalendar(calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH));

            if(contra_hash != null && urlFoto != null){
                request.addProperty("nombreUsuario", et_nombreUsuario.getText().toString());
                request.addProperty("contrasenia", contra_hash);
                request.addProperty("fechaNacimiento", fechaNacimiento.getTime());
                request.addProperty("urlFoto", urlFoto);
                request.addProperty("admin", checkBox_admin.isChecked());
            }else{
                return false;
            }

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            //Para que se puedan enviar Date
            new MarshalDate().register(envelope);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {
                ht.call(SOAP_ACTION, envelope);

                Object response = envelope.getResponse();

                if(response != null){
                    boolean usuarioCreado = Boolean.parseBoolean(response.toString());

                    if(usuarioCreado){
                        return true;
                    }
                }
                //resultado=response.toString();
                //Log.i("Resultado: ",resultado);
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
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
            else{

                //campo = (TextView) findViewById(R.id.conexion);

                //campo.setText(resultado);

                Toast.makeText(getApplicationContext(), 	"Usuario Creado", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendario.set(Calendar.YEAR,year);
                calendario.set(Calendar.MONTH,month);
                calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                et_fechaNacimiento.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    private void requestPermission(Activity context, String permission, String explanation, int requestId ){
        if (ContextCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?   
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,permission)) {
                Toast.makeText(context, explanation, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
        }else{
            switch (requestId){
                case STORAGE_PERMISSION : {
                    Intent pickImage = new Intent(Intent.ACTION_PICK);
                    pickImage.setType("image/*");
                    startActivityForResult(pickImage,IMAGE_PICKER_REQUEST);
                    break;

                }
                case CAMERA_PERMISSION : {
                    takePicture();
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case STORAGE_PERMISSION : {
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");
                startActivityForResult(pickImage,IMAGE_PICKER_REQUEST);
                break;

            }
            case CAMERA_PERMISSION : {
                takePicture();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case IMAGE_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    try{
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        previewFoto.setImageBitmap(selectedImage);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case IMAGE_CAPTURE_REQUEST:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap  = (Bitmap) extras.get("data");
                    previewFoto.setImageBitmap(imageBitmap);
                }
        }
    }

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
        }
    }

    public String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++){
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
