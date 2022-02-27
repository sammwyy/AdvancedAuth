package dev._2lstudios.advancedauth.common.cache;

import dev._2lstudios.advancedauth.common.cache.engines.MemoryEngine;
import dev._2lstudios.advancedauth.common.cache.engines.RedisEngine;
import dev._2lstudios.advancedauth.common.errors.NoSuchCacheEngineException;

public interface CacheEngine {
    public void delete(final String key);

    public String get(final String key);

    public void set(final String key, final String value);

    public static CacheEngine getEngine(final String driver, final int expiration, final String host, final int port,
            final String password) throws NoSuchCacheEngineException {
        final String name = driver.replace(" ", "").replace("-", "").toLowerCase();

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
