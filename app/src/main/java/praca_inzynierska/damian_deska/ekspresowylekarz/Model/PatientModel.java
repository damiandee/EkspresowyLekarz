package praca_inzynierska.damian_deska.ekspresowylekarz.Model;

/**
 * Created by Damian Deska on 2017-01-14.
 */

/*klasa reprezentujaca pacjenta*/
public class PatientModel {
    private int patientID;
    private String patientName;
    private String patientSurname;
    private String patientEmail;
    private String patientPassword;
    private String patientPhoneNumber;


    public String getPatientPhoneNumber() {

        return patientPhoneNumber;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
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

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

}
