package dev._2lstudios.advancedauth.common.cache.engines;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import dev._2lstudios.advancedauth.common.cache.CacheEngine;

public class MemoryEngine implements CacheEngine {

    private final LoadingCache<String, String> cache;

    public MemoryEngine(final int expiration) {
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(expiration, TimeUnit.SECONDS)
                .expireAfterAccess(expiration, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
                    public String load(final String key) throws Exception {
                        return "";
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
            String result = this.cache.get(key);
            if (result.equals("")) {
                return null;
            } else {
                return result;
            }
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
