package dev._2lstudios.advancedauth.bukkit.security;

import dev._2lstudios.advancedauth.bukkit.security.ciphers.BCryptCipher;
import dev._2lstudios.advancedauth.bukkit.security.ciphers.MD5Cipher;
import dev._2lstudios.advancedauth.bukkit.security.ciphers.SHA1Cipher;
import dev._2lstudios.advancedauth.bukkit.security.ciphers.SHA256Cipher;
import dev._2lstudios.advancedauth.bukkit.security.ciphers.SHA512Cipher;
import dev._2lstudios.advancedauth.common.errors.NoSuchCipherException;

public interface Cipher {
    public String hash(final String raw);

    public boolean compare(final String hash, final String raw);

    public static Cipher getCipher(final String input) throws NoSuchCipherException {
        final String name = input.replace(" ", "").replace("-", "").toLowerCase();

        switch (name) {
        case "bcrypt":
            return new BCryptCipher();
        case "md5":
            return new MD5Cipher();
        case "sha1":
            return new SHA1Cipher();
        case "sha256":
            return new SHA256Cipher();
        case "sha512":
            return new SHA512Cipher();
        default:
            throw new NoSuchCipherException(input);
        }
    }
}
