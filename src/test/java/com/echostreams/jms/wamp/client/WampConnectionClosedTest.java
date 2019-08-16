package com.echostreams.jms.wamp.client;

import org.junit.Before;
import org.junit.Test;

import javax.jms.Destination;
import javax.jms.Session;

import static org.junit.Assert.assertNull;

public class WampConnectionClosedTest extends WampConnectionTestSupport {

    protected Destination destination;

    protected WampConnection createAndCloseConnection(WampConnection connection) throws Exception {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createTopic("test");
        connection.close();
        return connection;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        connection = createConnectionToMockWampClient();
        createAndCloseConnection(connection);
    }

    @Test(timeout = 30000)
    public void testGetMetaData() throws Exception {
        connection.getMetaData();
    }

    @Test(timeout = 30000)
    public void testStop() throws Exception {
        connection.stop();
    }

    @Test(timeout = 30000)
    public void testClose() throws Exception {
        connection.close();
    }

    @Test(timeout = 30000)
    public void testCreateConnectionConsumerNull() throws Exception {
        assertNull(connection.createConnectionConsumer(destination, "", null, 1));
    }
}
