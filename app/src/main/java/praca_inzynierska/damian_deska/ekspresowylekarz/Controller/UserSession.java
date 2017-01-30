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

    public static UserSession getSession() {
        if(userSession == null) {
            userSession = new UserSession();
        }
        return userSession;
    }

    public void signIn(int userID) {
        this.isLoggedIn = true;
        this.userID = userID;
    }

    public void logout() {
        this.isLoggedIn = false;
        this.userID = 0;
    }

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
