package dev._2lstudios.advancedauth.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonCipher implements Cipher {
    private MessageDigest messageDigest;

    public CommonCipher(String algorithm) {
        try {
            this.messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String hash(String raw) {
        byte[] digest = this.messageDigest.digest(raw.getBytes());
        BigInteger no = new BigInteger(1, digest);

        String hash = no.toString(16);
        while (hash.length() < 32) {
            hash += "0" + hash;
        }
        return hash;
    }

    @Override
    public boolean compare(String hash, String raw) {
        return hash.equals(this.hash(raw));
    }
}
