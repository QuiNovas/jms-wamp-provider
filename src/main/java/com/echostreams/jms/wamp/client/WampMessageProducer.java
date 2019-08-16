package com.echostreams.jms.wamp.client;

import com.echostreams.jms.wamp.utils.DestinationUtils;
import com.echostreams.jms.wamp.utils.MessageUtils;
import io.crossbar.autobahn.wamp.types.Publication;
import io.crossbar.autobahn.wamp.types.PublishOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

public class WampMessageProducer implements MessageProducer, QueueSender, TopicPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WampMessageProducer.class);

    private CompletionListener completionListener;
    private WampJMSContext jmsContext;
    private WampDestination destination;
    private WampSession session;

    // Producer send configuration
    private long deliveryDelay = Message.DEFAULT_DELIVERY_DELAY;
    private int deliveryMode = DeliveryMode.PERSISTENT;
    private int priority = Message.DEFAULT_PRIORITY;
    private long timeToLive = Message.DEFAULT_TIME_TO_LIVE;
    private boolean disbledMessageId;
    private boolean disableMessageTimestamp;

    // Message Headers
    private String correlationId;
    private String type;
    private Destination replyTo;
    private byte[] correlationIdBytes;


    private final ReentrantLock sendLock = new ReentrantLock();

    public WampMessageProducer() {

    }

    /**
     * @param destination
     * @param session
     */
    public WampMessageProducer(Destination destination, WampSession session) throws JMSException {
        this.destination = (WampDestination) destination;
        this.session = session;
    }

    @Override
    public void setDisableMessageID(boolean value) throws JMSException {
        this.disbledMessageId = value;
    }

    @Override
    public boolean getDisableMessageID() throws JMSException {
        return this.disbledMessageId;
    }

    @Override
    public void setDisableMessageTimestamp(boolean value) throws JMSException {
        this.disableMessageTimestamp = value;
    }

    @Override
    public boolean getDisableMessageTimestamp() throws JMSException {
        return this.disableMessageTimestamp;
    }

    @Override
    public void setDeliveryMode(int deliveryMode) throws JMSException {
        switch (deliveryMode) {
            case DeliveryMode.PERSISTENT:
            case DeliveryMode.NON_PERSISTENT:
                this.deliveryMode = deliveryMode;
                break;
            default:
                throw new JMSException(String.format("Invalid DeliveryMode specified: %d", deliveryMode));
        }
    }

    @Override
    public int getDeliveryMode() throws JMSException {
        return deliveryMode;
    }

    @Override
    public void setPriority(int defaultPriority) throws JMSException {
        if (defaultPriority < 0 || defaultPriority > 9) {
            throw new JMSException(String.format("Priority value given {%d} is out of range (0..9)", defaultPriority));
        }

        this.priority = defaultPriority;
    }

    @Override
    public int getPriority() throws JMSException {
        return priority;
    }

    @Override
    public void setTimeToLive(long timeToLive) throws JMSException {
        this.timeToLive = timeToLive;
    }

    @Override
    public long getTimeToLive() throws JMSException {
        return this.timeToLive;
    }

    @Override
    public void setDeliveryDelay(long deliveryDelay) throws JMSException {
        this.deliveryDelay = deliveryDelay;
    }

    @Override
    public long getDeliveryDelay() throws JMSException {
        return this.deliveryDelay;
    }

    @Override
    public Destination getDestination() throws JMSException {
        return destination;
    }

    @Override
    public void close() {
    }

    @Override
    public Queue getQueue() throws JMSException {
        return (Queue) destination;
    }

    @Override
    public void send(Message message) throws JMSException {
        send(destination, message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void send(Message message, int deliveryMode, int priority,
                     long timeToLive) throws JMSException {
        send(destination, message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void send(Queue queue, Message message) throws JMSException {
        send((Destination) queue, message);
    }

    @Override
    public void send(Queue queue, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        send((Destination) queue, message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void send(Destination destination, Message message)
            throws JMSException {
        send(destination, message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void send(Destination destination, Message message,
                     int deliveryMode, int priority, long timeToLive)
            throws JMSException {
        sendMessage(destination, message, deliveryMode, priority, timeToLive, null);
    }

    @Override
    public void send(Message message, CompletionListener completionListener)
            throws JMSException {
        send(destination, message, deliveryMode, priority, timeToLive,
                completionListener);

    }

    @Override
    public void send(Message message, int deliveryMode, int priority,
                     long timeToLive, CompletionListener completionListener)
            throws JMSException {
        send(destination, message, deliveryMode, priority, timeToLive,
                completionListener);
    }

    @Override
    public void send(Destination destination, Message message,
                     CompletionListener completionListener) throws JMSException {
        send(destination, message, deliveryMode, priority, timeToLive,
                completionListener);
    }

    @Override
    public void send(Destination destination, Message message,
                     int deliveryMode, int priority, long timeToLive,
                     CompletionListener completionListener) throws JMSException {

        sendMessage(destination, message, deliveryMode, priority, timeToLive, completionListener);
    }

    @Override
    public Topic getTopic() throws JMSException {
        return (Topic) destination;
    }

    @Override
    public void publish(Message message) throws JMSException {
        this.publish((Topic) this.destination, message, this.getDeliveryMode(), this.getPriority(), this.getTimeToLive());
    }

    @Override
    public void publish(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        this.publish((Topic) this.destination, message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void publish(Topic topic, Message message) throws JMSException {
        this.publish(topic, message, this.getDeliveryMode(), this.getPriority(), this.getTimeToLive());
    }

    @Override
    public void publish(Topic topic, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        this.send((Destination) topic, message, deliveryMode, priority, timeToLive);
    }

    private void sendMessage(Destination destination, Message message, int deliveryMode, int priority, long timeToLive,
                             CompletionListener completionListener) throws JMSException {
        sendLock.lock();
        // Send each message and log message content and ID when successfully received
        try {
            if (destination == null) {
                throw new InvalidDestinationException("Destination must not be null");
            }

            if (message == null) {
                throw new MessageFormatException("Message must not be null");
            }

            if (correlationId != null) {
                message.setJMSCorrelationID(correlationId);
            }
            if (correlationIdBytes != null) {
                message.setJMSCorrelationIDAsBytes(correlationIdBytes);
            }
            if (type != null) {
                message.setJMSType(type);
            }
            if (replyTo != null) {
                message.setJMSReplyTo(replyTo);
            }

            message.setJMSDestination(DestinationUtils.transformDestination(destination));
            message.setJMSTimestamp((new Date()).getTime());
            message.setJMSPriority(priority);

            if (timeToLive > 0) {
                message.setJMSExpiration(System.currentTimeMillis() + timeToLive);
            } else {
                message.setJMSExpiration(0);
            }

            Message transformedMessage = MessageUtils.transformMessage(message);
            if (transformedMessage != null) {
                message = transformedMessage;
            }

            PublishOptions publishOptions = new PublishOptions(true, false);

            // Publish on topic with args and publishOptions
            CompletableFuture<Publication> completableFuture = session.getWampSession().
                    publish(getTopic().getTopicName(), publishOptions, message); //ObjectSerializer().objectToByteArray(message)
            completableFuture.thenAccept(publication -> {
                try {
                    LOGGER.info("Published on " + getTopic().getTopicName());
                } catch (JMSException e) {
                    LOGGER.error("JMSException during send : ", e);
                }
            });
        } finally {
            sendLock.unlock();
        }
    }
}
