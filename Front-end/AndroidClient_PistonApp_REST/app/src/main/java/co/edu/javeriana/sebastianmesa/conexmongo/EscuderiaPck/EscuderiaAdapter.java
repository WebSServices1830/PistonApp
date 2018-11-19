package co.edu.javeriana.sebastianmesa.conexmongo.EscuderiaPck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Escuderia;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class EscuderiaAdapter extends ArrayAdapter<Escuderia> {

    TextView textView_nombreEscuderia;

    public EscuderiaAdapter(Context context, List<Escuderia> datos){
        super(context, R.layout.item_escuderia, datos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Escuderia piloto = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_escuderia, parent, false);
        }

        textView_nombreEscuderia = convertView.findViewById(R.id.textView_item_nombreEscuderia);
        textView_nombreEscuderia.setText(piloto.getNombre());

        return convertView;
    }
}
