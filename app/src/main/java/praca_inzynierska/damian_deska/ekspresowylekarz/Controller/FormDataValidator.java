package praca_inzynierska.damian_deska.ekspresowylekarz.Controller;

/**
 * Created by Damian Deska on 2017-01-16.
 */

public class FormDataValidator {

    public boolean isLengthValid(String fieldValue, int expectedLength){
        if(fieldValue.length() < expectedLength) {
            return false;
        }
        return true;
    }

    public boolean isEmailValid(CharSequence target) {
        if (target == null)
            return false;

        boolean flag = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

        return flag;
    }


}
