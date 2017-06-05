package praca_inzynierska.damian_deska.pai_gym.Controller.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import praca_inzynierska.damian_deska.pai_gym.Controller.Adapters.CarnetsAdapter;
import praca_inzynierska.damian_deska.pai_gym.Controller.ServerController;
import praca_inzynierska.damian_deska.pai_gym.Controller.UserSession;
import praca_inzynierska.damian_deska.pai_gym.Model.CarnetModel;
import praca_inzynierska.damian_deska.pai_gym.R;

public class CarnetsActivity extends AppCompatActivity {

    ListView userVisitsListView;
    ArrayList<CarnetModel> carnetsList = new ArrayList<>();
    Toolbar patientVisitsToolbar;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carnets_activity);

        initToolbar();
        userVisitsListView = (ListView)findViewById(R.id.patientVisitsList);

        getUserReservations();



    }

    public void getUserReservations() {

        ServerController.get("carnet/all", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                JSONArray ja = new JSONArray();
                try {
                    //System.out.println(results.getJSONObject(0).getString("firstName"));

                    ja = response.getJSONArray("result");
                    CarnetModel carnet = new CarnetModel();

                    for(int i = 0; i < ja.length(); i++) {
                        if(ja.getJSONObject(i).getString("userId").equals( UserSession.getSession().getUserID())) {
                            carnet.setUserID(ja.getJSONObject(i).getString("userId"));
                            carnet.setCarnetName(ja.getJSONObject(i).getString("name"));
                            carnet.setCarnetID(ja.getJSONObject(i).getString("_id"));
                            carnet.setStartDate(ja.getJSONObject(i).getString("startDate"));
                            carnet.setEndDate(ja.getJSONObject(i).getString("endDate"));
                            carnet.setIsCancelled(ja.getJSONObject(i).getBoolean("ifCancelled"));
                            carnetsList.add(carnet);

                            System.out.println(carnetsList.get(0));

                            carnet = null;
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

                    CarnetsAdapter patientVisitsAdapter = new CarnetsAdapter(context, carnetsList);
                    userVisitsListView.setAdapter(patientVisitsAdapter);

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
