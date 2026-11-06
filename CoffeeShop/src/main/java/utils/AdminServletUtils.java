package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminServletUtils {

    private AdminServletUtils(){}

    public static String requireText(HttpServletRequest req, String parameterName, String fieldTitle){

        String value = req.getParameter(parameterName);

        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("Заполни поле «" + fieldTitle + "».");
        }

        return value.trim();
    }

    public static Long requireLong(HttpServletRequest req, String parameterName, String fieldTitle){

        String value = requireText(req, parameterName, fieldTitle);

        try{
            return Long.parseLong(value);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("В поле «" + fieldTitle + "» должно быть целое число.");
        }
    }

    public static Double requireDouble(HttpServletRequest req, String parameterName, String fieldTitle){

        String value = requireText(req, parameterName, fieldTitle);

        try{
            double number = Double.parseDouble(value);

            if(number <= 0){
                throw new IllegalArgumentException( "Значение поля «" + fieldTitle + "» должно быть больше нуля.");
            }
            return number;
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("В поле «" + fieldTitle + "» должно быть число.");
        }
    }

    public static String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

}
