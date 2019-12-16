package by.training.kolos.command;

import java.math.BigInteger;
import java.util.Base64;

public final class PasswordSecurity {

    private PasswordSecurity() {
    }

    public static String getHashedPassword(String password) {
        byte[] bytes;
        Base64.Encoder encoder = Base64.getEncoder();
        bytes = encoder.encode(password.getBytes());
        BigInteger bigInteger = new BigInteger(1, bytes);
        return bigInteger.toString(16);
    }
}
