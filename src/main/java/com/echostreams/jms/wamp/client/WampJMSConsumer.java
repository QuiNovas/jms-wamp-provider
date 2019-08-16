package com.echostreams.jms.wamp.client;

import io.crossbar.autobahn.wamp.types.EventDetails;
import io.crossbar.autobahn.wamp.types.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WampJMSConsumer implements JMSConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WampJMSConsumer.class);

    private Destination destination;
    private WampJMSContext jmsContext;
    private String messageSelector;

    public WampJMSConsumer(Destination destination, String messageSelector, WampJMSContext jmsContext) throws JMSException {
        this.destination = destination;
        this.messageSelector = messageSelector;
        this.jmsContext = jmsContext;
    }

    @Override
    public String getMessageSelector() {
        return messageSelector;
    }

    @Override
    public MessageListener getMessageListener() throws JMSRuntimeException {
        return listener;
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSRuntimeException {

    }

    @Override
    public Message receive() {
        return readMessages(-1, TimeUnit.MILLISECONDS);
    }

    @Override
    public Message receive(long timeout) {
        // Configure for infinite wait when timeout is zero (JMS Spec)
        if (timeout == 0) {
            timeout = -1;
        }
        return readMessages(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Message receiveNoWait() {
        return receive();
    }

    @Override
    public void close() {

    }

    @Override
    public <T> T receiveBody(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T receiveBody(Class<T> aClass, long l) {
        return null;
    }

    @Override
    public <T> T receiveBodyNoWait(Class<T> aClass) {
        return null;
    }

    private MessageListener listener = new MessageListener() {
        @Override
        public void onMessage(Message message) {
            // Denault noop listener
        }
    };

    private Message readMessages(long timeout, TimeUnit timeUnit) {

//Subscribe to topic
        CompletableFuture<Subscription> subFuture = null;
        try {
            subFuture = jmsContext.getWampSession().subscribe(getTopic().getTopicName(), this::receivedMsgHandler);
            subFuture.thenAccept(subscription -> LOGGER.info("Subscribed to topic " + subscription.topic));
        } catch (JMSException e) {
            LOGGER.info(" Error during subscription", e);
        }
        return null;
    }

    private void receivedMsgHandler(List<Object> messageList, Map<String, Object> kwargs, EventDetails details) {
        for (Object msg : messageList) {
            LOGGER.info(" Received Message : ", msg);
        }
    }

    void unsubscribe() throws JMSException {

    }

    private Topic getTopic() {
        return (Topic) destination;
    }

}
