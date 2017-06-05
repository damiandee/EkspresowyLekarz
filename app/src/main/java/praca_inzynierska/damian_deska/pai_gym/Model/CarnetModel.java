package praca_inzynierska.damian_deska.pai_gym.Model;

/**
 * Created by Damian Deska on 2017-01-18.
 */

/*klasa reprezentujaca rezerwacje*/
public class CarnetModel {

    private String carnetID;
    private String userID;
    private boolean isCancelled;
    private String carnetName;
    private String startDate;
    private String endDate;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getCarnetID() {
        return carnetID;
    }

    public void setCarnetID(String carnetID) {
        this.carnetID = carnetID;
    }


    public boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getCarnetName() {
        return carnetName;
    }

    public void setCarnetName(String carnetName) {
        this.carnetName = carnetName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
