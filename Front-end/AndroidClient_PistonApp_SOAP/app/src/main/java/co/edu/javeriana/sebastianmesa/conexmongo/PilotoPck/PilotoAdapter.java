package co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Piloto;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class PilotoAdapter extends ArrayAdapter<Piloto> {

    TextView textView_nombrePiloto;

    public PilotoAdapter(Context context, List<Piloto> datos){
        super(context, R.layout.item_piloto, datos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Piloto piloto = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_piloto, parent, false);
        }

        textView_nombrePiloto = convertView.findViewById(R.id.textView_nombrePiloto);
        textView_nombrePiloto.setText(piloto.getNombreCompleto());

        return convertView;
    }
}
