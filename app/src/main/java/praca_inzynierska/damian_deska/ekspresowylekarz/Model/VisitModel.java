package praca_inzynierska.damian_deska.ekspresowylekarz.Model;

/**
 * Created by Damian Deska on 2017-01-18.
 */

/*klasa reprezentujaca rezerwacje*/
public class VisitModel {

    private int visitID;
    private int treatmentDateID;
    private int patientID;
    private int isEnded;
    private int isActual;
    private int isReviewed;
    private int isCancelled;
    private String visitNote;
    private String visitDate;
    private String visitTime;

    public int getVisitID() {
        return visitID;
    }

    public void setVisitID(int visitID) {
        this.visitID = visitID;
    }

    public int getTreatmentDateID() {
        return treatmentDateID;
    }

    public void setTreatmentDateID(int treatmentDateID) {
        this.treatmentDateID = treatmentDateID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getIsEnded() {
        return isEnded;
    }

    public void setIsEnded(int isEnded) {
        this.isEnded = isEnded;
    }

    public int getIsActual() {
        return isActual;
    }

    public void setIsActual(int isActual) {
        this.isActual = isActual;
    }

    public int getIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(int isReviewed) {
        this.isReviewed = isReviewed;
    }

    public int getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(int isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(String visitNote) {
        this.visitNote = visitNote;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }
}
