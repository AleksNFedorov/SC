package com.scejtesting.core.context;

import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class Context {

    protected static final Logger LOG = LoggerFactory.getLogger(Context.class);

    private final Map attributes = new HashMap();

    private final Map globalVariables = new LinkedHashMap();

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

    public void addGlobalVariable(String key, Object value) {
        Check.notNull(value, "null value not allowed for global variables");
        if (globalVariables.containsKey(key))
            LOG.warn("Replacing global variable with key [{}] and value [{}]", key, value);
        globalVariables.put(key, value);
    }

    public Map<String, ?> getGlobalVariables() {
        return new LinkedHashMap<String, Object>(globalVariables);
    }

    protected void copyTo(Context destination) {
        destination.attributes.putAll(this.attributes);
        destination.globalVariables.putAll(this.globalVariables);
        LOG.info("Context content copied");
    }
}
