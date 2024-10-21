package no.hvl.dat250.polls.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class SignupUtils {


    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


}
