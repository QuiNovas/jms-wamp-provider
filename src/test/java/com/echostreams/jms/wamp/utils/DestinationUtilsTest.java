package com.echostreams.jms.wamp.utils;

import com.echostreams.jms.wamp.client.WampDestination;
import com.echostreams.jms.wamp.client.WampQueue;
import com.echostreams.jms.wamp.client.WampTopic;
import org.junit.Test;

import javax.jms.JMSException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DestinationUtilsTest {

    @Test
    public void testTransformDestination() throws JMSException {
        WampDestination destination = null;

        assertNull(DestinationUtils.transformDestination(destination));

        destination = new WampQueue("pulsarQueue");
        destination = DestinationUtils.transformDestination(destination);
        assertNotNull(destination);
        assertEquals("pulsarQueue", destination.getName());

        destination = new WampTopic("pulsarTopic");
        destination = DestinationUtils.transformDestination(destination);
        assertNotNull(destination);
        assertEquals("pulsarTopic", destination.getName());
    }
}
