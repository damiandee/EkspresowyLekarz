package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Model.OpinionModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

/**
 * Created by Damian Deska on 2017-01-14.
 */

public class OpinionsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OpinionModel> data;
    private static LayoutInflater inflater=null;
    public Button button;

    public OpinionsListAdapter(Context context, ArrayList<OpinionModel> d) {
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.opinions_list_item, null);

            TextView patientName = (TextView)vi.findViewById(R.id.patientName);
            patientName.setText(data.get(position).getPatientName());
            TextView opinionAddTime = (TextView)vi.findViewById(R.id.opinionDate);
            opinionAddTime.setText(String.valueOf(data.get(position).getOpinionAddDate()));
            RatingBar ratingBar = (RatingBar)vi.findViewById(R.id.opinionRatingBar);
            ratingBar.setRating(data.get(position).getRating());
            TextView opinionContent = (TextView)vi.findViewById(R.id.opinionContent);
            opinionContent.setText(data.get(position).getOpinionContent());
            return vi;
        } catch (Exception e) {

        }
        return vi;
    }
}
