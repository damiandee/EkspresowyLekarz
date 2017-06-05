package praca_inzynierska.damian_deska.pai_gym.Model;

/**
 * Created by Damian Deska on 2017-01-14.
 */

/*klasa reprezentujaca pacjenta*/
public class UserModel {
    private String userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private int __v = 0;
    private boolean ifLoggedIn;
    private String joinDate;

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public boolean isIfLoggedIn() {
        return ifLoggedIn;
    }

    public void setIfLoggedIn(boolean ifLoggedIn) {
        this.ifLoggedIn = ifLoggedIn;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getRole() {

        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
