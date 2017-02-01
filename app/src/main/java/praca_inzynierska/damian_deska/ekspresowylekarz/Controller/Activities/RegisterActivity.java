package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.FormDataValidator;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.PatientModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class RegisterActivity extends AppCompatActivity {

    EditText registerName;
    EditText registerSurname;
    EditText registerEmail;
    EditText registerPassword;
    EditText registerPhoneNumber;
    Button registerButton;
    Toolbar registerPageToolbar;
    ShapeDrawable shape;
    DatabaseConnectionController databaseConnectionController;
    FormDataValidator formDataValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        initToolbar();

        registerName = (EditText)findViewById(R.id.registerUserName);
        registerName.setBackgroundResource(R.drawable.rounded_border);

        registerSurname = (EditText)findViewById(R.id.registerUserSurname);
        registerSurname.setBackgroundResource(R.drawable.rounded_border);

        registerEmail = (EditText)findViewById(R.id.registerEmail);
        registerEmail.setBackgroundResource(R.drawable.rounded_border);

        registerPassword = (EditText)findViewById(R.id.registerPassword);
        registerPassword.setBackgroundResource(R.drawable.rounded_border);

        registerPhoneNumber = (EditText)findViewById(R.id.registerPhoneNumber);
        registerPhoneNumber.setBackgroundResource(R.drawable.rounded_border);

        registerButton = (Button) findViewById(R.id.registerButton);

        addEditTextListeners();

        databaseConnectionController = DatabaseConnectionController.getInstance();

        formDataValidator = new FormDataValidator();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientModel tmpPatientModel = new PatientModel();
                tmpPatientModel.setPatientName(registerName.getText().toString());
                tmpPatientModel.setPatientSurname(registerSurname.getText().toString());
                tmpPatientModel.setPatientEmail(registerEmail.getText().toString());
                tmpPatientModel.setPatientPassword(registerPassword.getText().toString());
                tmpPatientModel.setPatientPhoneNumber(registerPhoneNumber.getText().toString());

                /*sprawdzenie, czy wprowadzono poprawne dane*/
                if(isRegisterFormDataValid(tmpPatientModel)) {
                    /*rejestracja uzytkownika*/
                    if(databaseConnectionController.registerUser(getApplicationContext(), tmpPatientModel)) {
                        Toast.makeText(getApplicationContext(), "Rejestracja przebiegła pomyślnie, prosimy się zalogować",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Popraw zaznaczone pola", Toast.LENGTH_LONG).show();
                }

                tmpPatientModel = null;
            }
        });
    }

    public void initToolbar() {
        registerPageToolbar = (Toolbar)findViewById(R.id.registerPageToolbar);
        setSupportActionBar(registerPageToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        registerPageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void addEditTextListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {updateSignInButtonState();}

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        /*dodanie listenerow do wsztstkich pol formularza*/
        registerName.addTextChangedListener(textWatcher);
        registerSurname.addTextChangedListener(textWatcher);
        registerEmail.addTextChangedListener(textWatcher);
        registerPassword.addTextChangedListener(textWatcher);
        registerPhoneNumber.addTextChangedListener(textWatcher);
    }

    private void updateSignInButtonState() {
        if(isFormFilled()) {
            registerButton.setBackgroundResource(R.drawable.rounded_button_active);
            registerButton.setEnabled(true);
        } else {
            registerButton.setBackgroundResource(R.drawable.rounded_button_inactive);
            registerButton.setEnabled(false);
        }
    }

    /*funkcja sprawdzajaca, czy wszystkie pola formularza wypelniona poprawnie (min. dlugosc 3 znaki)*/
    private boolean isFormFilled(){
        return registerName.getText().length() > 2 && registerSurname.getText().length() > 2 && registerEmail.getText().length() > 2
                && registerPassword.getText().length() > 2 && registerPhoneNumber.getText().length() > 2;
    }

    /*walidacja wszystkich wprowadzonych danych*/
    public boolean isRegisterFormDataValid(PatientModel patientModel) {
        boolean isFormCorrect = true;

        if(!formDataValidator.isLengthValid(patientModel.getPatientName(), 3)) {
            registerName.setBackgroundResource(R.drawable.rounded_border_red);
            isFormCorrect = false;
        } else {
            registerName.setBackgroundResource(R.drawable.rounded_border);
        }

        if(!formDataValidator.isLengthValid(patientModel.getPatientSurname(), 3)) {
            registerSurname.setBackgroundResource(R.drawable.rounded_border_red);
            isFormCorrect = false;
        } else {
            registerSurname.setBackgroundResource(R.drawable.rounded_border);
        }

        if(!formDataValidator.isEmailValid(patientModel.getPatientEmail())) {
            registerEmail.setBackgroundResource(R.drawable.rounded_border_red);
            isFormCorrect = false;
        } else {
            registerEmail.setBackgroundResource(R.drawable.rounded_border);
        }

        if(!formDataValidator.isLengthValid(patientModel.getPatientPassword(), 5)) {
            registerPassword.setBackgroundResource(R.drawable.rounded_border_red);
            isFormCorrect = false;
        } else {
            registerPassword.setBackgroundResource(R.drawable.rounded_border);
        }

        if(!formDataValidator.isLengthValid(patientModel.getPatientPhoneNumber(), 8)) {
            registerPhoneNumber.setBackgroundResource(R.drawable.rounded_border_red);
            isFormCorrect = false;
        } else {
            registerPhoneNumber.setBackgroundResource(R.drawable.rounded_border);
        }

        return isFormCorrect;
    }
}
