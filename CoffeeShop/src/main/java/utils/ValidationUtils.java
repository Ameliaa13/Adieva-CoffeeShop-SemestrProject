package utils;

import java.time.LocalDate;
import java.util.regex.Pattern;

public abstract class ValidationUtils<Model> {

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final int MIN_PASSWORD_LENGTH = 3;
    protected ValidationUtils(){}

    public static class ValidationException extends RuntimeException{
        private final String field;

        public ValidationException(String field, String message){
            super(message);
            this.field = field;
        }

        public String getField(){
            return field;
        }
    }

    protected static ValidationException error(String field, String message){
        return new ValidationException(field, message);
    }


    public static <T> T requireNonNull(T value, String field){
        if(value == null){
            throw error(field, "Поле '" + field + "' обязательно");
        }
        return value;
    }

    public static String requireNotBlank(String value, String field){
        if(value == null || value.isBlank()){
            throw error(field, "Поле '" + field + "' не должно быть пустым");
        }
        return value;
    }

    public static String requireMaxLength(String value, int max, String field){
        if(value != null && value.length() >max){
            throw error(field, "Поле '" + field + "' не длиннее " + max + " символов");
        }
        return value;
    }

    public static String requireEmail(String value, String field){
        requireNotBlank(value, field);

        if(!EMAIL.matcher(value).matches()){
            throw error(field, "Некорректный email");
        }
        return value;
    }

    public static String requirePassword(String value, String field){
        requireNotBlank(value, field);

        if(value.length() < MIN_PASSWORD_LENGTH){
            throw error(field, "Пароль должен быть не короче " + MIN_PASSWORD_LENGTH + " символов");
        }
        return value;
    }

    public static <N extends Number> N requirePositive(N value, String field){

        requireNonNull(value, field);

        if(!(value.doubleValue() >  0)){
            throw error(field,  "Поле '" + field + "' должно быть больше нуля");
        }
        return value;
    }

    public static <N extends Number> N requireNonNegative(N value, String field){
        requireNonNull(value, field);

        if(!(value.doubleValue() >= 0 )){
            throw error(field, "Поле '" + field + "' не может быть отрицательным");
        }
        return value;
    }

    public static <N extends Number> N optionalNonNegative(N value, String field){
        return value == null ? null : requireNonNegative(value, field);
    }

    public static Long requireId(Long id, String field){
        return requirePositive(id, field);
    }

    public static LocalDate requirePast(LocalDate date, String field) {
        requireNonNull(date, field);
        if (!date.isBefore(LocalDate.now())) {
            throw error(field, "Дата должна быть в прошлом");
        }
        return date;
    }

    public static void requireDateNotBefore(LocalDate start, LocalDate end, String field){
        requireNonNull(start, field);
        requireNonNull(end, field);

        if(end.isBefore(start)){
            throw error(field,  "Дата окончания не может быть раньше даты начала");
        }
    }


    public abstract void validate(Model model);

}
