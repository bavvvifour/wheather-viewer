package pet.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    public String hash(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean verifyHash(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }
}
