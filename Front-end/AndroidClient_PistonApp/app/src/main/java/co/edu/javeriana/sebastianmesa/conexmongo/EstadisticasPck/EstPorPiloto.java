package co.edu.javeriana.sebastianmesa.conexmongo.EstadisticasPck;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import co.edu.javeriana.sebastianmesa.conexmongo.R;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class EstPorPiloto extends AppCompatActivity {

    private static final String[][] SPACESHIPS = { { "1", "Sebas", "Ferrari", "20" },
            { "2", "Heikki", "Skyppy", "10" } };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_por_piloto);

//        TableView tableView = (TableView) findViewById(R.id.tableView);
//
//        tableView.setColumnCount(4);
//
//        TableColumnPxWidthModel columnModel = new TableColumnPxWidthModel(4, 350);
//        columnModel.setColumnWidth(1, 500);
//        columnModel.setColumnWidth(2, 600);
//        tableView.setColumnModel(columnModel);

        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, SPACESHIPS));

    }



}
