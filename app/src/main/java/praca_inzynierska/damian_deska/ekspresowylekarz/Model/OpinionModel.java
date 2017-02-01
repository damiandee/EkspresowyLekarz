package praca_inzynierska.damian_deska.ekspresowylekarz.Model;

import java.sql.Date;

/**
 * Created by Damian Deska on 2017-01-14.
 */

/*klasa reprezentujaca obiekt opinii o lekarzu*/
public class OpinionModel {
    private int opinionID;
    private int doctorID;
    private int patientID;
    private String patientName;
    private float rating;
    private String opinionContent;
    private String opinionAddDate;

    public int getOpinionID() {
        return opinionID;
    }

    public void setOpinionID(int opinionID) {
        this.opinionID = opinionID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getOpinionContent() {
        return opinionContent;
    }

    public void setOpinionContent(String opinionContent) {
        this.opinionContent = opinionContent;
    }

    public String getOpinionAddDate() {
        return opinionAddDate;
    }

    public void setOpinionAddDate(String opinionAddDate) {
        this.opinionAddDate = opinionAddDate;
    }
}
