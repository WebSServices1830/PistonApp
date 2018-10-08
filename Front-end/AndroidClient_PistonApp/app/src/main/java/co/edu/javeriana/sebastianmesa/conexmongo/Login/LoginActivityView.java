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
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import butterknife.ButterKnife;
import co.edu.javeriana.sebastianmesa.conexmongo.MainActivity;
import co.edu.javeriana.sebastianmesa.conexmongo.R;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.CrearUsuarioView;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.VerUsuarioView;

public class LoginActivityView extends AppCompatActivity {

        private static final String TAG = "LoginActivity";
        private static final int REQUEST_SIGNUP = 0;

        private TextView _signupLink, _emailText, _passwordText;
        private Button _loginButton;
        private WebMet_ConsultarUsuario wm_agregarPiloto = null;

        private String nombreUsuario, contra, descripcion,foto;
        private int edad;
        private boolean admin;
        private long bolsillo;

        private LinearLayout ll;

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public void setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
        }

        public String getContra() {
            return contra;
        }

        public void setContra(String contra) {
            this.contra = contra;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }

        public int getEdad() {
            return edad;
        }

        public void setEdad(int edad) {
            this.edad = edad;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public long getBolsillo() {
            return bolsillo;
        }

        public void setBolsillo(long bolsillo) {
            this.bolsillo = bolsillo;
        }


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

            wm_agregarPiloto = new WebMet_ConsultarUsuario();
            wm_agregarPiloto.execute();


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

                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    this.finish();
                }
            }
        }

        @Override
        public void onBackPressed() {
            // disable going back to the MainActivity
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

    private class WebMet_ConsultarUsuario extends AsyncTask<Void, Void, Boolean>  {



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL="http://10.0.2.2:8081/WS/crud_usuario?wsdl";
            final String METHOD_NAME = "usuario_readByName";
            final String SOAP_ACTION = "http://webservice.javeriana.co/usuario_readByName";

            String user = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("nombreUsuario", user);


            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            try {

                //campoRespuesta = (TextView) findViewById(R.id.respuestaConsulta);
                //campoRespuesta.setText("");

                ht.call(SOAP_ACTION, envelope);
                SoapObject response = (SoapObject)envelope.getResponse();

                if (response != null) {


                    setNombreUsuario(response.getPrimitivePropertyAsString("nombreUsuario"));
                    setContra(response.getPrimitivePropertyAsString("contra"));
                    setEdad( Integer.parseInt(response.getPrimitivePropertyAsString("edad")));
                    setDescripcion(response.getPrimitivePropertyAsString("descripcion"));
                    setFoto(response.getPrimitivePropertyAsString("foto"));
                    setAdmin(Boolean.parseBoolean (response.getPrimitivePropertyAsString("admin")));
                    setBolsillo(Long.parseLong (response.getPrimitivePropertyAsString("bolsillo")));

                    if(getNombreUsuario().equals(user) && getContra().equals(password)){
                        startActivity(new Intent(getBaseContext(), MainActivity.class));

                        Log.i("ACK-ACK", user + "-" + password);
                        Log.i("ACK-ACK", getNombreUsuario() + "-" + getContra());

                    }

                }else{
                    Toast.makeText(getApplicationContext(), "No encontrado", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                }


            }
            catch (Exception e)
            {
                Log.i("Error: ",e.getMessage());
                //Toast.makeText(getApplicationContext(), "No encontrado", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success==false){
                startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
        }
    }

}
