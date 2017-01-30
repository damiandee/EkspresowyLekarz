package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters.TreatmentReservationAdapter;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentDateModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class TreatmentReservationActivity extends AppCompatActivity {

    ListView treatmentReservationsList;
    ArrayList<TreatmentDateModel> treatmentDateModels;
    DatabaseConnectionController databaseConnectionController;
    int treatmentID;
    String todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treatment_reservation_activity);



        databaseConnectionController = DatabaseConnectionController.getInstance();

        Intent intent = getIntent();
        treatmentID = intent.getExtras().getInt("treatmentID");
        todayDate = intent.getExtras().getString("choosenDate");

        treatmentReservationsList = (ListView)findViewById(R.id.treatmentReservationsList);
        treatmentDateModels = databaseConnectionController.getTodayTreatment(treatmentID, todayDate);

        TreatmentReservationAdapter treatmentReservationAdapter = new TreatmentReservationAdapter(this, treatmentDateModels);

        treatmentReservationsList.setAdapter(treatmentReservationAdapter);

        treatmentReservationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TreatmentReservationActivity.this, ReservationConfirmAcitivity.class);
                i.putExtra("treatmentID", treatmentDateModels.get(position).getTreatmentID());
                i.putExtra("doctorID", treatmentDateModels.get(position).getDoctorID());
                i.putExtra("treatmentDateID", treatmentDateModels.get(position).getTreatmentDateID());
                i.putExtra("treatmentDate", treatmentDateModels.get(position).getTreatmentDate());
                i.putExtra("treatmentTime", treatmentDateModels.get(position).getTreatmentTime());
                startActivity(i);
                finish();
            }
        });
    }
}
