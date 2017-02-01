package praca_inzynierska.damian_deska.ekspresowylekarz.Model;

/**
 * Created by Damian Deska on 2017-01-11.
 */

/*klasa reprezentujaca dany zabieg*/
public class TreatmentModel {
    private int treatmentID;
    private int doctorID;
    private String treatmentName;
    private int treatmentTime;
    private int treatmentCost;

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

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public int getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(int treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public int getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(int treatmentCost) {
        this.treatmentCost = treatmentCost;
    }
}
