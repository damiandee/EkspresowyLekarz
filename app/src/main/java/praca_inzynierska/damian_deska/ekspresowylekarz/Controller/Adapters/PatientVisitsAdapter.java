package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Adapters;

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

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities.AddOpinionActivity;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities.CancelReservationActivity;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentDateModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.TreatmentModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.VisitModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

/**
 * Created by Damian Deska on 2017-01-14.
 */

public class PatientVisitsAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<VisitModel> data;
    private static LayoutInflater inflater = null;
    VisitModel visit;
    DatabaseConnectionController databaseConnectionController;
    TextView visitNote;


    public PatientVisitsAdapter(Activity activity, ArrayList<VisitModel> d) {
        this.context = activity;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = d;
        databaseConnectionController = DatabaseConnectionController.getInstance();
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
                vi = inflater.inflate(R.layout.patient_visits_item, null);

            visit = data.get(position);
            final TreatmentDateModel treatmentDateModel = databaseConnectionController.getTreatmentDateInfo(visit.getTreatmentDateID());
            TreatmentModel treatment = databaseConnectionController.getTreatmentInfo(treatmentDateModel.getTreatmentID());
            DoctorModel doctor = databaseConnectionController.getDoctorInfo(treatment.getDoctorID());


            TextView visitName = (TextView) vi.findViewById(R.id.visitName);
            visitName.setText(treatment.getTreatmentName());

            TextView visitDate = (TextView) vi.findViewById(R.id.visitDate);
            visitDate.setText(treatmentDateModel.getTreatmentDate());
            TextView visitTime = (TextView) vi.findViewById(R.id.visitTime);
            visitTime.setText("godz. " + treatmentDateModel.getTreatmentTime());

            TextView visitDoctor = (TextView) vi.findViewById(R.id.visitDoctor);
            visitDoctor.setText(doctor.getDoctorName() + " " + doctor.getDoctorSurname());

            TextView visitDoctorAddress = (TextView) vi.findViewById(R.id.visitDoctorAddress);
            visitDoctorAddress.setText(doctor.getDoctorStreet() + ", " + doctor.getDoctorCity());

            TextView visitCost = (TextView) vi.findViewById(R.id.visitCost);
            visitCost.setText("Cena: " + treatment.getTreatmentCost() + "zł");

            visitNote = (TextView) vi.findViewById(R.id.visitNote);

            final Button visitButton = (Button) vi.findViewById(R.id.visitButton);
            visitNote.setVisibility(View.GONE);
            /*ustawienie widoku rezerwacji wedlug jej statusu*/
            /*sprawdzenie, czy rezerwacja nie jest odwolana*/
            if (visit.getIsCancelled() == 0) {
                /*sprawdzenie, czy rezerwacja jest juz zakonczona*/
                if (visit.getIsEnded() == 1) {
                    vi.setBackgroundColor(Color.parseColor("#FFB8B8B8"));
                    if (visit.getVisitNote() != null) {
                    } else {
                        visitNote.setVisibility(View.GONE);
                    }
                    /*sprawdzenie, czy zakonczona rezerwacja zostala juz oceniona*/
                    if (visit.getIsReviewed() != 1) {
                        visitButton.setEnabled(true);
                        visitButton.setText("Dodaj Opinię");
                        visitButton.setBackgroundResource(R.drawable.rounded_button_active);
                    } else {
                        visitButton.setText("Wizyta oceniona");
                        visitButton.setEnabled(false);
                    }

                } else {
                    visitNote.setVisibility(View.GONE);
                    visitButton.setEnabled(true);
                    vi.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    visitButton.setText("Odwołaj wizytę");
                }
            } else {
                vi.setBackgroundColor(Color.parseColor("#FFCE4749"));
                visitButton.setBackgroundResource(R.drawable.rounded_button_inactive);
                visitButton.setText("Odwołana");
                visitButton.setEnabled(false);
            }

            /*listener na przycisk pod kazda rezerwacja, w zaleznosci od jej statusu uruchamia aktywnosc do odwolania lub
            * ocenienia wizyty*/
            visitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    if (data.get(position).getIsEnded() == 0) {
                        intent = new Intent(context.getApplicationContext(), CancelReservationActivity.class);
                        intent.putExtra("treatmentDateID", treatmentDateModel.getTreatmentDateID());
                    } else {
                        intent = new Intent(context.getApplicationContext(), AddOpinionActivity.class);
                        intent.putExtra("treatmentDateID", treatmentDateModel.getTreatmentDateID());
                    }
                    context.startActivity(intent);
                    context.finish();
                }
            });

            return vi;
        } catch (Exception e) {

        }

        return vi;
    }

}
