package com.echostreams.jms.wamp.client;

import javax.jms.JMSException;
import javax.jms.Queue;

public class WampQueue implements Queue, WampDestination {
    private static final long serialVersionUID = -7830091263426455391L;

    private String queueName;

    public WampQueue(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public String getQueueName() throws JMSException {
        return queueName;
    }

    @Override
    public String getName() throws JMSException {
        return getQueueName();
    }

}
