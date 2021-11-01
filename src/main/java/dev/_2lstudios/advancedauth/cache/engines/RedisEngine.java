package dev._2lstudios.advancedauth.cache.engines;

import dev._2lstudios.advancedauth.cache.CacheEngine;
import redis.clients.jedis.Jedis;

public class RedisEngine implements CacheEngine {

    private final Jedis client;
    private final int expiration;

    public RedisEngine(final String host, final int port, final String password, final int expiration) {
        this.client = new Jedis(host, port);
        this.expiration = expiration;

        if (password != null && !password.isEmpty()) {
            this.client.auth(password);
        }
    }

    @Override
    public void delete(final String key) {
        this.client.del(key);
    }

    @Override
    public String get(final String key) {
        final String value = this.client.get(key);
        if (value != null) {
            this.client.expire(key, this.expiration);
        }
        return value;
    }

    @Override
    public void set(final String key, final String value) {
        this.client.setex(key, this.expiration, value);
    }

}
