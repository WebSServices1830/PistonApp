package co.edu.javeriana.sebastianmesa.conexmongo.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;
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
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.IndexUsuarioView;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class
LoginActivityView extends AppCompatActivity {

    private static final String TAG = "Log_LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView _signupLink, _emailText, _passwordText;
    private Button _loginButton;
    private WebMet_ValidarLogin wm_validarLogin = null;

    private LinearLayout ll;
    private TextView titulo;

    Usuario usuarioLogeado;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    getUsuario(user);
                } else {
                // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        getWindow().setBackgroundDrawableResource(R.drawable.ic_fondo);
        //findViewById(R.id.fondo).setBackground(ContextCompat.getDrawable(this,R.drawable.ic_fondo));

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

        titulo = (TextView) findViewById(R.id.titulo);
//
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Cheetah Kick.otf");
        titulo.setTypeface(type);
        titulo.setTextSize(60);

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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,
                                "signInWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            _emailText.setText("");
                            _passwordText.setText("");
                            onLoginFailed();
                            startActivity(new Intent(LoginActivityView.this, LoginActivityView.class));
                            finish();
                        }
                    }
                });

        /*wm_validarLogin = new WebMet_ValidarLogin();
        wm_validarLogin.execute();*/

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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

    public String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void getUsuario(FirebaseUser user){
        RequestQueue mRequestQueue;
        String url = "http://10.0.2.2:8080/myapp/PistonApp/usuarios/";

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + user.getEmail(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String nombreJSON = response.get("nombreUsuario").toString();
                    String passJSON   = response.get("contra").toString();

                    Boolean adminJSON  = Boolean.parseBoolean(response.get("admin").toString());

                    Log.i(TAG,"Es admin:" + adminJSON.toString() );

                    if(!adminJSON) {
                        startActivity(new Intent(LoginActivityView.this, UserMenuActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(LoginActivityView.this, AdminMainActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG,"Error pero con respuesta",e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }

    private class WebMet_ValidarLogin extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //WebService - Opciones
            final String NAMESPACE = "http://webservice.javeriana.co/";
            final String URL = "http://10.0.2.2:8080/WS/autenticacion?wsdl";
            final String METHOD_NAME = "validarLogin";
            final String SOAP_ACTION = "http://webservice.javeriana.co/validarLogin";

            String user = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            String contra_hash = null;
            try {
                contra_hash = computeHash(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.i(TAG, e.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.i(TAG, e.getMessage());
            }

            if (contra_hash == null) {
                return false;
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("nombreUsuario", user);
            request.addProperty("contrasenia", contra_hash);

            Log.i(TAG, contra_hash);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
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
                    boolean admin = Boolean.parseBoolean(response.getPrimitivePropertyAsString("admin"));
                    double bolsillo = Double.parseDouble(response.getPrimitivePropertyAsString("bolsillo"));

                    ManagerUsuario.usuario = new Usuario();

                    ManagerUsuario.usuario.setNombreUsuario(nombreUsuario);
                    ManagerUsuario.usuario.setContra(contrasenia);
                    ManagerUsuario.usuario.setFechaNacimiento(fechaNacimiento);
                    ManagerUsuario.usuario.setUrlFoto(urlFoto);
                    ManagerUsuario.usuario.setAdmin(admin);
                    ManagerUsuario.usuario.setBolsillo(bolsillo);

                    if (ManagerUsuario.usuario.isAdmin()) {
                        startActivity(new Intent(getBaseContext(), AdminMainActivity.class));
                    } else {
                        startActivity(new Intent(getBaseContext(), UserMenuActivity.class));
                    }

                    return true;
                }


            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
                //Toast.makeText(getApplicationContext(), "No encontrado", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                e.printStackTrace();

            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success == false) {
                startActivity(new Intent(getBaseContext(), LoginActivityView.class));
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            } else {
            }
        }

        @Override
        protected void onCancelled() {
            startActivity(new Intent(getBaseContext(), LoginActivityView.class));
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }


}
