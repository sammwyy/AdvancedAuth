package dev._2lstudios.advancedauth.cache;

import dev._2lstudios.advancedauth.cache.engines.MemoryEngine;
import dev._2lstudios.advancedauth.cache.engines.RedisEngine;
import dev._2lstudios.advancedauth.errors.NoSuchCacheEngineException;

public interface CacheEngine {
    public void delete(String key);

    public String get(String key);

    public void set(String key, String value);

    public static CacheEngine getEngine(String driver, int expiration, String host, int port,
            String password) throws NoSuchCacheEngineException {
        String name = driver.replace(" ", "").replace("-", "").toLowerCase();

        switch (name) {
        case "redis":
            return new RedisEngine(host, port, password, expiration);
        case "memory":
            return new MemoryEngine(expiration);
        default:
            throw new NoSuchCacheEngineException(driver);
        }
    }
}
