package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
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

    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acitivity);

        searchInput = (EditText)findViewById(R.id.searchInput);

        loginButton = (Button)findViewById(R.id.loginButton);
        /*sprawdzenie, czy uzytkownik jest zalogowany, jezeli nie, widoczny jest przycisk loowania*/
        if(UserSession.getSession().isLoggedIn()) {
            loginButton.setVisibility(View.GONE);
        }

        /*sprawdzenie, czy uzytkownik jest zalogowany i inicjalizacja odpowiedniego menu bocznego na tej podstawie*/
        if(UserSession.getSession().isLoggedIn()) {
            initUserSideMenu();
        } else {
            initSideMenu();
        }

        databaseConnectionController = DatabaseConnectionController.getInstance();

        /*wyplenienie listy specjalizacji i inicjalizacja listenera na polu wyszukiwarki*/
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
                        new PrimaryDrawerItem().withName(R.string.advanced_search).withIcon(R.drawable.search_icon).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.your_profile).withIcon(R.drawable.profile_icon).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.your_visits).withIcon(R.drawable.calendar_icon).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.logout).withIcon(R.drawable.logout_icon).withIdentifier(4).withSelectable(false)
                        )
                /*listener uruchamiajacy akcje po kliknieciu na dany element menu*/
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

    /*funkcja tworzace menu boczne dla uzytkownika niezalogowanego*/
    void initSideMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

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
        /*funkcja obslugujaca przycisk wstecz - najpierw zamyka menu boczne, a jezeli juz zostlao to zrobione, zatrzymuje cala aktywnosc*/
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    /*wypelnienie listy specjalizacji*/
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

        /*listener na kazdej specjalizacji*/
        specializationsListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DoctorsListActivity.class);
                /*pobranie nazwy wybranej specjalizacji*/
                String searchedSpecializationName = specializationsNamesList.get(position);
                /*jezeli uzyto wyszukiwarki, id w liscie sie przesuwaja, nalezy wiec w calej liscie wyszukac wpisana nazwe specjalizacji*/
                if(searchInput.getText().toString().length() > 0) {
                    for(String specializationName : specializationsNamesList) {
                        if(specializationName.toLowerCase().startsWith(searchInput.getText().toString().toLowerCase())) {
                            searchedSpecializationName = specializationName;
                        }
                    }
                }
                /*dodanie do intencji nazwy wybranej specjalizacji, uruchomenie nowej aktywnosci*/
                intent.putExtra("selectedSpecializationName", searchedSpecializationName);
                startActivity(intent);
                finish();
            }
        });
    }

    /*listener na wyszukiwarke, odpalany w przypadku zmiany wpisanego tekstu*/
    private void initSpecializationsSearchInput() {
        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                MainActivity.this.arrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

}
