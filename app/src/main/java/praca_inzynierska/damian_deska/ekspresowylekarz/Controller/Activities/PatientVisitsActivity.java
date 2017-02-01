package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters.PatientVisitsAdapter;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.VisitModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class PatientVisitsActivity extends AppCompatActivity {

    ListView patientVisitsListView;
    ArrayList<VisitModel> patientVisitsList;
    Toolbar patientVisitsToolbar;
    DatabaseConnectionController databaseConnectionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_visits_activity);

        databaseConnectionController = DatabaseConnectionController.getInstance();
        initToolbar();
        patientVisitsListView = (ListView)findViewById(R.id.patientVisitsList);

        patientVisitsList = databaseConnectionController.getPatientVisitsList(UserSession.getSession().getUserID());

        PatientVisitsAdapter patientVisitsAdapter = new PatientVisitsAdapter(this, patientVisitsList);
        patientVisitsListView.setAdapter(patientVisitsAdapter);

    }

    public void initToolbar() {
        patientVisitsToolbar = (Toolbar)findViewById(R.id.patientVisitsToolbar);
        setSupportActionBar(patientVisitsToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        patientVisitsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
