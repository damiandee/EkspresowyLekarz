package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Fragments.DatePickerFragment;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

/**
 * Created by Damian Deska on 2017-01-14.
 */

public class TreatmentsListAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<TreatmentModel> data;
    private static LayoutInflater inflater=null;
    int doctorID;
    public Button button;
    FragmentManager fragmentManager;

    public TreatmentsListAdapter(Activity activity, FragmentManager fm, ArrayList<TreatmentModel> d, int doctorID) {
        this.context = activity;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = d;
        this.fragmentManager = fm;
        this.doctorID = doctorID;
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
                vi = inflater.inflate(R.layout.treatments_list_item, null);

            final DatabaseConnectionController databaseConnectionController = DatabaseConnectionController.getInstance();

            TextView treatmentName = (TextView)vi.findViewById(R.id.treatmentName);
            treatmentName.setText(data.get(position).getTreatmentName());
            TextView treatmentCost = (TextView)vi.findViewById(R.id.treatmentCost);
            treatmentCost.setText(String.valueOf(data.get(position).getTreatmentCost()) + "zł");
            Button treatmentCalendarButton = (Button) vi.findViewById(R.id.treatmentCalendarButton);
            /*listener przycisku rezerwacji terminu*/
            treatmentCalendarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*sprawdzenie, czy uzytkownik jest zalogowany*/
                    if(UserSession.getSession().isLoggedIn()) {
                        /*sprawdzenie, czy uzytkownik nie jest zalogowany u danego lekarza*/
                        if(!databaseConnectionController.isPatientBlocked(data.get(position).getDoctorID(), UserSession.getSession().getUserID())) {
                            /*wyswietlenie kalendarza rezerwacji*/
                            DatePickerFragment datePickerFragment = new DatePickerFragment();
                            datePickerFragment.setIDS(doctorID, data.get(position).getTreatmentID());
                            datePickerFragment.show(fragmentManager, "DatePicker");
                        } else {
                            /*wyswietlenie komunikatu o zablokowaniu*/
                            new AlertDialog.Builder(context)
                                    .setTitle("Zostałeś zablokowany")
                                    .setMessage("Ten lekarz zablokował Cię i nie możesz się do niego umówić")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    } else {
                        /*wyswietlenie komunikatu o koniecznosci zalogowania sie*/
                        Toast.makeText(context, "Zalgouj sie, aby zobaczyć wolne terminy", Toast.LENGTH_LONG).show();
                    }
                }
            });
            button = treatmentCalendarButton;
            return vi;
        } catch (Exception e) {

        }
        return vi;
    }

}
