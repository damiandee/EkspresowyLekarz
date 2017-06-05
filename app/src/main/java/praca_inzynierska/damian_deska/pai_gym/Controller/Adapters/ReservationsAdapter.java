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
import praca_inzynierska.damian_deska.pai_gym.Controller.Activities.ReservationsActivity;
import praca_inzynierska.damian_deska.pai_gym.Controller.ServerController;
import praca_inzynierska.damian_deska.pai_gym.Model.ReservationModel;
import praca_inzynierska.damian_deska.pai_gym.R;

/**
 * Created by Damian Deska on 2017-01-14.
 */

public class ReservationsAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<ReservationModel> data;
    private static LayoutInflater inflater = null;
    ReservationModel visit;
    TextView visitNote;


    public ReservationsAdapter(Activity activity, ArrayList<ReservationModel> d) {
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
                vi = inflater.inflate(R.layout.reservations_item, null);

            visit = data.get(position);

            TextView visitName = (TextView) vi.findViewById(R.id.visitName);
            visitName.setText(visit.getReservationName());

            TextView visitDate = (TextView) vi.findViewById(R.id.reservationDate);
            visitDate.setText(visit.getReservationDate());
            TextView visitTime = (TextView) vi.findViewById(R.id.reservationTime);
            visitTime.setText("godz. " + visit.getReservationTime());

            TextView visitDoctor = (TextView) vi.findViewById(R.id.reservationPlace);
            visitDoctor.setText(visit.getPlace());


            final Button visitButton = (Button) vi.findViewById(R.id.cancelVisitButton);

            if(!visit.getIsCancelled()) {
                visitButton.setEnabled(true);
                visitButton.setBackgroundColor(Color.parseColor("#949494"));
                visitButton.setText("Odwołaj rezerwację");
            } else {
                visitButton.setEnabled(false);
                visitButton.setBackgroundColor(Color.parseColor("#e12929"));
                visitButton.setText("Rezerwacja odwołana");
            }

            visitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cancelReservation();

                    Intent intent;
                    intent = new Intent(context.getApplicationContext(), ReservationsActivity.class);
                    context.startActivity(intent);
                    context.finish();
                }
            });

            return vi;
        } catch (Exception e) {

        }

        return vi;
    }

    public void cancelReservation() {
        RequestParams params = new RequestParams();

        params.put("_id", visit.getReservationID());
        params.put("employeeID", visit.getEmployeeID());
        params.put("userID", visit.getUserID());
        params.put("name", visit.getReservationName());
        params.put("time", visit.getReservationTime());
        params.put("ifCancelled", true);
        params.put("__v", 0);
        params.put("place", visit.getPlace());
        params.put("date", visit.getReservationDate());

        ServerController.put("reservation/" + visit.getReservationID(), params,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }

        });
    }

}
