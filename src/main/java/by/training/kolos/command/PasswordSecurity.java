package by.training.kolos.command;

import java.math.BigInteger;
import java.util.Base64;

/**
 * Класс используется для хэширования пароля
 *
 * @author Колос Марина
 */
public final class PasswordSecurity {

    private PasswordSecurity() {
    }

    /**
     * @param password полученный от пользователя пароль
     * @return захешированный пароль в виде строки
     */
    public static String getHashedPassword(String password) {
        byte[] bytes;
        Base64.Encoder encoder = Base64.getEncoder();
        bytes = encoder.encode(password.getBytes());
        BigInteger bigInteger = new BigInteger(1, bytes);
        return bigInteger.toString(16);
    }
}
