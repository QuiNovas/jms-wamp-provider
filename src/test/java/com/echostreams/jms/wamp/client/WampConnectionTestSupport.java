package com.echostreams.jms.wamp.client;

import io.crossbar.autobahn.wamp.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.TopicConnection;

import static org.mockito.Mockito.mock;

public class WampConnectionTestSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(WampConnectionTestSupport.class);

    protected WampConnection connection;


    @Rule
    public TestName _testName = new TestName();

    private Client createMockWampClient() {
        return mock(Client.class);
    }

    protected WampConnection createConnectionToMockWampClient() {
        return createConnection();
    }

    protected QueueConnection createQueueConnectionToMockWampClient() {
        return createConnection();
    }

    protected TopicConnection createTopicConnectionToMockWampClient() {
        return createConnection();
    }

    private WampConnection createConnection() {
        return new WampConnection(createMockWampClient());
    }

    protected WampJMSContext createContextToMockWampClient() throws JMSException {
        return new WampJMSContext(createMockWampClient(), JMSContext.AUTO_ACKNOWLEDGE);
    }

    @Before
    public void setUp() throws Exception {
        LOGGER.info("========== start " + getTestName() + " ==========");
    }

    @After
    public void tearDown() throws Exception {
        LOGGER.info("========== tearDown " + getTestName() + " ==========");
    }

    protected String getTestName() {
        return getClass().getSimpleName() + "." + _testName.getMethodName();
    }

}
