package dev._2lstudios.advancedauth.cache.engines;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import dev._2lstudios.advancedauth.cache.CacheEngine;

public class MemoryEngine implements CacheEngine {

    private final LoadingCache<String, String> cache;

    public MemoryEngine(final int expiration) {
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(expiration, TimeUnit.SECONDS)
                .expireAfterAccess(expiration, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
                    public String load(final String _key) throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void delete(final String key) {
        this.cache.invalidate(key);
    }

    @Override
    public String get(final String key) {
        try {
            return this.cache.get(key);
        } catch (final ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void set(final String key, final String value) {
        this.cache.put(key, value);
    }

}
