package com.echostreams.jms.wamp.client;

import com.echostreams.jms.wamp.message.WampMessage;
import io.crossbar.autobahn.wamp.types.EventDetails;
import io.crossbar.autobahn.wamp.types.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WampMessageConsumer implements MessageConsumer, QueueReceiver, TopicSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(WampMessageConsumer.class);

    private Destination destination;
    private WampSession session;
    private String messageSelector;
    private boolean durable;
    private boolean noLocal;
    private MessageListener listener = new MessageListener() {
        @Override
        public void onMessage(Message message) {
            // Denault noop listener
        }
    };

    /**
     * consumer config should define a group Id
     *
     * @throws JMSException
     */
    public WampMessageConsumer(Destination destination, String messageSelector, WampSession session) throws JMSException {
        this.destination = destination;
        this.messageSelector = messageSelector;
        this.session = session;

    }

    @Override
    public String getMessageSelector() throws JMSException {
        // TODO Auto-generated method stub
        return this.messageSelector;
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
    public Message receive() throws JMSException {
        return readMessages(0, TimeUnit.MILLISECONDS);
    }

    @Override
    public Message receive(long timeout) throws JMSException {
        return readMessages(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Message receiveNoWait() throws JMSException {
        return receive(0);
    }

    public void commit() {
        // NOOP
    }

    @Override
    public void close() throws JMSException {

    }

    @Override
    public Queue getQueue() throws JMSException {
        return (Queue) destination;
    }

    @Override
    public Topic getTopic() throws JMSException {
        return (Topic) destination;
    }

    @Override
    public boolean getNoLocal() throws JMSException {
        return false;
    }

    private Message readMessages(long timeout, TimeUnit timeUnit) {

        //Subscribe to topic
        CompletableFuture<Subscription> subFuture = null;
        try {
            subFuture = session.getWampSession().subscribe(getTopic().getTopicName(), this::receivedMsgHandler);
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
        //session.getWampSession().unsubscribe(subscription);
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isNoLocal() {
        return noLocal;
    }

    public void setNoLocal(boolean noLocal) {
        this.noLocal = noLocal;
    }

    public WampMessage receiveAsync() throws ExecutionException, InterruptedException {
        return null;
    }

}
