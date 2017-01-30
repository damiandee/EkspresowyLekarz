package praca_inzynierska.damian_deska.ekspresowylekarz.Model;

/**
 * Created by Damian Deska on 2017-01-18.
 */

public class TreatmentDateModel {

    private int treatmentDateID;
    private int treatmentID;
    private int doctorID;
    private String treatmentDate;
    private String treatmentTime;
    private int isAvailable;

    public int getTreatmentDateID() {
        return treatmentDateID;
    }

    public void setTreatmentDateID(int treatmentDateID) {
        this.treatmentDateID = treatmentDateID;
    }

    public int getTreatmentID() {
        return treatmentID;
    }

    public void setTreatmentID(int treatmentID) {
        this.treatmentID = treatmentID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public String getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(String treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public int getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }
}
