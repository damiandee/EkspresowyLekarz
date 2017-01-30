package praca_inzynierska.damian_deska.ekspresowylekarz.Model;

/**
 * Created by Damian Deska on 2017-01-10.
 */

public class DoctorModel{
    private int doctorID;
    private String doctorName;
    private String doctorSurname;
    private int specjalizationID;
    private String doctorAvatarURL;
    private String doctorDescription;
    private String doctorStreet;
    private String doctorCity;
    private String doctorPhoneNumber;
    private String doctorWebPage;
    private int isNfz;
    private int isCardPayment;
    private int isParking;
    private int isForDisabled;

    private float doctorRating;
    private float doctorDistance;

    public String getDoctorDescription() {
        return doctorDescription;
    }

    public void setDoctorDescription(String doctorDescription) {
        this.doctorDescription = doctorDescription;
    }

    public String getDoctorStreet() {
        return doctorStreet;
    }

    public void setDoctorStreet(String doctorStreet) {
        this.doctorStreet = doctorStreet;
    }

    public String getDoctorCity() {
        return doctorCity;
    }

    public void setDoctorCity(String doctorCity) {
        this.doctorCity = doctorCity;
    }

    public String getDoctorPhoneNumber() {
        return doctorPhoneNumber;
    }

    public void setDoctorPhoneNumber(String doctorPhoneNumber) {
        this.doctorPhoneNumber = doctorPhoneNumber;
    }

    public String getDoctorWebPage() {
        return doctorWebPage;
    }

    public void setDoctorWebPage(String doctorWebPage) {
        this.doctorWebPage = doctorWebPage;
    }

    public int getIsNfz() {
        return isNfz;
    }

    public void setIsNfz(int isNfz) {
        this.isNfz = isNfz;
    }

    public int getIsCardPayment() {
        return isCardPayment;
    }

    public void setIsCardPayment(int isCardPayment) {
        this.isCardPayment = isCardPayment;
    }

    public int getIsParking() {
        return isParking;
    }

    public void setIsParking(int isParking) {
        this.isParking = isParking;
    }

    public int getIsForDisabled() {
        return isForDisabled;
    }

    public void setIsForDisabled(int isForDisabled) {
        this.isForDisabled = isForDisabled;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorSurname() {
        return doctorSurname;
    }

    public void setDoctorSurname(String doctorSurname) {
        this.doctorSurname = doctorSurname;
    }

    public int getSpecjalizationID() {
        return specjalizationID;
    }

    public void setSpecjalizationID(int specjalizationID) {
        this.specjalizationID = specjalizationID;
    }

    public String getDoctorAvatarURL() {
        return doctorAvatarURL;
    }

    public void setDoctorAvatarURL(String doctorAvatarURL) {
        this.doctorAvatarURL = doctorAvatarURL;
    }

    public float getDoctorRating() {
        return doctorRating;
    }

    public void setDoctorRating(float doctorRating) {
        this.doctorRating = doctorRating;
    }

    public float getDoctorDistance() {
        return doctorDistance;
    }

    public void setDoctorDistance(float doctorDistance) {
        this.doctorDistance = doctorDistance;
    }
}
