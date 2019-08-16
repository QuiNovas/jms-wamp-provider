package com.echostreams.jms.wamp.client;

import javax.jms.JMSException;
import javax.jms.Topic;

public class WampTopic implements Topic, WampDestination {

    private static final long serialVersionUID = 4029670726631550860L;

    private String topicName;

    /**
     * @param topicName
     */
    public WampTopic(String topicName) {
        super();
        this.topicName = topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String getTopicName() throws JMSException {
        return topicName;
    }

    @Override
    public String getName() throws JMSException {
        return getTopicName();
    }

}
