package com.echostreams.jms.wamp.client;

import org.junit.Test;

import javax.jms.JMSException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WampTopicTest {

    @Test
    public void testGetName() throws JMSException {
        WampTopic topic = new WampTopic("myTopic");
        assertEquals("myTopic", topic.getName());
    }

    @Test
    public void testNullName() throws Exception {
        WampTopic topic = new WampTopic(null);
        assertNull(topic.getName());
    }
}
