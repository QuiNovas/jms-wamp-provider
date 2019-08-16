package com.echostreams.jms.wamp.client;

import org.junit.Test;

import javax.jms.JMSException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WampQueueTest {

    @Test
    public void testGetName() throws JMSException {
        WampQueue queue = new WampQueue("myQueue");
        assertEquals("myQueue", queue.getName());
    }

    @Test
    public void testNullName() throws Exception {
        WampQueue queue = new WampQueue(null);
        assertNull(queue.getName());
    }
}
