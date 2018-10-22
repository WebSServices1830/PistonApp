package co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class VerPilotoView extends AppCompatActivity {

    ImageView imageView_fotoPiloto;
    TextView textView_nombreCompleto;
    TextView textView_fechaNacimiento;
    TextView textView_lugarNacimiento;
    TextView textView_podiosTotales;
    TextView textView_granPremios;
    TextView textView_calificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_piloto_view);

        imageView_fotoPiloto = findViewById(R.id.imageView_fotoPerfil);
        textView_nombreCompleto = findViewById(R.id.textView_nombreCompleto);
        textView_fechaNacimiento = findViewById(R.id.textView_fechaNacimientoPiloto);
        textView_lugarNacimiento = findViewById(R.id.textView_lugarNacimiento);
        textView_podiosTotales = findViewById(R.id.textView_podiosTotales);
        textView_granPremios = findViewById(R.id.textView_ingresosGranPremios);
        textView_calificacion = findViewById(R.id.textView_calificacion);

        Piloto piloto = (Piloto) getIntent().getSerializableExtra("Piloto");

        textView_nombreCompleto.setText(piloto.getNombreCompleto());

        Date fechaNacimiento = piloto.getFecha_Nacimiento();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(fechaNacimiento);
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        textView_fechaNacimiento.setText(anio+"/"+mes+"/"+dia);

        textView_lugarNacimiento.setText(piloto.getLugarNacimiento());

        textView_podiosTotales.setText(piloto.getCant_podiosTotales());

        textView_granPremios.setText(piloto.getCant_granPremiosIngresado());

        textView_calificacion.setText( Float.toString(piloto.getCalificacion()) );
    }


}
