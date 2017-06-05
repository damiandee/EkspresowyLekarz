package praca_inzynierska.damian_deska.pai_gym.Controller.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import praca_inzynierska.damian_deska.pai_gym.Controller.Adapters.ReservationsAdapter;
import praca_inzynierska.damian_deska.pai_gym.Controller.ServerController;
import praca_inzynierska.damian_deska.pai_gym.Controller.UserSession;
import praca_inzynierska.damian_deska.pai_gym.Model.ReservationModel;
import praca_inzynierska.damian_deska.pai_gym.R;

public class ReservationsActivity extends AppCompatActivity {

    ListView userVisitsListView;
    ArrayList<ReservationModel> reservationsList = new ArrayList<>();
    Toolbar patientVisitsToolbar;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations_activity);

        initToolbar();
        userVisitsListView = (ListView)findViewById(R.id.patientVisitsList);

        getUserReservations();



    }

    public void getUserReservations() {

        ServerController.get("reservation/all", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                JSONArray ja = new JSONArray();
                try {
                    //System.out.println(results.getJSONObject(0).getString("firstName"));

                    ja = response.getJSONArray("result");
                    ReservationModel reservation = new ReservationModel();

                    for(int i = 0; i < ja.length(); i++) {
                        if(ja.getJSONObject(i).getString("userId").equals( UserSession.getSession().getUserID())) {
                            reservation.setUserID(ja.getJSONObject(i).getString("userId"));
                            reservation.setEmployeeID(ja.getJSONObject(i).getString("employeeId"));
                            reservation.setReservationName(ja.getJSONObject(i).getString("name"));
                            reservation.setReservationID(ja.getJSONObject(i).getString("_id"));
                            reservation.setReservationDate(ja.getJSONObject(i).getString("date"));
                            reservation.setReservationTime(ja.getJSONObject(i).getString("time"));
                            reservation.setPlace(ja.getJSONObject(i).getString("place"));
                            reservation.setIsCancelled(ja.getJSONObject(i).getBoolean("ifCancelled"));
                            reservationsList.add(reservation);

                            System.out.println(reservationsList.get(0));

                            reservation = null;
                        }
                    }

                    ObjectMapper objectMapper = new ObjectMapper();

                    JsonNode root = objectMapper.readTree(response.toString());



                    JsonNode resultNode = root.path("_id");
                    if (resultNode.isMissingNode()) {
                        // if "name" node is missing
                    } else {

                        if(resultNode.path("userId").equals(UserSession.getSession().getUserID())) {

                        }

                    }

                    ReservationsAdapter reservationsAdapter = new ReservationsAdapter(context, reservationsList);
                    userVisitsListView.setAdapter(reservationsAdapter);

                } catch (Exception e) {

                }
            }

        });
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
