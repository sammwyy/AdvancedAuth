package dev._2lstudios.advancedauth.cache.engines;

import dev._2lstudios.advancedauth.cache.CacheEngine;
import redis.clients.jedis.Jedis;

public class RedisEngine implements CacheEngine {

    private Jedis client;
    private int expiration;

    public RedisEngine(String host, int port, String password, int expiration) {
        this.client = new Jedis(host, port);
        this.expiration = expiration;

        if (password != null && !password.isEmpty()) {
            this.client.auth(password);
        }

        this.client.ping();
    }

    @Override
    public void delete(String key) {
        this.client.del(key);
    }

    @Override
    public String get(String key) {
        String value = this.client.get(key);
        if (value != null) {
            this.client.expire(key, this.expiration);
        }
        return value;
    }

    @Override
    public void set(String key, String value) {
        this.client.setex(key, this.expiration, value);
    }

}
