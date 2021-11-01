package dev._2lstudios.advancedauth.errors;

public class NoSuchCacheEngineException extends Exception {
    public NoSuchCacheEngineException(final String engine) {
        super("Cache Engine named " + engine + " doesn't exist");
    }
}
