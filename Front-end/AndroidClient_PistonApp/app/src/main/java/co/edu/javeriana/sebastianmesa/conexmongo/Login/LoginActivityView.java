package co.edu.javeriana.sebastianmesa.conexmongo.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import co.edu.javeriana.sebastianmesa.conexmongo.AdminMainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.Managers.ManagerUsuario;
import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Usuario;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import co.edu.javeriana.sebastianmesa.conexmongo.UserMenuActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.CrearUsuarioView;

public class LoginActivityView extends AppCompatActivity {

        private static final String TAG = "LoginActivity";
        private static final int REQUEST_SIGNUP = 0;

        private TextView _signupLink, _emailText, _passwordText;
        private Button _loginButton;
        private WebMet_ValidarLogin wm_validarLogin = null;

        private LinearLayout ll;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_view);
            ButterKnife.bind(this);

            findViewById(R.id.fondo).setBackground(ContextCompat.getDrawable(this,R.drawable.gradiente_back2));

            _loginButton = (Button) findViewById(R.id.btn_login);
            _signupLink = (TextView) findViewById(R.id.link_signup);

            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), CrearUsuarioView.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });
        }

        public void login() {
            Log.d(TAG, "Login");

            if (!validate()) {
                onLoginFailed();
                return;
            }
            _loginButton = (Button) findViewById(R.id.btn_login);
            _loginButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivityView.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Autenticando...");
            progressDialog.show();

            _emailText = (EditText) findViewById(R.id.input_email);
            _passwordText = (EditText) findViewById(R.id.input_password);

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            wm_validarLogin = new WebMet_ValidarLogin();
            wm_validarLogin.execute();


            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_SIGNUP) {
                if (resultCode == RESULT_OK) {

                    startActivity(new Intent(getBaseContext(), AdminMainActivity.class));
                    this.finish();
                }
            }
        }

        @Override
        public void onBackPressed() {
            // disable going back to the AdminMainActivity
            moveTaskToBack(true);
        }

        public void onLoginSuccess() {
            _loginButton = (Button) findViewById(R.id.btn_login);
            _loginButton.setEnabled(true);
            finish();
        }

        public void onLoginFailed() {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            _loginButton = (Button) findViewById(R.id.btn_login);
            _loginButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            _emailText = (EditText) findViewById(R.id.input_email);
            _passwordText = (EditText) findViewById(R.id.input_password);

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            if (email.isEmpty()) {
                _emailText.setError("Llene el campo de usuario");
                valid = false;
            } else {
                _emailText.setError(null);
            }

            if (password.isEmpty()) {
                _passwordText.setError("Llene el campo de pass");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            return valid;
        }

    private class WebMet_ValidarLogin extends AsyncTask<Void, Void, Boolean>  {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8080/WS/autenticacion?wsdl";
            final String METHOD_NAME = "validarLogin";
            final String SOAP_ACTION = "http://webservice.javeriana.co/validarLogin";

            String user = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            String contra_hash = null;
            try {
                contra_hash = computeHash(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.i("Error: ",e.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.i("Error: ",e.getMessage());
            }

            if(contra_hash == null){
                return false;
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("nombreUsuario", user);
            request.addProperty("contrasenia", contra_hash);

            Log.i("Login", contra_hash);


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                //campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                //campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();

                if (response != null) {

                    String nombreUsuario = response.getPrimitivePropertyAsString("nombreUsuario");
                    String contrasenia = response.getPrimitivePropertyAsString("contra");
                    Date fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(response.getPrimitivePropertyAsString("fechaNacimiento"));
                    String urlFoto = response.getPrimitivePropertyAsString("urlFoto");
                    boolean admin = Boolean.getBoolean(response.getPrimitivePropertyAsString("admin"));
                    double bolsillo = Double.parseDouble(response.getPrimitivePropertyAsString("bolsillo"));

                    ManagerUsuario.usuario = new Usuario();

                    ManagerUsuario.usuario.setNombreUsuario(nombreUsuario);
                    ManagerUsuario.usuario.setContra(contrasenia);
                    ManagerUsuario.usuario.setFechaNacimiento(fechaNacimiento);
                    ManagerUsuario.usuario.setUrlFoto(urlFoto);
                    ManagerUsuario.usuario.setAdmin(admin);
                    ManagerUsuario.usuario.setBolsillo(bolsillo);

                    if(ManagerUsuario.usuario.isAdmin()){
                        startActivity(new Intent(getBaseContext(), AdminMainActivity.class));
                    }else{
                        startActivity(new Intent(getBaseContext(), UserMenuActivity.class));
                    }

                    return true;
                }


            }
            catch (Exception e)
            {
                Log.i("Error: ",e.getMessage());
                //Toast.makeText(getApplicationContext(), "No encontrado", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                e.printStackTrace();

            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_SHORT).show();
            }
            else{

//                campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
//
//                if (getNombreUsuario() == null){
//                    campoRespuesta.setText("Usuario no encontrado");
//                    Toast.makeText(getApplicationContext(), "Prueba otro nombre", Toast.LENGTH_LONG).show();
//                }else{
//                    campoRespuesta.setText(
//                            "Nombre: "+getNombreUsuario()+/*getFecha()+*/"\n"+
//                                    "Edad: "+getEdad()+"\n"+
//                                    "Descripción: "+getDescripcion()+"\n"+
//                                    "Foto: "+getFoto()+"\n"+
//                                    "Admin: "+isAdmin()+"\n"+
//                                    "Bolsillo: "+getBolsillo()
//                    );
//
//                    Toast.makeText(getApplicationContext(), "Viendo Piloto", Toast.LENGTH_LONG).show();
//
//                }
            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getBaseContext(), LoginActivityView.class));
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
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
