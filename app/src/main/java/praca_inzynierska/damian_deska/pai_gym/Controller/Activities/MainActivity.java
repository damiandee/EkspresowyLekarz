package praca_inzynierska.damian_deska.pai_gym.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.*;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import praca_inzynierska.damian_deska.pai_gym.Controller.ServerController;
import praca_inzynierska.damian_deska.pai_gym.Controller.UserSession;
import praca_inzynierska.damian_deska.pai_gym.R;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> specializationsNamesList;
    ArrayAdapter<String> arrayAdapter;
    JSONArray clientsList = new JSONArray();

    EditText login;
    EditText password;
    Button loginButton;
    TextView hello;
    TextView appName;

    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acitivity);

        login = (EditText)findViewById(R.id.login);
        password = (EditText)findViewById(R.id.password);

        hello = (TextView)findViewById(R.id.hello);

        appName = (TextView)findViewById(R.id.AppNameText);


        loginButton = (Button)findViewById(R.id.loginButton);

        /*sprawdzenie, czy uzytkownik jest zalogowany, jezeli nie, widoczny jest przycisk loowania*/
        if(UserSession.getSession().isLoggedIn()) {
            showLogged();
        } else {
            hideLogged();
        }

        /*sprawdzenie, czy uzytkownik jest zalogowany i inicjalizacja odpowiedniego menu bocznego na tej podstawie*/
        initUserSideMenu();

        //getAllClients();

        try{
            fillClientsList2();
        } catch (Exception e) {

        }

               loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logIn();
                /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();*/
            }
        });

    }

    public void fillClientsList2() {
        try {
            fillClientsList();
        } catch(Exception e) {

        }
    }

    public void fillClientsList() throws Exception {

        RequestParams jo = new RequestParams();

        jo.put("role", "CLIENT");
        jo.put("firstName", "Andrzej");
        jo.put("lastName", "Duda");
        jo.put("email", "duda");
        jo.put("password", "duda");


        ServerController.post("user", jo,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }

        });

    }

    public void logIn() {
        ServerController.get("user/all", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                JSONArray ja = new JSONArray();
                boolean loginFlag = false;
                int iterator = 0;
                try {
                    clientsList = response.getJSONArray("result");
                    while(!loginFlag && iterator <= clientsList.length()) {
                        if(clientsList.getJSONObject(iterator).getString("email").equals(login.getText().toString())) {
                            if(clientsList.getJSONObject(iterator).getString("password").equals(password.getText().toString())) {
                                //UserSession.getSession().setUserID(clientsList.getJSONObject(iterator).getString("_id"));
                                showLogged();
                                loginFlag = true;
                                UserSession.getSession().signIn(clientsList.getJSONObject(iterator).getString("_id"));
                                UserSession.getSession().setUsername(clientsList.getJSONObject(iterator).getString("firstName"));
                                hello.setText("Hello " + UserSession.getSession().getUsername() + "!\nYou're logged in GymNet web Client");
                            }
                        }
                        iterator++;
                    }
                    iterator = 0;
                    //System.out.println(results.getJSONObject(0).getString("firstName"));
                    System.out.println(response.getString("success"));

                    fillClientsList();

                    ObjectMapper objectMapper = new ObjectMapper();

                    JsonNode root = objectMapper.readTree(response.toString());

                    //String success = root.path("id").asText();
                    //System.out.println("success : " + success);

                    JsonNode resultNode = root.path("_id");
                    if (resultNode.isMissingNode()) {
                        // if "name" node is missing
                    } else {

                    }

                } catch (Exception e) {

                }
            }


        });

    }

    public void showLogged() {
        appName.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        hello.setText("Hello " + UserSession.getSession().getUsername() + "!\nYou're logged in GymNet web Client");
        hello.setVisibility(View.VISIBLE);
    }

    public void hideLogged() {
        appName.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        hello.setVisibility(View.GONE);
    }


    /*funkcja tworzace menu boczne dla uzytkownika zalogowanego*/
    void initUserSideMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                /*dodanie poszczegolnych pozycji w menu bocznym*/
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Twoje rezerwacje").withIcon(R.drawable.search_icon).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("Twoje karnety").withIcon(R.drawable.calendar_icon).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.your_profile).withIcon(R.drawable.profile_icon).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.logout).withIcon(R.drawable.logout_icon).withIdentifier(4).withSelectable(false)
                        )
                /*listener uruchamiajacy akcje po kliknieciu na dany element menu*/
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch((int)drawerItem.getIdentifier()) {

                            case 1:
                                Intent advancedSearchIntent = new Intent(MainActivity.this, ReservationsActivity.class);
                                startActivity(advancedSearchIntent);
                                finish();
                                break;

                            case 2:
                                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                finish();
                                break;

                            case 3:
                                Intent visitsIntent = new Intent(MainActivity.this, CarnetsActivity.class);
                                startActivity(visitsIntent);
                                finish();
                                break;

                            case 4:
                                UserSession.getSession().logout();
                                Intent logoutIntent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(logoutIntent);
                                finish();
                                break;
                        }

                        return false;
                    }
                }).build();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        /*funkcja obslugujaca przycisk wstecz - najpierw zamyka menu boczne, a jezeli juz zostlao to zrobione, zatrzymuje cala aktywnosc*/
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


}
