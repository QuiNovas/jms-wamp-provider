package com.echostreams.jms.wamp.client;

import com.echostreams.jms.wamp.auth.WampClientAuthConfig;
import com.echostreams.jms.wamp.config.JmsWampConstants;
import com.echostreams.jms.wamp.config.Status;
import com.echostreams.jms.wamp.utils.StringUtils;
import io.crossbar.autobahn.wamp.Client;
import io.crossbar.autobahn.wamp.Session;
import io.crossbar.autobahn.wamp.auth.ChallengeResponseAuth;
import io.crossbar.autobahn.wamp.auth.CryptosignAuth;
import io.crossbar.autobahn.wamp.auth.TicketAuth;
import io.crossbar.autobahn.wamp.interfaces.IAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.Map;

public class WampConnectionFactory extends com.echostreams.jms.wamp.jndi.JNDIStorable implements ConnectionFactory, QueueConnectionFactory, TopicConnectionFactory, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(WampConnectionFactory.class);
    private static final long serialVersionUID = 5725538819716172914L;

    public static String DEFAULT_WAMP_WEBSOCKET_URL = "ws://127.0.0.1:8080/ws";
    public static String DEFAULT_WAMP_REALM = "realm1";

    private static String WEBSOCKET_URL = JmsWampConstants.WAMP_WEBSOCKET_URL;
    private static String REALM = JmsWampConstants.WAMP_REALM;

    public WampConnectionFactory() {
    }

    public WampConnectionFactory(String websocketUrl, String realm) {
        WEBSOCKET_URL = websocketUrl;
        REALM = realm;
        verifyIfNotEmpty(websocketUrl, realm);
    }

    private void verifyIfNotEmpty(String websocketUrl, String realm) {
        if (StringUtils.isNullOrEmpty(websocketUrl)) {
            WEBSOCKET_URL = DEFAULT_WAMP_WEBSOCKET_URL;
        }
        if (StringUtils.isNullOrEmpty(realm)) {
            REALM = DEFAULT_WAMP_REALM;
        }
    }

    public Connection createConnection() throws JMSException {
        return createConnection(null, null);
    }

    @Override
    public Connection createConnection(String userName, String password)
            throws JMSException {
        return createWampConnection(userName, password);
    }

    @Override
    public JMSContext createContext() {
        return createContext(null, null, JMSContext.AUTO_ACKNOWLEDGE);
    }

    @Override
    public JMSContext createContext(String userName, String password) {
        return createContext(userName, password, JMSContext.AUTO_ACKNOWLEDGE);
    }

    @Override
    public JMSContext createContext(String userName, String password,
                                    int sessionMode) {
        JMSContext jmsContext = null;
        try {
            jmsContext = createWampConnection(userName, password, sessionMode);
        } catch (JMSException e) {
            LOGGER.error("createContext error ", e);
        }
        return jmsContext;
    }

    @Override
    public JMSContext createContext(int sessionMode) {
        return createContext(null, null, sessionMode);
    }

    @Override
    public QueueConnection createQueueConnection() throws JMSException {
        return (QueueConnection) createConnection();
    }

    @Override
    public QueueConnection createQueueConnection(String userName, String password) throws JMSException {
        return (QueueConnection) createConnection(userName, password);
    }

    @Override
    public TopicConnection createTopicConnection() throws JMSException {
        return (TopicConnection) createConnection();
    }

    @Override
    public TopicConnection createTopicConnection(String userName, String password) throws JMSException {
        return (TopicConnection) createConnection(userName, password);
    }

    /**
     * Set the properties that will represent the instance in JNDI
     *
     * @param props The properties to use when building the new isntance.
     * @return a new, unmodifiable, map containing any unused properties, or empty if none were.
     */
    @Override
    protected Map<String, String> buildFromProperties(Map<String, String> props) {
        String remoteURI = props.remove(DEFAULT_WAMP_WEBSOCKET_URL);
        if (remoteURI != null) {
            WEBSOCKET_URL = remoteURI;
        }

        return com.echostreams.jms.wamp.utils.PropertyUtils.setProperties(this, props);
    }

    /**
     * Initialize the instance from properties stored in JNDI
     *
     * @param props The properties to use when initializing the new instance.
     */
    @Override
    protected void populateProperties(Map<String, String> props) {
        try {
            Map<String, String> result = com.echostreams.jms.wamp.utils.PropertyUtils.getProperties(this);
            props.putAll(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Connection createWampConnection(String userName, String password) throws JMSException {
        WampClientAuthConfig wampClientAuthConfig = new WampClientAuthConfig();

        if (Status.TICKET.name().equals(wampClientAuthConfig.getAuthType())) {
            return new WampConnection(validateTicketAuth(wampClientAuthConfig.getAuthId(), wampClientAuthConfig.getAuthKey()));
        }
        if (Status.CRA.name().equals(wampClientAuthConfig.getAuthType())) {
            return new WampConnection(validateCRA(wampClientAuthConfig.getAuthId(), wampClientAuthConfig.getAuthKey()));
        }
        if (Status.CRYPTOSIGN.name().equals(wampClientAuthConfig.getAuthType())) {
            return new WampConnection(validateCryptoSign(wampClientAuthConfig.getAuthId(), wampClientAuthConfig.getAuthKey(), wampClientAuthConfig.getAuthPublicKey()));
        }

        return new WampConnection(connectWithoutAuth());
    }

    private JMSContext createWampConnection(String userName, String password, int sessionMode) throws JMSException {
        WampClientAuthConfig wampClientAuthConfig = new WampClientAuthConfig();

        if (Status.TICKET.name().equals(wampClientAuthConfig.getAuthType())) {
            return new WampJMSContext(validateTicketAuth(wampClientAuthConfig.getAuthId(), wampClientAuthConfig.getAuthKey()),
                    sessionMode);
        }
        if (Status.CRA.name().equals(wampClientAuthConfig.getAuthType())) {
            return new WampJMSContext(validateCRA(wampClientAuthConfig.getAuthId(), wampClientAuthConfig.getAuthKey()),
                    sessionMode);
        }
        if (Status.CRYPTOSIGN.name().equals(wampClientAuthConfig.getAuthType())) {
            return new WampJMSContext(validateCryptoSign(wampClientAuthConfig.getAuthId(), wampClientAuthConfig.getAuthKey(),
                    wampClientAuthConfig.getAuthPublicKey()), sessionMode);
        }

        return new WampJMSContext(connectWithoutAuth(), sessionMode);
    }

    private static Client connect(
            IAuthenticator authenticator) {
        Session wampSession = new Session();
        wampSession.addOnJoinListener((session, details) -> System.out.println("Joined session."));
        Client client = new Client(wampSession, WEBSOCKET_URL, REALM, authenticator);
        return client;
    }

    public static Client validateTicketAuth(String authid, String ticket) {
        return connect(new TicketAuth(authid, ticket));
    }

    public static Client validateCRA(String authid, String secret) {
        return connect(new ChallengeResponseAuth(authid, secret));
    }

    public static Client validateCryptoSign(String authid, String privkey, String pubkey) {
        return connect(new CryptosignAuth(authid, privkey, pubkey));
    }

    private static Client connectWithoutAuth() {
        Session wampSession = new Session();
        wampSession.addOnJoinListener((session, details) -> System.out.println("Joined session."));
        Client client = new Client(wampSession, WEBSOCKET_URL, REALM);
        return client;
    }

}
