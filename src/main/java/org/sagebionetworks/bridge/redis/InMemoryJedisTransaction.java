package org.sagebionetworks.bridge.redis;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * In-memory mock implementation of a JedisTransaction. Wraps around an InMemoryJedisOps. Doesn't actually honor
 * transactionality.
 */
public class InMemoryJedisTransaction extends JedisTransaction {
    private final InMemoryJedisOps ops;
    private final List<Object> resultList = new ArrayList<>();

    public InMemoryJedisTransaction(InMemoryJedisOps ops) {
        // We need to pass in *something*, otherwise this causes a NullPointerException. However, we don't care what it
        // is, so just pass in a Mockito mock.
        super(mock(Jedis.class));
        this.ops = ops;
    }

    @Override
    public JedisTransaction setex(String key, int seconds, String value) {
        resultList.add(ops.setex(key, seconds, value));
        return this;
    }

    @Override
    public JedisTransaction expire(String key, int seconds) {
        resultList.add(ops.expire(key, seconds));
        return this;
    }

    @Override public JedisTransaction del(String key) {
        resultList.add(ops.del(key));
        return this;
    }

    @Override
    public JedisTransaction incr(String key) {
        resultList.add(ops.incr(key));
        return this;
    }

    @Override
    public List<Object> exec() {
        // no-op, since we don't support transactionality
        return resultList;
    }

    @Override public String discard() {
        // no-op, since we don't support transactionality
        return "OK";
    }

    @Override public void close() {
        // no-op, since we don't suport transactionality
    }
}
