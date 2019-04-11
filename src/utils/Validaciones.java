package utils;

import javafx.scene.control.TextField;

import static controller.FXMLLoginController.infoBox;

public class Validaciones {

    public static boolean validarLlenado(TextField textField){
        if (textField.getText() == null || textField.getText().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isValidName(String s){
        if(s.equalsIgnoreCase("")){
            return false;
        }
        String regex="[A-Za-z\\s]+";
        return s.matches(regex);//returns true if input and regex matches otherwise false;
    }

    public static boolean isValidNumber(String s){
        if(s.equalsIgnoreCase("")){
            return false;
        }
        String regex="[0-9.]+";
        return s.matches(regex);//returns true if input and regex matches otherwise false;
    }
}
