package com.echostreams.jms.wamp.utils;

import com.echostreams.jms.wamp.client.WampDestination;
import com.echostreams.jms.wamp.client.WampQueue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

public class DestinationUtils {

    public static WampDestination transformDestination(Destination destination) throws JMSException {
        if (destination == null) {
            return null;
        }
        if (destination instanceof Queue) {
            return new WampQueue(((Queue) destination).getQueueName());
        }
        if (destination instanceof Topic) {
            return new WampQueue(((Topic) destination).getTopicName());
        }
        if (destination instanceof WampDestination) {
            return (WampDestination) destination;
        }
        //TODO need to add for temporary if needed
        throw new JMSException("Could not support non-OpenJMS destination into a Pulsar destination: " + destination);
    }

}

