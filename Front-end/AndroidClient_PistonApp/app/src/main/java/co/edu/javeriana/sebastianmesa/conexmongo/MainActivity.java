package co.edu.javeriana.sebastianmesa.conexmongo;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.datatype.XMLGregorianCalendar;

public class MainActivity extends Activity {
    private EditText nombre, edad, equipo;
    private Button agregarP, consultaP, accionesPiloto;
    private String resultado="";
    private TextView campo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void accionesPiloto (View view){
        startActivity(new Intent(getBaseContext(), IndexView.class));
    }



}
