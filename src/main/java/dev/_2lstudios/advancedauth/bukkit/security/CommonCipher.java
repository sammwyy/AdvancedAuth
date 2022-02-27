package dev._2lstudios.advancedauth.bukkit.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonCipher implements Cipher {
    private MessageDigest messageDigest;

    public CommonCipher(final String algorithm) {
        try {
            this.messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String hash(final String raw) {
        final byte[] digest = this.messageDigest.digest(raw.getBytes());
        final BigInteger no = new BigInteger(1, digest);

        String hash = no.toString(16);
        while (hash.length() < 32) {
            hash += "0" + hash;
        }
        return hash;
    }

    @Override
    public boolean compare(final String hash, final String raw) {
        return hash.equals(this.hash(raw));
    }
}
