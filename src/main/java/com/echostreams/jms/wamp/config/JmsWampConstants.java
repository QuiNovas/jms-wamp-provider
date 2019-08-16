package com.echostreams.jms.wamp.config;

import com.echostreams.jms.wamp.jndi.WampInitialContextFactory;

public final class JmsWampConstants {

    /**
     * Properties (fixed/not user configurable)
     */

    public static String DEFAULT_CONFIG_FILEPATH = "application.properties";

    // JNDI related constants
    public static final String JNDI_CONTEXT_FACTORY = WampInitialContextFactory.class.getName();
    public static final String JNDI_ENV_CLIENT_ID = "pulsar.naming.clientID";

    // Message Type
    public static final String TEXT_MESSAGE = "TextMessage";
    public static final String BYTES_MESSAGE = "BytesMessage";
    public static final String MAP_MESSAGE = "MapMessage";
    public static final String OBJECT_MESSAGE = "ObjectMessage";
    public static final String STREAM_MESSAGE = "StreamMessage";
    public static final String BLOB_MESSAGE = "BlobMessage";

    // PULSAR
    public static final String DOUBLE_FORWARD_SLASH = "://";
    public static final String SINGLE_FORWARD_SLASH = "/";
    public static final String PERSISTENT_NAME = "persistent";
    public static final String TENANT_NAME = "public";
    public static final String NAMESPACE_NAME = "default";
    public static final String NON_PERSISTENT_NAME = "non-persistent";
    //persistent://public/default/my-topic
    public static final String DEFAULT_PULSAR_TOPIC_NAME_PREFIX = PERSISTENT_NAME + DOUBLE_FORWARD_SLASH + TENANT_NAME + SINGLE_FORWARD_SLASH + NAMESPACE_NAME;

    /**
     * Properties (user configurable)
     */

    //WAMP
    public static final String WAMP_WEBSOCKET_URL = ConfigurationLoader.getConfig().getProperty(ConfigProperties.WAMP_WEBSOCKET_URL_PROP);
    public static final String WAMP_REALM = ConfigurationLoader.getConfig().getProperty(ConfigProperties.WAMP_REALM_PROP);
    public static final String WAMP_CLIENT_AUTH_TYPE = ConfigurationLoader.getConfig().getProperty(ConfigProperties.WAMP_CLIENT_AUTH_TYPE_PROP);
    public static final String WAMP_CLIENT_AUTH_ID = ConfigurationLoader.getConfig().getProperty(ConfigProperties.WAMP_CLIENT_AUTH_ID_PROP);
    public static final String WAMP_CLIENT_AUTH_KEY = ConfigurationLoader.getConfig().getProperty(ConfigProperties.WAMP_CLIENT_AUTH_KEY_PROP);
    public static final String WAMP_CLIENT_AUTH_PUB_KEY = ConfigurationLoader.getConfig().getProperty(ConfigProperties.WAMP_CLIENT_AUTH_PUB_KEY_PROP);
}

