package com.echostreams.jms.wamp.config;

import com.echostreams.jms.wamp.auth.WampClientAuthConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public final class ConfigurationLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLoader.class);

    private Properties properties = null;
    private static ConfigurationLoader loader = null;

    public static synchronized ConfigurationLoader getConfig() {
        if (loader == null) {
            //synchronized block to remove overhead
            synchronized (ConfigurationLoader.class) {
                if (loader == null) {
                    // if instance is null, initialize
                    loader = new ConfigurationLoader();
                }
            }
        }
        return loader;

    }

    private ConfigurationLoader() {
        LOGGER.info("Loading configuration of configName = {}.", JmsWampConstants.DEFAULT_CONFIG_FILEPATH);
        this.properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(JmsWampConstants.DEFAULT_CONFIG_FILEPATH));
            assignAuthConfig();
        } catch (Exception ex) {
            LOGGER.error("Something Went wrong during loading configuration ", ex);
        }
    }

    /**
     * Get a property value of the given property
     *
     * @param key
     * @return
     */

    public String getProperty(String key) {
        String result = null;
        if (key != null && !key.trim().isEmpty()) {
            result = this.properties.getProperty(key);
        }
        return result;
    }

    public void assignAuthConfig() {
        new WampClientAuthConfig(JmsWampConstants.WAMP_CLIENT_AUTH_TYPE,
                JmsWampConstants.WAMP_CLIENT_AUTH_ID, JmsWampConstants.WAMP_CLIENT_AUTH_KEY
                , JmsWampConstants.WAMP_CLIENT_AUTH_PUB_KEY);
    }

}
