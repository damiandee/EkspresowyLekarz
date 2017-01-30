package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class ReservationConfirmAcitivity extends AppCompatActivity {

    TextView reservationDoctorName;
    TextView reservationConfirmTreatmentName;
    TextView reservationConfirmDate;
    TextView reservationConfirmTime;
    Toolbar reservationConfirmToolbar;
    Button reservationConfirmButton;

    DatabaseConnectionController databaseConnectionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_confirm_acitivity);

        databaseConnectionController = DatabaseConnectionController.getInstance();

        Intent intent = getIntent();
        int treatmentID = intent.getExtras().getInt("treatmentID");
        int doctorID = intent.getExtras().getInt("doctorID");
        final int treatmentDateID = intent.getExtras().getInt("treatmentDateID");
        String treatmentDate = intent.getExtras().getString("treatmentDate");
        String treatmentTime = intent.getExtras().getString("treatmentTime");
        TreatmentModel treatmentModel = databaseConnectionController.getTreatmentInfo(treatmentID);

        DoctorModel doctorModel = databaseConnectionController.getDoctorInfo(doctorID);

        initToolbar();

        reservationDoctorName = (TextView)findViewById(R.id.reservationConfirmDoctorName);
        reservationDoctorName.setText(doctorModel.getDoctorName() + " " + doctorModel.getDoctorSurname() + ", " +
                doctorModel.getDoctorStreet() + ", " + doctorModel.getDoctorCity());

        reservationConfirmTreatmentName = (TextView)findViewById(R.id.reservationConfirmTreatmentName);
        reservationConfirmTreatmentName.setText(treatmentModel.getTreatmentName());

        reservationConfirmDate = (TextView)findViewById(R.id.reservationConfirmDate);
        reservationConfirmDate.setText(treatmentDate + ", godzina: " + treatmentTime);

        reservationConfirmTime = (TextView)findViewById(R.id.reservationConfirmTime);
        reservationConfirmTime.setText("Cena: " + treatmentModel.getTreatmentCost() + "zł");

        reservationConfirmButton = (Button)findViewById(R.id.reservationConfirmButton);

        reservationConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(databaseConnectionController.bookTreatment(treatmentDateID, UserSession.getSession().getUserID())) {
                Toast.makeText(getApplicationContext(), "Wizyty umówiona pomyślnie", Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(getApplicationContext(), PatientVisitsActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void initToolbar() {
        reservationConfirmToolbar = (Toolbar)findViewById(R.id.reservationConfirmToolbar);
        setSupportActionBar(reservationConfirmToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reservationConfirmToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TreatmentReservationActivity.class);
                startActivity(intent);
            }
        });
    }
}
