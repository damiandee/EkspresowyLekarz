package praca_inzynierska.damian_deska.pai_gym.Controller;

import java.util.ArrayList;

/**
 * Created by Damian Deska on 2017-01-09.
 */

public class UserSession {
    private static UserSession userSession = null;
    private boolean isLoggedIn = false;
    private String userID;
    private String choosenDate;
    private boolean isAvailable = false;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
    public void signIn(String userID) {
        this.isLoggedIn = true;
        this.userID = userID;
    }

    /*wylogowanie uzytkownika*/
    public void logout() {
        this.isLoggedIn = false;
        this.userID = null;
    }

    /*gettery i settery klasy*/
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
