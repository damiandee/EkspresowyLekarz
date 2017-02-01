package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.OpinionModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.PatientModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentDateModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.VisitModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class AddOpinionActivity extends AppCompatActivity {

    TreatmentDateModel treatmentDate;
    TreatmentModel treatment;
    DoctorModel doctor;
    VisitModel visit;
    PatientModel patient;
    Toolbar addOpinionToolbar;
    int treatmentDateID;
    EditText opinionContent;
    RatingBar opinionRatingBar;
    Button addOpinionButton;

    DatabaseConnectionController databaseConnectionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_opinion_activity);
        /*wydobycie ID_terminu z intencji*/
        Intent intent = getIntent();
        treatmentDateID = intent.getExtras().getInt("treatmentDateID");

        databaseConnectionController = DatabaseConnectionController.getInstance();
        /*inicjacja toolbara*/
        initToolbar();
        /*inicjacja obiektu widoku*/
        treatmentDate = databaseConnectionController.getTreatmentDateInfo(treatmentDateID);
        treatment = databaseConnectionController.getTreatmentInfo(treatmentDate.getTreatmentID());
        doctor = databaseConnectionController.getDoctorInfo(treatment.getDoctorID());
        visit = databaseConnectionController.getVisitInfo(treatmentDate.getTreatmentDateID());
        patient = databaseConnectionController.getPatientInfo(visit.getPatientID());

        TextView visitName = (TextView)findViewById(R.id.visitName);
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

        opinionContent = (EditText)findViewById(R.id.opinionContent);

        opinionRatingBar = (RatingBar)findViewById(R.id.addOpinionRatingBar);

        addOpinionButton = (Button)findViewById(R.id.addOpinionButton);
        /*ustawienie listenera na przycisk*/
        addOpinionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*stworzenie nowego obiektu opinii i wysłanie go do bazy*/
                OpinionModel opinion = new OpinionModel();
                opinion.setDoctorID(doctor.getDoctorID());
                opinion.setPatientID(patient.getPatientID());
                opinion.setPatientName(patient.getPatientName());
                opinion.setOpinionContent(opinionContent.getText().toString());
                opinion.setRating(opinionRatingBar.getRating());
                opinion.setOpinionAddDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

                databaseConnectionController.addOpinion(opinion, visit.getVisitID());
                opinion = null;
                /*uruchomienie nowej intencji*/
                Intent i = new Intent(getApplicationContext(), PatientVisitsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void initToolbar() {
        addOpinionToolbar = (Toolbar)findViewById(R.id.addOpinionToolbar);
        setSupportActionBar(addOpinionToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addOpinionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PatientVisitsActivity.class);
                startActivity(intent);
            }
        });
    }
}
