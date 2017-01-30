package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentDateModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.VisitModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class CancelReservationActivity extends AppCompatActivity {

    Toolbar removeReservationToolbar;
    Button removeReservationButton;
    TextView visitName;
    int treatmentDateID;
    DatabaseConnectionController databaseConnectionController;

    TreatmentDateModel treatmentDate;
    TreatmentModel treatment;
    DoctorModel doctor;
    VisitModel visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_reservation_activity);

        Intent intent = getIntent();
        treatmentDateID = intent.getExtras().getInt("treatmentDateID");

        databaseConnectionController = DatabaseConnectionController.getInstance();

        treatmentDate = databaseConnectionController.getTreatmentDateInfo(treatmentDateID);
        treatment = databaseConnectionController.getTreatmentInfo(treatmentDate.getTreatmentID());
        doctor = databaseConnectionController.getDoctorInfo(treatment.getDoctorID());
        visit = databaseConnectionController.getVisitInfo(treatmentDate.getTreatmentDateID());

        initToolbar();

        visitName = (TextView)findViewById(R.id.visitName);
        visitName.setText(treatment.getTreatmentName());

        TextView visitDate = (TextView)findViewById(R.id.visitDate);
        visitDate.setText(treatmentDate.getTreatmentDate());
        TextView visitTime = (TextView)findViewById(R.id.visitTime);
        visitTime.setText("godz. " + treatmentDate.getTreatmentTime());
        TextView visitDoctor = (TextView)findViewById(R.id.visitDoctor);
        visitDoctor.setText(doctor.getDoctorName() + " " + doctor.getDoctorSurname());

        TextView visitDoctorAddress = (TextView)findViewById(R.id.visitDoctorAddress);
        visitDoctorAddress.setText(doctor.getDoctorStreet() + ", " + doctor.getDoctorCity());

        TextView visitCost = (TextView)findViewById(R.id.visitCost);
        visitCost.setText("Cena: " + treatment.getTreatmentCost() + "zł");

        removeReservationButton = (Button)findViewById(R.id.removeReservationButton);
        removeReservationButton.setText("ODWOŁAJ WIZYTĘ");

        removeReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseConnectionController.removeReservation(visit.getVisitID(), treatmentDate.getTreatmentDateID());
                Intent intent = new Intent(CancelReservationActivity.this, PatientVisitsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void initToolbar() {
        removeReservationToolbar = (Toolbar)findViewById(R.id.removeReservationToolbar);
        setSupportActionBar(removeReservationToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        removeReservationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PatientVisitsActivity.class));
            }
        });
    }
}
