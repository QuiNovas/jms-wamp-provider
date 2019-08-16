package com.echostreams.jms.wamp.client;

import com.echostreams.jms.wamp.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WampSession implements Session, QueueSession, TopicSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(WampSession.class);

    private Properties config;
    private WampMessageProducer producer;
    private WampMessageConsumer consumer;
    private MessageListener listener;
    private WampConnection connection;
    private int acknowledgeMode;
    private boolean transacted;
    private final Executor executor;
    private final io.crossbar.autobahn.wamp.Session wampSession;

    public WampSession(WampConnection connection, boolean transacted, int acknowledgeMode) {
        this.connection = connection;
        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
        this.executor = Executors.newSingleThreadExecutor();
        this.wampSession = new io.crossbar.autobahn.wamp.Session(executor);
    }

    @Override
    public BytesMessage createBytesMessage() throws JMSException {
        return new WampBytesMessage();
    }

    @Override
    public MapMessage createMapMessage() throws JMSException {
        return new WampMapMessage();
    }

    @Override
    public Message createMessage() throws JMSException {
        return createObjectMessage();
    }

    @Override
    public ObjectMessage createObjectMessage() throws JMSException {
        return new WampObjectMessage();
    }

    @Override
    public ObjectMessage createObjectMessage(Serializable object)
            throws JMSException {
        WampObjectMessage msg = new WampObjectMessage();
        msg.setObject(object);
        return msg;
    }

    @Override
    public StreamMessage createStreamMessage() throws JMSException {
        return new WampStreamMessage();
    }

    @Override
    public TextMessage createTextMessage() throws JMSException {
        return new WampTextMessage();
    }

    @Override
    public TextMessage createTextMessage(String text) throws JMSException {
        WampTextMessage msg = new WampTextMessage();
        msg.setText(text);
        return msg;
    }

    @Override
    public boolean getTransacted() throws JMSException {
        return this.transacted;
    }

    @Override
    public int getAcknowledgeMode() throws JMSException {
        return this.acknowledgeMode;
    }

    @Override
    public void commit() throws JMSException {
        consumer.commit();
    }

    @Override
    public void rollback() throws JMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() throws JMSException {
        try {
            producer.close();
            consumer.close();
        } catch (Exception e) {
        }
    }

    @Override
    public void recover() throws JMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return listener;
    }

    @Override
    public void setMessageListener(MessageListener listener)
            throws JMSException {
        this.listener = listener;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public WampMessageProducer createProducer(Destination destination) throws JMSException {

        if (destination == null)
            throw new IllegalArgumentException("destination may not be null!");

        try {
            producer = new WampMessageProducer(destination, this);
        } catch (Exception e) {
            LOGGER.error("Error while creating Producer", e);
            System.exit(-1);
        }
        return producer;
    }

    @Override
    public WampMessageConsumer createConsumer(Destination destination)
            throws JMSException {
        return createConsumer(destination, null, false);
    }

    @Override
    public WampMessageConsumer createConsumer(Destination destination,
                                              String messageSelector) throws JMSException {
        return createConsumer(destination, messageSelector, false);
    }

    @Override
    public WampMessageConsumer createConsumer(Destination destination,
                                              String messageSelector, boolean noLocal) throws JMSException {
        if (destination == null)
            throw new IllegalArgumentException("destination may not be null!");

        consumer = new WampMessageConsumer(destination, messageSelector, this);
        return consumer;
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic,
                                                String sharedSubscriptionName) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic,
                                                String sharedSubscriptionName, String messageSelector)
            throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Queue createQueue(String queueName) throws JMSException {
        return new WampQueue(queueName);
    }

    @Override
    public QueueReceiver createReceiver(Queue queue) throws JMSException {
        return createReceiver(queue, null);
    }

    @Override
    public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException {
        return createConsumer(queue, messageSelector);
    }

    @Override
    public QueueSender createSender(Queue queue) throws JMSException {
        return createProducer(queue);
    }

    @Override
    public Topic createTopic(String topicName) throws JMSException {
        return new WampTopic(topicName);
    }

    @Override
    public TopicSubscriber createSubscriber(Topic topic) throws JMSException {
        return createSubscriber(topic, null, false);
    }

    @Override
    public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
        return createConsumer(topic, messageSelector, noLocal);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
        return createDurableSubscriber(topic, name, null, false);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String name,
                                                   String messageSelector, boolean noLocal) throws JMSException {
        if (topic == null)
            throw new IllegalArgumentException("topic is null!");
        if (name == null)
            throw new IllegalArgumentException("name is null!");

        WampMessageConsumer pulsarMessageConsumer =
                new WampMessageConsumer((WampDestination) topic, messageSelector, this);

        pulsarMessageConsumer.setNoLocal(noLocal);
        pulsarMessageConsumer.setDurable(true);
        return pulsarMessageConsumer;
    }

    @Override
    public TopicPublisher createPublisher(Topic topic) throws JMSException {
        return createProducer(topic);
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String name)
            throws JMSException {
        return createDurableConsumer(topic, name, null, false);
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String name,
                                                 String messageSelector, boolean noLocal) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String name)
            throws JMSException {
        return createSharedDurableConsumer(topic, name, null);
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic,
                                                       String name, String messageSelector) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        throw new UnsupportedOperationException("Browsing is not supported for Wamp");
    }

    @Override
    public QueueBrowser createBrowser(Queue queue, String messageSelector)
            throws JMSException {
        throw new UnsupportedOperationException("Browsing is not supported for Wamp");
    }

    @Override
    public TemporaryQueue createTemporaryQueue() throws JMSException {
        throw new UnsupportedOperationException("Temporary queues are not supported for Wamp");
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        throw new UnsupportedOperationException("Temporary topics are not supported for Wamp");
    }

    @Override
    public void unsubscribe(String name) throws JMSException {
        consumer.unsubscribe();
    }

    public WampConnection getConnection() {
        return connection;
    }

    public io.crossbar.autobahn.wamp.Session getWampSession() {
        return this.wampSession;
    }
}
