package com.scej.core;

import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class Context {
    protected static final Logger LOG = LoggerFactory.getLogger(TestContext.class);

    private final Map attributes = new TreeMap();

    public <K, V> void addAttribute(K key, V value) {
        Check.notNull(value, "null value not allowed, use cleanAttribute instead of");
        if (attributes.containsKey(key))
            LOG.warn("Replacing key [{}] with value [{}]", key, value);
        attributes.put(key, value);
    }

    public <K, V> V getAttribute(K key) {
        Check.notNull(key, "null key is not allowed");
        return (V) attributes.get(key);
    }

    public <K> void cleanAttribute(K key) {
        Check.notNull(key, "null key is not allowed");
        attributes.remove(key);
    }
}
