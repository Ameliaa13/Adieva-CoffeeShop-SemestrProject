package service.impl;

import service.PasswordEncoder;

public class PasswordEncoderImpl implements PasswordEncoder {

    private PasswordEncoderImpl(){}
    private static class Holder{
        private static final PasswordEncoderImpl INSTANCE = new PasswordEncoderImpl();
    }
    public static PasswordEncoderImpl getInstance(){
        return Holder.INSTANCE;
    }


    @Override
    public String getHash(String password) {
        return "a" + password.hashCode();
    }

    @Override
    public boolean matches(String password, String hashPassword) {
        return hashPassword.equals(getHash(password));
    }
}
