package co.edu.javeriana.sebastianmesa.conexmongo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import co.edu.javeriana.sebastianmesa.conexmongo.PilotoPck.VerPilotoView;
import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class EstadisticasPilotosFragment extends Fragment {

    private ImageView mImageView;
    private CardView card_pilotos;

    public EstadisticasPilotosFragment() {
        // Required empty public constructor
    }

    public static EstadisticasPilotosFragment newInstance(String param1, String param2) {
        EstadisticasPilotosFragment fragment = new EstadisticasPilotosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static EstadisticasPilotosFragment newInstance() {
        EstadisticasPilotosFragment fragment = new EstadisticasPilotosFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_estadisticas, container, false);
//
//        mImageView = (ImageView) v.findViewById(R.id.pilotoImg);
//        mImageView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Toast.makeText(getContext(), "Cilcked..",Toast.LENGTH_SHORT).show();
//
//            }
//        });

        card_pilotos = v.findViewById(R.id.card_pilotos);
        card_pilotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),VerPilotoView.class);
                startActivity(intent);
            }
        });

        return inflater.inflate(R.layout.fragment_estadisticas, container, false);
    }

}
