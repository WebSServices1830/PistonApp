package co.edu.javeriana.sebastianmesa.conexmongo;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import co.edu.javeriana.sebastianmesa.conexmongo.AutoPck.IndexAutoView;
import co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck.IndexEscuderiaView;
import co.edu.javeriana.sebastianmesa.conexmongo.GranPremioPck.IndexGPView;
import co.edu.javeriana.sebastianmesa.conexmongo.Login.LoginActivityView;
import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.IndexPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.UsuarioPck.IndexUsuarioView;

public class AdminMainActivity extends Activity {
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
        startActivity(new Intent(getBaseContext(), IndexPilotoView.class));
    }

    public void accionesEscuderia (View view){
        startActivity(new Intent(getBaseContext(), IndexEscuderiaView.class));
    }

    public void accionesAuto (View view){
        startActivity(new Intent(getBaseContext(), IndexAutoView.class));
    }

    public void accionesGP (View view){
        startActivity(new Intent(getBaseContext(), IndexGPView.class));
    }

    public void accionesUsuario (View view){
        startActivity(new Intent(getBaseContext(), IndexUsuarioView.class));
    }

    public void logout (View view){
        startActivity(new Intent(getBaseContext(), LoginActivityView.class));
    }

}
