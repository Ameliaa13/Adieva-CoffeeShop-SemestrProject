package service.impl;

import service.PasswordEncoder;

public class PasswordEncoderImpl implements PasswordEncoder {


    @Override
    public String getHash(String password) {
        return "a" + password.hashCode();
    }

    @Override
    public boolean matches(String password, String hashPassword) {
        return hashPassword.equals(getHash(password));
    }
}
