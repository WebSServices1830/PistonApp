package co.edu.javeriana.sebastianmesa.conexmongo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import co.edu.javeriana.sebastianmesa.conexmongo.R;

public class EstadisticasPilotosFragment extends Fragment {

    private ImageView mImageView;

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

//        final View v = inflater.inflate(R.layout.fragment_estadisticas, container, false);
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

        return inflater.inflate(R.layout.fragment_estadisticas, container, false);
    }

}
