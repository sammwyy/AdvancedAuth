package dev._2lstudios.advancedauth.security.ciphers;

import at.favre.lib.crypto.bcrypt.BCrypt;

import dev._2lstudios.advancedauth.security.Cipher;

public class BCryptCipher implements Cipher {
    @Override
    public String hash(final String raw) {
        return BCrypt.withDefaults().hashToString(10, raw.toCharArray());
    }

    @Override
    public boolean compare(final String hash, final String raw) {
        return BCrypt.verifyer().verify(raw.toCharArray(), hash).verified;
    }
}
