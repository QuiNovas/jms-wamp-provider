package com.echostreams.jms.wamp.client;

import com.echostreams.jms.wamp.common.WampConnectionMetaDataImpl;
import io.crossbar.autobahn.wamp.Client;

import javax.jms.*;
import java.util.Properties;

public class WampConnection implements Connection, QueueConnection, TopicConnection {

    private static ConnectionMetaData metaData = new WampConnectionMetaDataImpl();

    private Properties config;
    private Session session;
    private Client client;

    public WampConnection(Client client) {
        this.client = client;
    }

    @Override
    public Session createSession(boolean transacted, int acknowledgeMode)
            throws JMSException {
        session = new WampSession(this, transacted, acknowledgeMode);
        return session;
    }

    @Override
    public Session createSession(int sessionMode) throws JMSException {
        return createSession(true, sessionMode);
    }

    @Override
    public Session createSession() throws JMSException {
        return createSession(true, Session.AUTO_ACKNOWLEDGE);
    }

    @Override
    public String getClientID() throws JMSException {
        //return (String) config.get(ProducerConfiguration.CLIENT_ID_CONFIG);
        return null;
    }

    @Override
    public void setClientID(String clientID) throws JMSException {
        //config.put(ProducerConfig.CLIENT_ID_CONFIG, clientID);
    }

    @Override
    public ConnectionMetaData getMetaData() throws JMSException {
        return metaData;
    }


    @Override
    public ExceptionListener getExceptionListener() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setExceptionListener(ExceptionListener listener)
            throws JMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public void start() throws JMSException {
        // TODO create session pool
    }

    @Override
    public void stop() throws JMSException {
        close();
    }

    @Override
    public void close() throws JMSException {
        session.close();
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Destination destination,
                                                       String messageSelector, ServerSessionPool sessionPool,
                                                       int maxMessages) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConnectionConsumer createSharedConnectionConsumer(Topic topic,
                                                             String subscriptionName, String messageSelector,
                                                             ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return (TopicSession) createSession(transacted, acknowledgeMode);
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Topic topic, String s, ServerSessionPool serverSessionPool, int i) throws JMSException {
        return null;
    }

    @Override
    public ConnectionConsumer createDurableConnectionConsumer(Topic topic,
                                                              String subscriptionName, String messageSelector,
                                                              ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConnectionConsumer createSharedDurableConnectionConsumer(
            Topic topic, String subscriptionName, String messageSelector,
            ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return (QueueSession) createSession(transacted, acknowledgeMode);
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Queue queue, String s, ServerSessionPool serverSessionPool, int i) throws JMSException {
        return null;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
