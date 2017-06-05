package praca_inzynierska.damian_deska.pai_gym.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import praca_inzynierska.damian_deska.pai_gym.Controller.ServerController;
import praca_inzynierska.damian_deska.pai_gym.Controller.UserSession;
import praca_inzynierska.damian_deska.pai_gym.Model.UserModel;
import praca_inzynierska.damian_deska.pai_gym.R;

public class ProfileActivity extends AppCompatActivity {

    EditText profileName;
    EditText profileSurname;
    EditText profileEmail;
    Button profileButton;
    Button changePasswordButton;
    Toolbar profilePageToolbar;
    Toolbar changePasswordToolbar;
    TextView changePasswordLink;
    EditText oldPassword;
    EditText newPassword;
    EditText newPasswordRepeat;


    UserModel user = new UserModel();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        initProfileToolbar();


        /*inicjalizacja obiektow widoku i uzupelnienie ich danymi pacjenta*/
        profileName = (EditText)findViewById(R.id.profileUserName);
        profileName.setBackgroundResource(R.drawable.rounded_border);

        profileSurname = (EditText)findViewById(R.id.profileUserSurname);
        profileSurname.setBackgroundResource(R.drawable.rounded_border);

        profileEmail = (EditText)findViewById(R.id.profileEmail);
        profileEmail.setBackgroundResource(R.drawable.rounded_border);

        profileButton = (Button) findViewById(R.id.profileButton);

        getUserInfo();

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*aktualizacja danych pacjenta*/
               changeUserInfo();
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

                /*changePasswordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        *//*sprawdzenie, czy nowe haslo wpisano identycznie w obu polach*//*
                        if(!newPassword.getText().toString().equals(newPasswordRepeat.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Nowe hasła nie są identyczne", Toast.LENGTH_LONG).show();
                            newPassword.setBackgroundResource(R.drawable.rounded_border_red);
                            newPasswordRepeat.setBackgroundResource(R.drawable.rounded_border_red);
                        } else {
                            newPassword.setBackgroundResource(R.drawable.rounded_border);
                            newPasswordRepeat.setBackgroundResource(R.drawable.rounded_border);

                            *//*szyfrowanie starego i nowego hasla*//*
                            String oldHashedPassword = md5Hasher.hashToMD5(oldPassword.getText().toString());
                            String newHashedPassword = md5Hasher.hashToMD5(newPassword.getText().toString());

                            *//*sprawdzenie, czy wpisano poprawnie stare haslo*//*
                            if(oldHashedPassword.equals(currentPatient.getPatientPassword())){
                                *//*jezeli tak, zmiana hasla i odswiezenie aktywnosci*//*
                                databaseConnectionController.changePassword(newHashedPassword);

                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                Toast.makeText(getApplicationContext(), "Pomyślnie zmieniono hasło", Toast.LENGTH_LONG).show();
                            } else {
                                *//*jezeli nie, wyswietlany jest komunikat*//*
                                Toast.makeText(getApplicationContext(), "Wprowadzono niepoprawne stare hasło", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });*/
            }
        });
    }

    public void getUserInfo() {
        ServerController.get("user/" + UserSession.getSession().getUserID(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                //JSONArray ja = new JSONArray();
                try {

                    JSONObject userInfo = new JSONObject(response.getString("result"));

                    profileName.setText(userInfo.getString("firstName"));
                    profileSurname.setText(userInfo.getString("lastName"));
                    profileEmail.setText(userInfo.getString("email"));

                    user.set__v(0);
                    user.setEmail(userInfo.getString("email"));
                    user.setFirstName(userInfo.getString("firstName"));
                    user.setLastName(userInfo.getString("lastName"));
                    user.setIfLoggedIn(userInfo.getBoolean("ifLoggedIn"));
                    user.setJoinDate(userInfo.getString("joinDate"));
                    user.setPassword(userInfo.getString("password"));
                    user.setRole(userInfo.getString("role"));
                    user.setUserID(userInfo.getString("_id"));


                } catch (Exception e) {

                }
            }


        });
    }

    public void changeUserInfo() {
        RequestParams params = new RequestParams();

        params.put("_id", user.getUserID());
        params.put("role", user.getRole());
        params.put("joinDate", user.getJoinDate());
        params.put("password", user.getPassword());
        params.put("ifLoggedIn", true);
        params.put("__v", 0);

        params.put("firstName", profileName.getText());
        params.put("lastName", profileSurname.getText());
        params.put("email", profileEmail.getText());

        ServerController.put("user/" + UserSession.getSession().getUserID(), params,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
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
