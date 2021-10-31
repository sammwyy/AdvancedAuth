package dev._2lstudios.advancedauth.errors;

public class NoSuchCipherException extends Exception {
    public NoSuchCipherException(final String cipherName) {
        super("Cipher named " + cipherName + " doesn't exist");
    }
}
