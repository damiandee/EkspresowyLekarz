package praca_inzynierska.damian_deska.ekspresowylekarz.Controller;

import java.util.ArrayList;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities.MapsActivity;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;

/**
 * Created by Damian Deska on 2017-01-09.
 */

public class UserSession {
    private static UserSession userSession = null;
    private boolean isLoggedIn = false;
    private int userID;
    MapsActivity mapsActivity = null;
    ArrayList<DoctorModel> foundedDoctorsList;
    private String choosenDate;
    private boolean isAvailable = false;

    private UserSession() {

    }

    /*zwrocenie obiektu Singleton sesji uzytkownika*/
    public static UserSession getSession() {
        if(userSession == null) {
            userSession = new UserSession();
        }
        return userSession;
    }

    /*zalogowanie uzytkownika, zapisanie jego ID*/
    public void signIn(int userID) {
        this.isLoggedIn = true;
        this.userID = userID;
    }

    /*wylogowanie uzytkownika*/
    public void logout() {
        this.isLoggedIn = false;
        this.userID = 0;
    }

    /*gettery i settery klasy*/
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public MapsActivity getMapsActivity() {
        return mapsActivity;
    }

    public void setMapsActivity(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    public ArrayList<DoctorModel> getFoundedDoctorsList() {
        return foundedDoctorsList;
    }

    public void setFoundedDoctorsList(ArrayList<DoctorModel> foundedDoctorsList) {
        this.foundedDoctorsList = foundedDoctorsList;
    }

    public String getChoosenDate() {
        return choosenDate;
    }

    public void setChoosenDate(String choosenDate) {
        this.choosenDate = choosenDate;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        isAvailable = available;
    }
}
