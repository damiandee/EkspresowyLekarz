package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.MD5Hasher;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.PatientModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class ProfileActivity extends AppCompatActivity {

    EditText profileName;
    EditText profileSurname;
    EditText profileEmail;
    EditText profilePhoneNumber;
    Button profileButton;
    Button changePasswordButton;
    Toolbar profilePageToolbar;
    Toolbar changePasswordToolbar;
    TextView changePasswordLink;
    EditText oldPassword;
    EditText newPassword;
    EditText newPasswordRepeat;
    DatabaseConnectionController databaseConnectionController = DatabaseConnectionController.getInstance();
    MD5Hasher md5Hasher = new MD5Hasher();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        initProfileToolbar();

        final PatientModel currentPatient = databaseConnectionController.getPatientInfo(UserSession.getSession().getUserID());

        profileName = (EditText)findViewById(R.id.profileUserName);
        profileName.setBackgroundResource(R.drawable.rounded_border);
        profileName.setText(currentPatient.getPatientName());

        profileSurname = (EditText)findViewById(R.id.profileUserSurname);
        profileSurname.setBackgroundResource(R.drawable.rounded_border);
        profileSurname.setText(currentPatient.getPatientSurname());

        profileEmail = (EditText)findViewById(R.id.profileEmail);
        profileEmail.setBackgroundResource(R.drawable.rounded_border);
        profileEmail.setText(currentPatient.getPatientEmail());

        profilePhoneNumber = (EditText)findViewById(R.id.profilePhoneNumber);
        profilePhoneNumber.setBackgroundResource(R.drawable.rounded_border);
        profilePhoneNumber.setText(currentPatient.getPatientPhoneNumber());

        profileButton = (Button) findViewById(R.id.profileButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientModel tmpPatient = new PatientModel();
                tmpPatient.setPatientID(UserSession.getSession().getUserID());
                tmpPatient.setPatientName(profileName.getText().toString());
                tmpPatient.setPatientSurname(profileSurname.getText().toString());
                tmpPatient.setPatientEmail(profileEmail.getText().toString());
                tmpPatient.setPatientPhoneNumber(profilePhoneNumber.getText().toString());
                DatabaseConnectionController.getInstance().updatePatientInfo(tmpPatient);
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
        changePasswordLink = (TextView)findViewById(R.id.changePasswordLink);

        changePasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            setContentView(R.layout.profile_change_password);
                initChangePasswordToolbar();
                oldPassword = (EditText)findViewById(R.id.oldPassword);
                oldPassword.setBackgroundResource(R.drawable.rounded_border);
                newPassword = (EditText)findViewById(R.id.newPassword);
                newPassword.setBackgroundResource(R.drawable.rounded_border);
                newPasswordRepeat = (EditText)findViewById(R.id.newPasswordRepeat);
                newPasswordRepeat.setBackgroundResource(R.drawable.rounded_border);
                changePasswordButton = (Button)findViewById(R.id.newPasswordButton);
                changePasswordButton.setBackgroundResource(R.drawable.rounded_button_inactive);

                changePasswordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!newPassword.getText().toString().equals(newPasswordRepeat.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Nowe hasła nie są identyczne", Toast.LENGTH_LONG).show();
                            newPassword.setBackgroundResource(R.drawable.rounded_border_red);
                            newPasswordRepeat.setBackgroundResource(R.drawable.rounded_border_red);
                        } else {
                            newPassword.setBackgroundResource(R.drawable.rounded_border);
                            newPasswordRepeat.setBackgroundResource(R.drawable.rounded_border);

                            String oldHashedPassword = md5Hasher.hashToMD5(oldPassword.getText().toString());
                            String newHashedPassword = md5Hasher.hashToMD5(newPassword.getText().toString());

                            if(oldHashedPassword.equals(currentPatient.getPatientPassword())){
                                databaseConnectionController.changePassword(newHashedPassword);

                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                Toast.makeText(getApplicationContext(), "Pomyślnie zmieniono hasło", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Wprowadzono niepoprawne stare hasło", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }

    public void initProfileToolbar() {
        profilePageToolbar = (Toolbar)findViewById(R.id.profilePageToolbar);
        setSupportActionBar(profilePageToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profilePageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    public void initChangePasswordToolbar() {
        changePasswordToolbar = (Toolbar)findViewById(R.id.changePasswordToolbar);
        setSupportActionBar(changePasswordToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profilePageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }
}
