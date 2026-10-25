package service;

public interface PasswordEncoder {

    String getHash(String password);
    boolean matches(String password, String hashPassword);
}
