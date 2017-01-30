package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.SpecializationModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class MainActivity extends AppCompatActivity {

    DatabaseConnectionController databaseConnectionController;
    ArrayList<SpecializationModel> specializationsList;
    ArrayList<String> specializationsNamesList;
    ArrayAdapter<String> arrayAdapter;

    ListView specializationsListListView;
    EditText searchInput;
    Button loginButton;
    Toolbar mainToolbar;

    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acitivity);

       /* Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        String body = intent.getExtras().getString("body");
        if( title != null) {
            new AlertDialog.Builder(getApplicationContext())
                    .setTitle(title)
                    .setMessage(body)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }*/

        searchInput = (EditText)findViewById(R.id.searchInput);

        loginButton = (Button)findViewById(R.id.loginButton);
        if(UserSession.getSession().isLoggedIn()) {
            loginButton.setVisibility(View.GONE);
        }

        //initProfileToolbar();
        if(UserSession.getSession().isLoggedIn()) {
            initUserSideMenu();
        } else {
            initSideMenu();
        }

        databaseConnectionController = DatabaseConnectionController.getInstance();

        specializationsListListView =(ListView)findViewById(R.id.specializationList);
        fillSpecializationsList();
        initSpecializationsSearchInput();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void sendNotification(View view) {

//Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.stub)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");


// Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                mNotificationManager.notify(001, mBuilder.build());
    }
    
    void initUserSideMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.advanced_search).withIcon(R.drawable.search_icon).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.your_profile).withIcon(R.drawable.profile_icon).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.your_visits).withIcon(R.drawable.calendar_icon).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.logout).withIcon(R.drawable.logout_icon).withIdentifier(4).withSelectable(false)
                        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch((int)drawerItem.getIdentifier()) {

                            case 1:
                                Intent advancedSearchIntent = new Intent(MainActivity.this, AdvancedSearchInitializationActivity.class);
                                startActivity(advancedSearchIntent);
                                finish();
                                break;

                            case 2:
                                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                finish();
                                break;

                            case 3:
                                Intent visitsIntent = new Intent(MainActivity.this, PatientVisitsActivity.class);
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

    void initSideMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.advanced_search).withIcon(R.drawable.search_icon).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.signin).withIcon(R.drawable.signin_icon).withIdentifier(2).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch((int)drawerItem.getIdentifier()) {

                            case 1:
                                Intent advancedSearchIntent = new Intent(MainActivity.this, AdvancedSearchInitializationActivity.class);
                                startActivity(advancedSearchIntent);
                                finish();
                                break;

                            case 2:
                                Intent signinIntent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(signinIntent);
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
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void fillSpecializationsList() {
        specializationsList = databaseConnectionController.getAllSpecializationsList();
        specializationsNamesList = new ArrayList<>();

        for(SpecializationModel specializationModel : specializationsList) {
            specializationsNamesList.add(specializationModel.getSpecializationName());
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, specializationsNamesList);
        specializationsListListView.setAdapter(arrayAdapter);

        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);

        specializationsListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DoctorsListActivity.class);
                String searchedSpecializationName = specializationsNamesList.get(position);
                if(searchInput.getText().toString().length() > 0) {
                    for(String specializationName : specializationsNamesList) {
                        if(specializationName.toLowerCase().startsWith(searchInput.getText().toString().toLowerCase())) {
                            searchedSpecializationName = specializationName;
                        }
                    }
                }
                intent.putExtra("selectedSpecializationName", searchedSpecializationName);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initSpecializationsSearchInput() {
        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                MainActivity.this.arrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

}
