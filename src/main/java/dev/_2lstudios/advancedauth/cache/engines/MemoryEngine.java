package dev._2lstudios.advancedauth.cache.engines;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import dev._2lstudios.advancedauth.cache.CacheEngine;

public class MemoryEngine implements CacheEngine {

    private LoadingCache<String, String> cache;

    public MemoryEngine(int expiration) {
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(expiration, TimeUnit.SECONDS)
                .expireAfterAccess(expiration, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
                    public String load(String key) throws Exception {
                        return "";
                    }
                });
    }

    @Override
    public void delete(String key) {
        this.cache.invalidate(key);
    }

    @Override
    public String get(String key) {
        try {
            String result = this.cache.get(key);
            if (result.equals("")) {
                return null;
            } else {
                return result;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void set(String key, String value) {
        this.cache.put(key, value);
    }

}
