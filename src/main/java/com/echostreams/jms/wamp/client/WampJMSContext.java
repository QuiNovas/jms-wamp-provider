package com.echostreams.jms.wamp.client;

import com.echostreams.jms.wamp.common.WampConnectionMetaDataImpl;
import com.echostreams.jms.wamp.message.*;
import io.crossbar.autobahn.wamp.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WampJMSContext implements JMSContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(WampJMSContext.class);

    private static ConnectionMetaData metaData = new WampConnectionMetaDataImpl();

    private WampJMSProducer producer;
    private WampJMSConsumer consumer;
    private MessageListener listener;
    private Client client;
    private Destination destination;
    private int sessionMode;
    private final Executor executor;
    private final io.crossbar.autobahn.wamp.Session wampSession;


    public WampJMSContext(Client client, int sessionMode) throws JMSException {
        this.client = client;
        this.sessionMode = sessionMode;
        this.executor = Executors.newSingleThreadExecutor();
        this.wampSession = new io.crossbar.autobahn.wamp.Session(executor);
    }

    @Override
    public JMSContext createContext(int sessionMode) {
        WampJMSContext wampJMSContext = null;
        try {
            wampJMSContext = new WampJMSContext(client, sessionMode);
        } catch (JMSException e) {
            LOGGER.error(" Exception during createContext: ", e);
        }
        return wampJMSContext;
    }

    @Override
    public JMSProducer createProducer() {
        try {
            producer = new WampJMSProducer(this);
        } catch (JMSException e) {
            LOGGER.error("Pulsar createProducer JMSException ", e);
        }
        return producer;
    }

    @Override
    public String getClientID() {
        return null;
    }

    @Override
    public void setClientID(String s) {

    }

    @Override
    public ConnectionMetaData getMetaData() {
        return metaData;
    }

    @Override
    public ExceptionListener getExceptionListener() {
        return null;
    }

    @Override
    public void setExceptionListener(ExceptionListener exceptionListener) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setAutoStart(boolean b) {

    }

    @Override
    public boolean getAutoStart() {
        return false;
    }

    @Override
    public void close() {
        try {
            consumer.close();
        } catch (Exception e) {
        }
    }

    @Override
    public BytesMessage createBytesMessage() {
        WampBytesMessage wampBytesMessage = null;
        try {
            wampBytesMessage = new WampBytesMessage();
        } catch (JMSException e) {
            LOGGER.error("createBytesMessage error", e);
        }
        return wampBytesMessage;
    }

    @Override
    public MapMessage createMapMessage() {
        WampMapMessage wampMapMessage = null;
        try {
            wampMapMessage = new WampMapMessage();
        } catch (JMSException e) {
            LOGGER.error("createMapMessage error", e);
        }
        return wampMapMessage;
    }

    @Override
    public Message createMessage() {
        return createObjectMessage();
    }

    @Override
    public ObjectMessage createObjectMessage() {
        WampObjectMessage pulsarObjectMessage = null;
        try {
            pulsarObjectMessage = new WampObjectMessage();
        } catch (JMSException e) {
            LOGGER.error("createObjectMessage error", e);
        }
        return pulsarObjectMessage;
    }

    @Override
    public ObjectMessage createObjectMessage(Serializable serializable) {
        WampObjectMessage pulsarObjectMessage = null;
        try {
            pulsarObjectMessage = new WampObjectMessage();
            pulsarObjectMessage.setObject(serializable);
        } catch (JMSException e) {
            LOGGER.error("createObjectMessage error", e);
        }
        return pulsarObjectMessage;
    }

    @Override
    public StreamMessage createStreamMessage() {
        WampStreamMessage pulsarStreamMessage = null;
        try {
            pulsarStreamMessage = new WampStreamMessage();
        } catch (JMSException e) {
            LOGGER.error("createStreamMessage error", e);
        }
        return pulsarStreamMessage;
    }

    @Override
    public TextMessage createTextMessage() {
        WampTextMessage pulsarTextMessage = null;
        try {
            pulsarTextMessage = new WampTextMessage();
        } catch (JMSException e) {
            LOGGER.error("createTextMessage error", e);
        }
        return pulsarTextMessage;
    }

    @Override
    public TextMessage createTextMessage(String value) {
        WampTextMessage pulsarTextMessage = null;
        try {
            pulsarTextMessage = new WampTextMessage();
            pulsarTextMessage.setText(value);
        } catch (JMSException e) {
            LOGGER.error("createTextMessage error", e);
        }
        return pulsarTextMessage;
    }

    @Override
    public boolean getTransacted() {
        return (JMSContext.SESSION_TRANSACTED == sessionMode);
    }

    @Override
    public int getSessionMode() {
        return sessionMode;
    }

    @Override
    public void commit() {
    }

    @Override
    public void rollback() {

    }

    @Override
    public void recover() {

    }

    @Override
    public JMSConsumer createConsumer(Destination destination) {
        return createConsumer(destination, null, false);
    }

    @Override
    public JMSConsumer createConsumer(Destination destination, String messageSelector) {
        return createConsumer(destination, messageSelector, false);
    }

    @Override
    public JMSConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) {
        try {
            if (destination == null)
                throw new IllegalArgumentException("destination may not be null!");

            consumer = new WampJMSConsumer(destination, messageSelector, this);
        } catch (JMSException e) {
            LOGGER.error("createConsumer JMSException ", e);
        }
        return consumer;
    }

    @Override
    public Queue createQueue(String queueName) {
        WampQueue pulsarQueue = new WampQueue(queueName);
        this.destination = pulsarQueue;
        return pulsarQueue;
    }

    @Override
    public Topic createTopic(String topicName) {
        WampTopic pulsarTopic = new WampTopic(topicName);
        this.destination = pulsarTopic;
        return pulsarTopic;
    }

    @Override
    public JMSConsumer createDurableConsumer(Topic topic, String name) {
        return createDurableConsumer(topic, name, null, false);
    }

    @Override
    public JMSConsumer createDurableConsumer(Topic topic, String s, String s1, boolean b) {
        return null;
    }

    @Override
    public JMSConsumer createSharedDurableConsumer(Topic topic, String s) {
        return null;
    }

    @Override
    public JMSConsumer createSharedDurableConsumer(Topic topic, String s, String s1) {
        return null;
    }

    @Override
    public JMSConsumer createSharedConsumer(Topic topic, String s) {
        return null;
    }

    @Override
    public JMSConsumer createSharedConsumer(Topic topic, String s, String s1) {
        return null;
    }

    @Override
    public QueueBrowser createBrowser(Queue queue) {
        return null;
    }

    @Override
    public QueueBrowser createBrowser(Queue queue, String s) {
        return null;
    }

    @Override
    public TemporaryQueue createTemporaryQueue() {
        return null;
    }

    @Override
    public TemporaryTopic createTemporaryTopic() {
        return null;
    }

    @Override
    public void unsubscribe(String s) {
        try {
            consumer.unsubscribe();
        } catch (JMSException e) {
            LOGGER.error("JMSContext unsubscribe JMSException ", e);
        }
    }

    @Override
    public void acknowledge() {
    }

    public Client getClient() {
        return client;
    }

    public Destination getDestination() {
        return destination;
    }

    public io.crossbar.autobahn.wamp.Session getWampSession() {
        return this.wampSession;
    }
}
