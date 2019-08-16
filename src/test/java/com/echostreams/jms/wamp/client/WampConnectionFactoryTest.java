package com.echostreams.jms.wamp.client;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

import static org.junit.Assert.*;

public class WampConnectionFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WampConnectionFactoryTest.class);

    @Test
    public void testCreateConnection() throws JMSException {
        WampConnectionFactory factory = new WampConnectionFactory("mock://127.0.0.1:6650", "realm1");

        Connection connection = factory.createConnection();
        assertNotNull(connection);

        try {
            connection.close();
            fail("Should have thrown exception");
        } catch (NullPointerException npe) {
        }
    }

    @Test
    public void testCreateQueueConnection() throws JMSException {
        WampConnectionFactory factory = new WampConnectionFactory("mock://127.0.0.1:6650", "realm1");

        QueueConnection connection = factory.createQueueConnection();
        assertNotNull(connection);
        try {
            connection.close();
            fail("Should have thrown exception");
        } catch (NullPointerException npe) {
        }
    }

    @Test
    public void testCreateTopicConnection() throws JMSException {
        WampConnectionFactory factory = new WampConnectionFactory("mock://127.0.0.1:6650", "realm1");

        TopicConnection connection = factory.createTopicConnection();
        assertNotNull(connection);

        try {
            connection.close();
            fail("Should have thrown exception");
        } catch (NullPointerException npe) {
        }
    }

    public void testCreateConnectionWithPortOutOfRange() throws Exception {
        WampConnectionFactory factory = new WampConnectionFactory("pulsar://127.0.0.1:665066506", "realm1");

        try {
            factory.createConnection();
            fail("Should have thrown exception");
        } catch (IllegalArgumentException iae) {
            LOGGER.debug("Exceiption Occurred : ", iae);
        }

        factory = new WampConnectionFactory("pulsar://127.0.0.1:665066506", "realm1");

        try {
            factory.createConnection();
            fail("Should have thrown exception");
        } catch (IllegalArgumentException iae) {
            LOGGER.debug("Exceiption Occurred : ", iae);
        }
    }

    @Test
    public void testCreateContext() {
        WampConnectionFactory factory = new WampConnectionFactory("mock://127.0.0.1:6650", "realm1");

        JMSContext context = factory.createContext();
        assertNotNull(context);
        assertEquals(JMSContext.AUTO_ACKNOWLEDGE, context.getSessionMode());

        context.close();
    }

    @Test
    public void testCreateContextWithSessionMode() {
        WampConnectionFactory factory = new WampConnectionFactory("mock://127.0.0.1:6650", "realm1");

        JMSContext context = factory.createContext(JMSContext.CLIENT_ACKNOWLEDGE);
        assertNotNull(context);
        assertEquals(JMSContext.CLIENT_ACKNOWLEDGE, context.getSessionMode());

        context.close();
    }

}
