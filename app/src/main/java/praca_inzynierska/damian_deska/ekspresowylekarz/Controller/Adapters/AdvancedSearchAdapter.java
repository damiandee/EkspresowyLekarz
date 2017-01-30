package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

//import praca_inzynierska.damian_deska.ekspresowylekarz.LazyImageAdapter.ImageLoader;

/**
 * Created by Damian Deska on 2017-01-18.
 */

public class AdvancedSearchAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<DoctorModel> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;


    public AdvancedSearchAdapter(Activity a, ArrayList<DoctorModel> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
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
                vi = inflater.inflate(R.layout.doctors_list_item, null);

            ImageView image=(ImageView)vi.findViewById(R.id.doctorAvatar);
            Picasso.with(activity.getApplicationContext()).load(data.get(position).getDoctorAvatarURL()).into(image);
            //imageLoader.DisplayImage(data.get(position).getDoctorAvatarURL(), image);
            TextView docName=(TextView)vi.findViewById(R.id.doctorsListDocName);
            TextView docAddress = (TextView)vi.findViewById(R.id.doctorsListDocAddress);
            RatingBar doctorRatingBar = (RatingBar)vi.findViewById(R.id.doctorRatingBar);
            docName.setText(data.get(position).getDoctorName() + " " + data.get(position).getDoctorSurname());
            docAddress.setText(data.get(position).getDoctorStreet() + ", " + data.get(position).getDoctorCity());
            doctorRatingBar.setRating(data.get(position).getDoctorRating());




            return vi;
        } catch (Exception e) {

        }
        return vi;
    }


}
