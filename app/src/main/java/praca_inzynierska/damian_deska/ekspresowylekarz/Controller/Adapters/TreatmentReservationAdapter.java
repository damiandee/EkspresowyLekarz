package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentDateModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

/**
 * Created by Damian Deska on 2017-01-14.
 */

public class TreatmentReservationAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<TreatmentDateModel> data;
    private static LayoutInflater inflater=null;

    public TreatmentReservationAdapter(Activity activity, ArrayList<TreatmentDateModel> d) {
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
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.treatment_reservation_item, null);

            TextView treatmentTime = (TextView)vi.findViewById(R.id.treatmentTime);
            treatmentTime.setText(data.get(position).getTreatmentTime());


            return vi;
        } catch (Exception e) {

        }
        return vi;
    }

}
