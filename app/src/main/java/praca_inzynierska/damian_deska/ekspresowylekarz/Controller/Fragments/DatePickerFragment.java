package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities.TreatmentReservationActivity;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.DatabaseConnectionController;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;

/**
 * Created by Damian Deska on 2017-01-13.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int yy;
    private int mm;
    private int dd;

    int doctorID;
    int treatmentID;

    String currentDate;

    DatabaseConnectionController databaseConnectionController = DatabaseConnectionController.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        yy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, yy, mm, dd);
        dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
        return dpd;
    }

    public void onDateSet(DatePicker view, int _yy, int _mm, int _dd) {
        this.dd = _dd;
        this.mm = _mm + 1;
        this.yy = _yy;
        populateSetDate(yy, mm+1, dd);

        if(!UserSession.getSession().getIsAvailable()) {
            startReservationActivity();
        } else {
            String date;
            if(this.mm < 10) {
                String month = "0" + String.valueOf(this.mm);
                date = this.yy + "-" + month + "-" + this.dd;
            } else {
                date = this.yy + "-" + this.mm + "-" + this.dd;
            }
            UserSession.getSession().setChoosenDate(date);
            UserSession.getSession().setFoundedDoctorsList(databaseConnectionController.getDoctorsAvailableInDate(date));

        }
    }

    public void populateSetDate(int year, int month, int day) {
        //dateButton.setText(month+"/"+day+"/"+year);
    }


    void startReservationActivity() {
        Intent intent = new Intent(getActivity(), TreatmentReservationActivity.class);
        String choosenDate = null;
        if(this.mm < 10) {
            String month = "0" + String.valueOf(this.mm);
            choosenDate = this.yy + "-" + month + "-" + this.dd;
        } else {
            choosenDate = this.yy + "-" + this.mm + "-" + this.dd;
        }
        intent.putExtra("choosenDate", choosenDate);
        intent.putExtra("doctorID", doctorID);
        intent.putExtra("treatmentID", treatmentID);
        startActivity(intent);
    }


    public String foo() {
        return (dd + "/" + mm + "/" + yy);
    }

    public void setIDS(int doctorID, int treatmentID) {
        this.doctorID = doctorID;
        this.treatmentID = treatmentID;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}