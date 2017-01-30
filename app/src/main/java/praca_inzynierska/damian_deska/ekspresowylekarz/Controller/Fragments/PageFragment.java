package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters.OpinionsListAdapter;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters.TreatmentsListAdapter;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.OpinionModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

/**
 * Created by Damian Deska on 2017-01-12.
 */

public class PageFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    int doctorID;
    TextView descValueCollapsed;
    ImageView descArrow;
    DatabaseConnectionController databaseConnectionController;
    DoctorModel doctorModel;
    DoctorModel doctor;
    boolean isExpanded = false;
    public DatePickerFragment datePickerFragment;

    public static PageFragment newInstance(int page, int doctorID) {
        Bundle args = new Bundle();
        args.putInt("doctorID", doctorID);
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        doctorID = getArguments().getInt("doctorID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseConnectionController = DatabaseConnectionController.getInstance();
        View view = inflater.inflate(R.layout.treatments_list_fragment, container, false);
        try {
            ListView listView = (ListView)view.findViewById(R.id.treatmentsListView);
            ArrayList<TreatmentModel> list = new ArrayList<>();
            list = databaseConnectionController.getDoctorTreatmentsList(doctorID);
            TreatmentsListAdapter treatmentListAdapter = new TreatmentsListAdapter(getActivity(), getActivity().getSupportFragmentManager(), list, doctorID);
            listView.setAdapter(treatmentListAdapter);
            //initDatePicker();
        } catch(Exception e) {

        }
        if(mPage==2) {
            view = inflater.inflate(R.layout.doctor_info_fragment, container, false);

            doctor = databaseConnectionController.getDoctorInfo(doctorID);

            final TextView telephoneValue = (TextView)view.findViewById(R.id.telephoneValue);
            telephoneValue.setText(doctor.getDoctorPhoneNumber());
            //telephoneValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone, 0, 0, 0);
            telephoneValue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel: " + telephoneValue.getText().toString()));
                    startActivity(intent);
                }
            });

            TextView addressValue = (TextView)view.findViewById(R.id.addressValue);
            addressValue.setText(doctor.getDoctorStreet() + "\n" + doctor.getDoctorCity());

            TextView pageNumber =(TextView)view.findViewById(R.id.pageName);
            TextView pageValue = (TextView)view.findViewById(R.id.pageValue);
            pageValue.setText(doctor.getDoctorWebPage());

            descValueCollapsed = (TextView)view.findViewById(R.id.descValue);
            descValueCollapsed.setText(doctor.getDoctorDescription().substring(0, 30) + "(...)");
            descValueCollapsed.setOnClickListener(this);

            descArrow = (ImageView)view.findViewById(R.id.descArrow);

            TextView nfzNumber =(TextView)view.findViewById(R.id.nfzName);
            ImageView nfzValue = (ImageView)view.findViewById(R.id.nfzValue);
            if(doctor.getIsNfz() == 1) {
                nfzValue.setImageResource(R.drawable.available);
            } else {
                nfzValue.setImageResource(R.drawable.nope);
            }

            TextView cardNumber =(TextView)view.findViewById(R.id.cardName);
            ImageView cardValue = (ImageView)view.findViewById(R.id.cardValue);
            if(doctor.getIsCardPayment() == 1) {
                cardValue.setImageResource(R.drawable.available);
            } else {
                cardValue.setImageResource(R.drawable.nope);
            }

            TextView parkingNumber =(TextView)view.findViewById(R.id.parkingName);
            ImageView parkingValue = (ImageView)view.findViewById(R.id.parkingValue);
            if(doctor.getIsParking() == 1) {
                parkingValue.setImageResource(R.drawable.available);
            } else {
                parkingValue.setImageResource(R.drawable.nope);
            }

            TextView disabledNumber =(TextView)view.findViewById(R.id.disabledName);
            ImageView disabledValue = (ImageView)view.findViewById(R.id.disabledValue);
            if(doctor.getIsForDisabled() == 1) {
                disabledValue.setImageResource(R.drawable.available);
            } else {
                disabledValue.setImageResource(R.drawable.nope);
            }
        } else if(mPage==3) {
            view = inflater.inflate(R.layout.opinions_list_fragment, container, false);
            ListView opinionsListView = (ListView)view.findViewById(R.id.opinionsListView);
            RatingBar doctorRatingBar = (RatingBar)view.findViewById(R.id.doctorOpinionsRatingBar);
            ArrayList<OpinionModel> opinionsList = databaseConnectionController.getDoctorOpinionsList(doctorID);
            doctorModel = databaseConnectionController.getDoctorInfo(doctorID);
            doctorRatingBar.setRating(doctorModel.getDoctorRating());
            TextView opinionsCounter = (TextView)view.findViewById(R.id.doctorOpinionsCounter);
            int opinionsNumber = opinionsList.size();
            String opinionsWord = "OPINII";
            if(opinionsNumber == 1) {
                opinionsWord = "OPINIA";
            } else if(opinionsNumber >= 2 && opinionsNumber <= 4) {
                opinionsWord = "OPINIE";
            }

            opinionsCounter.setText(opinionsNumber + " " + opinionsWord);
            OpinionsListAdapter opinionsListAdapter = new OpinionsListAdapter(getContext(), opinionsList);
            opinionsListView.setAdapter(opinionsListAdapter);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.descValue:
                if(isExpanded) {
                    descValueCollapsed.setText(doctor.getDoctorDescription().substring(0,30) + "(...)");
                    descArrow.setImageResource(R.drawable.expand);
                } else {
                    descValueCollapsed.setText(doctor.getDoctorDescription());
                    descArrow.setImageResource(R.drawable.collapse);
                }
                isExpanded = !isExpanded;
                break;
        }
    }

}
