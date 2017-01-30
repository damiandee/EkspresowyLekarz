package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.FormDataValidator;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.MD5Hasher;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

/**
 * Created by Damian Deska on 2017-01-09.
 */

public class LoginActivity extends AppCompatActivity {

    public EditText userEmail;
    public EditText userPassword;
    public Button loginButton;
    public Toolbar loginPageToolbar;
    public TextView registerLink;
    FormDataValidator formDataValidator;
    MD5Hasher md5Hasher;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.login_activity);

        userEmail = (EditText)findViewById(R.id.loginUserName);
        userEmail.setBackgroundResource(R.drawable.rounded_border);
        userPassword = (EditText)findViewById(R.id.loginUserPassword);
        userPassword.setBackgroundResource(R.drawable.rounded_border);
        loginButton = (Button)findViewById(R.id.signinButton);
        loginButton.setBackgroundResource(R.drawable.rounded_button_inactive);
        registerLink = (TextView)findViewById(R.id.registerLink);
        md5Hasher = new MD5Hasher();
        addEditTextListeners();

        formDataValidator = new FormDataValidator();

        initToolbar();

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLoginFormDataValid(userEmail.getText().toString(), userPassword.getText().toString())){
                    signIn();
                    if(UserSession.getSession().isLoggedIn()) {
                        String d = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        FirebaseMessaging.getInstance().subscribeToTopic(d);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    public void initToolbar() {
        loginPageToolbar = (Toolbar)findViewById(R.id.loginPageToolbar);
        setSupportActionBar(loginPageToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loginPageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void signIn() {
        String username = userEmail.getText().toString();
        String password = md5Hasher.hashToMD5(userPassword.getText().toString());
        if(username.trim().equals("")|| password.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Nieprawidłowe dane logowania", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseConnectionController databaseConnectionController = DatabaseConnectionController.getInstance();
        databaseConnectionController.signIn(getApplicationContext(), username, password);
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

        userEmail.addTextChangedListener(textWatcher);
        userPassword.addTextChangedListener(textWatcher);
    }

    private void updateSignInButtonState() {
        if(userEmail.getText().length() > 2 && userPassword.getText().length() > 2) {
            loginButton.setBackgroundResource(R.drawable.rounded_button_active);
            loginButton.setEnabled(true);
        } else {
            loginButton.setBackgroundResource(R.drawable.rounded_button_inactive);
            loginButton.setEnabled(false);
        }
    }

    private boolean isLoginFormDataValid(String email, String password) {
        boolean isFormCorrect = true;

        if(!formDataValidator.isEmailValid(email)) {
            userEmail.setBackgroundResource(R.drawable.rounded_border_red);
            isFormCorrect = false;
        } else {
            userEmail.setBackgroundResource(R.drawable.rounded_border);
        }

        if(!formDataValidator.isLengthValid(password, 3)) {
            userPassword.setBackgroundResource(R.drawable.rounded_border_red);
            isFormCorrect = false;
        } else {
            userPassword.setBackgroundResource(R.drawable.rounded_border);
        }

        return isFormCorrect;
    }

}
