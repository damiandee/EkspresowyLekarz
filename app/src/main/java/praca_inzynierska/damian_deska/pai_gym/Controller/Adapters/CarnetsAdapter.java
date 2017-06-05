package praca_inzynierska.damian_deska.pai_gym.Controller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import praca_inzynierska.damian_deska.pai_gym.Controller.Activities.CarnetsActivity;
import praca_inzynierska.damian_deska.pai_gym.Controller.ServerController;
import praca_inzynierska.damian_deska.pai_gym.Model.CarnetModel;
import praca_inzynierska.damian_deska.pai_gym.R;

/**
 * Created by Damian Deska on 2017-01-14.
 */

public class CarnetsAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<CarnetModel> data;
    private static LayoutInflater inflater = null;
    CarnetModel carnet;
    TextView visitNote;


    public CarnetsAdapter(Activity activity, ArrayList<CarnetModel> d) {
        this.context = activity;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = d;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        try {
            if (convertView == null)
                vi = inflater.inflate(R.layout.carnets_item, null);

            carnet = data.get(position);

            TextView visitName = (TextView) vi.findViewById(R.id.visitName);
            visitName.setText(carnet.getCarnetName());

            TextView visitDate = (TextView) vi.findViewById(R.id.startDate);
            visitDate.setText("Ważny od: " + carnet.getStartDate() +", do " + carnet.getEndDate());;


            final Button visitButton = (Button) vi.findViewById(R.id.cancelVisitButton);

            if(!carnet.getIsCancelled()) {
                visitButton.setEnabled(true);
                visitButton.setBackgroundColor(Color.parseColor("#949494"));
                visitButton.setText("Anuluj karnet");
            } else {
                visitButton.setEnabled(false);
                visitButton.setBackgroundColor(Color.parseColor("#e12929"));
                visitButton.setText("Karnet anulowany");
            }

            visitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cancelCarnet();

                    Intent intent;
                    intent = new Intent(context.getApplicationContext(), CarnetsActivity.class);
                    context.startActivity(intent);
                    context.finish();
                }
            });

            return vi;
        } catch (Exception e) {

        }

        return vi;
    }

    public void cancelCarnet() {
        RequestParams params = new RequestParams();

        params.put("_id", carnet.getCarnetID());
        params.put("userID", carnet.getUserID());
        params.put("name", carnet.getCarnetName());
        params.put("startDate", carnet.getStartDate());
        params.put("ifCancelled", true);
        params.put("__v", 0);
        params.put("endDate", carnet.getEndDate());

        ServerController.put("carnet/" + carnet.getCarnetID(), params,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }

        });
    }

}
