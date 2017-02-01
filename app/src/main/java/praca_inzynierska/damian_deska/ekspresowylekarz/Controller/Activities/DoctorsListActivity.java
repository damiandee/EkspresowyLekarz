package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters.AdvancedSearchAdapter;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.SpecializationModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

/**
 * Created by Damian Deska on 2017-01-10.
 */

public class DoctorsListActivity extends AppCompatActivity {

    String choosenSpecializationName;
    ListView list;
    Toolbar doctorListToolbar;
    ArrayList<DoctorModel> doctorModels;
    DatabaseConnectionController databaseConnectionController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_list_activity);

        initToolbar();

        Intent intent = getIntent();
        choosenSpecializationName = intent.getExtras().getString("selectedSpecializationName");
        databaseConnectionController = DatabaseConnectionController.getInstance();
        SpecializationModel specialization = databaseConnectionController.getSpecializationInfoFromName(choosenSpecializationName);
        TextView specializationName = (TextView)findViewById(R.id.specializationName);
        specializationName.setText(specialization.getSpecializationName());
        list=(ListView)findViewById(R.id.list);
        doctorModels = databaseConnectionController.getDoctorsFromSpecialization(specialization.getSpecializationID());
        AdvancedSearchAdapter adapter = new AdvancedSearchAdapter(this, doctorModels);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDoctor = doctorModels.get(position).getDoctorSurname();
                Intent intent = new Intent(DoctorsListActivity.this, DoctorPageActivity.class);
                intent.putExtra("doctorID", doctorModels.get(position).getDoctorID());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public void initToolbar() {
        doctorListToolbar = (Toolbar)findViewById(R.id.doctorsListToolbar);
        setSupportActionBar(doctorListToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        doctorListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
