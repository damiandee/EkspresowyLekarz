package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters.AdvancedSearchAdapter;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Fragments.DatePickerFragment;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

public class AdvancedSearchActivity extends AppCompatActivity {

    EditText searchInput;
    ListView foundedDoctorsListView;
    DatabaseConnectionController databaseConnectionController;
    ArrayList<DoctorModel> foundedDoctorsList;
    TextView sortText;
    Button opinionsSort;
    Button dateSort;
    Button distanceSort;
    Button filterButton;

    boolean isOpinionsClicked;
    boolean isDateClicked;
    boolean isDistanceClicked;

    int day, month, year;

    DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_search_activity);

        databaseConnectionController = DatabaseConnectionController.getInstance();
        datePickerFragment = new DatePickerFragment();

        searchInput = (EditText) findViewById(R.id.advancedSearchInput);
        addSearchInputtListeners();

        UserSession.getSession().setIsAvailable(true);

        foundedDoctorsListView = (ListView) findViewById(R.id.advancedSearchDoctorsList);

        foundedDoctorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdvancedSearchActivity.this, DoctorPageActivity.class);
                intent.putExtra("doctorID", foundedDoctorsList.get(position).getDoctorID());
                startActivity(intent);

            }
        });

        sortText = (TextView) findViewById(R.id.sortText);
        opinionsSort = (Button) findViewById(R.id.opinionsButton);
        dateSort = (Button) findViewById(R.id.dateButton);
        distanceSort = (Button) findViewById(R.id.distanceButton);
        filterButton = (Button) findViewById(R.id.startFilterButton);

        initButtonsListeners();

    }

    /*funkcja inicjalizująca listenery na polu wyszukiwarki; tutaj wyszukiwanie uruchamiane jest przy zmianie tekstu w polu*/
    private void addSearchInputtListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                UserSession.getSession().setFoundedDoctorsList(databaseConnectionController.searchForDoctors(searchInput.getText().toString()));
                foundedDoctorsList = UserSession.getSession().getFoundedDoctorsList();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshDoctorsList();
            }
        };

        searchInput.addTextChangedListener(textWatcher);
    }

    /*odświeżenie listy znalezionych lekarzy*/
    void refreshDoctorsList() {
        if (searchInput.getText().toString().length() > 0) {
            foundedDoctorsList = UserSession.getSession().getFoundedDoctorsList();
            AdvancedSearchAdapter advancedSearchAdapter = new AdvancedSearchAdapter(this, foundedDoctorsList);
            foundedDoctorsListView.setAdapter(advancedSearchAdapter);
            foundedDoctorsListView.setVisibility(View.VISIBLE);
            if (UserSession.getSession().getMapsActivity() != null) {
                UserSession.getSession().getMapsActivity().clearAllMarks();
                UserSession.getSession().getMapsActivity().makeDoctorsMarks(getApplicationContext());
            }
        } else {
            foundedDoctorsListView.setVisibility(View.INVISIBLE);
            foundedDoctorsList = null;
            UserSession.getSession().setFoundedDoctorsList(foundedDoctorsList);
            UserSession.getSession().getMapsActivity().clearAllMarks();
            UserSession.getSession().getMapsActivity().makeDoctorsMarks(getApplicationContext());
        }

    }

    /*funkcja inicjalizujaca listenery na przyciskach wyboru sposobu sortowania; zapewnia rowniez wybor i podswietlenie tylko
    jednego przycisku jednoczesnie*/
    void initButtonsListeners() {
        opinionsSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchInput.getText().length() > 0 && UserSession.getSession().getMapsActivity() != null) {
                    boolean tmpStatus = isOpinionsClicked;
                    setButtonsInactive();
                    isOpinionsClicked = !tmpStatus;
                    isDateClicked = false;
                    isDistanceClicked = false;

                    if (isOpinionsClicked) {
                        opinionsSort.setBackgroundResource(R.drawable.rounded_button_active);
                    } else {
                        opinionsSort.setBackgroundResource(R.drawable.rounded_button_inactive);
                    }
                }
            }
        });

        dateSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchInput.getText().length() > 0 && UserSession.getSession().getMapsActivity() != null) {
                    boolean tmpStatus = isDateClicked;
                    setButtonsInactive();
                    isDateClicked = !tmpStatus;
                    isOpinionsClicked = false;
                    isDistanceClicked = false;

                    if (isDateClicked) {
                        dateSort.setBackgroundResource(R.drawable.rounded_button_active);
                        datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
                    } else {
                        dateSort.setBackgroundResource(R.drawable.rounded_button_inactive);
                    }
                }

            }
        });

        distanceSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchInput.getText().length() > 0 && UserSession.getSession().getMapsActivity() != null) {
                    boolean tmpStatus = isDistanceClicked;
                    isOpinionsClicked = false;
                    isDateClicked = false;
                    setButtonsInactive();
                    isDistanceClicked = !tmpStatus;

                    if (isDistanceClicked) {
                        distanceSort.setBackgroundResource(R.drawable.rounded_button_active);
                    } else {
                        distanceSort.setBackgroundResource(R.drawable.rounded_button_inactive);
                    }
                }
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchInput.getText().length() > 0 && UserSession.getSession().getMapsActivity() != null) {
                    filterDoctorsList();
                }
            }
        });
    }

    void filterDoctorsList() {

        if ((!isOpinionsClicked || !isDateClicked) || !isDistanceClicked) {
            UserSession.getSession().setFoundedDoctorsList(databaseConnectionController.searchForDoctors(searchInput.getText().toString()));
        }

        if (isOpinionsClicked) {
            Collections.sort(UserSession.getSession().getFoundedDoctorsList(), new Comparator<DoctorModel>() {
                @Override
                public int compare(DoctorModel o1, DoctorModel o2) {
                    if (o1.getDoctorRating() > o2.getDoctorRating()) {
                        return -1;
                    }
                    if (o1.getDoctorRating() < o2.getDoctorRating()) {
                        return 1;
                    }
                    return 0;
                }
            });
        } else if (isDateClicked) {
            UserSession.getSession().setFoundedDoctorsList(databaseConnectionController.getDoctorsAvailableInDate(UserSession.getSession().getChoosenDate()));
            foundedDoctorsList = UserSession.getSession().getFoundedDoctorsList();
        } else if (isDistanceClicked) {
            Collections.sort(UserSession.getSession().getFoundedDoctorsList(), new Comparator<DoctorModel>() {
                @Override
                public int compare(DoctorModel o1, DoctorModel o2) {
                    if (o1.getDoctorRating() > o2.getDoctorRating()) {
                        return 1;
                    }
                    if (o1.getDoctorRating() < o2.getDoctorRating()) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        refreshDoctorsList();
    }

    /*wygaszenie wszystkich przyciskow sposobu sortowania*/
    void setButtonsInactive() {
        opinionsSort.setBackgroundResource(R.drawable.rounded_button_inactive);
        dateSort.setBackgroundResource(R.drawable.rounded_button_inactive);
        distanceSort.setBackgroundResource(R.drawable.rounded_button_inactive);
    }
}
