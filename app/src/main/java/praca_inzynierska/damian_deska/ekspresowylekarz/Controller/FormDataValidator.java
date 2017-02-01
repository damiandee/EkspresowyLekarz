package praca_inzynierska.damian_deska.ekspresowylekarz.Controller;

/**
 * Created by Damian Deska on 2017-01-16.
 */

public class FormDataValidator {

    /*funkcja odpowiedzialna za sprawdzenie, czy wartosc wpisana w pole ma ustalona lub wieksza dlugosc*/
    public boolean isLengthValid(String fieldValue, int expectedLength){
        if(fieldValue.length() < expectedLength) {
            return false;
        }
        return true;
    }

    /*funkcja odpowiedzialna za sprawdzenie wzoru adresu mailowego*/
    public boolean isEmailValid(CharSequence target) {
        if (target == null)
            return false;

        boolean flag = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

        return flag;
    }


}
