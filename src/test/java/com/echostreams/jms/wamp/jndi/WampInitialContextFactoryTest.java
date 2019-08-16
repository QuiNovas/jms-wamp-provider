package com.echostreams.jms.wamp.jndi;

import com.echostreams.jms.wamp.client.WampConnectionFactory;
import com.echostreams.jms.wamp.client.WampQueue;
import com.echostreams.jms.wamp.client.WampTopic;
import com.echostreams.jms.wamp.config.JmsWampConstants;
import org.junit.Test;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import java.util.Hashtable;

import static org.junit.Assert.*;

public class WampInitialContextFactoryTest {

    private static final String SERVICE_URL = "pulsar://example.com:6650";
    private WampInitialContextFactory factory;
    private Context context;


    private Context createInitialContext(final Hashtable<Object, Object> environment) throws NamingException {
        factory = new WampInitialContextFactory();
        context = factory.getInitialContext(environment);
        assertNotNull("No context created", context);

        return context;
    }

    @Test
    public void testDefaultConnectionFactoriesPresentWithEmptyEnvironment() throws Exception {
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        Context ctx = createInitialContext(env);

        for (String factoryName : WampInitialContextFactory.DEFAULT_CONNECTION_FACTORY_NAMES) {
            Object o = ctx.lookup(factoryName);

            assertNotNull("No object returned", o);
            assertEquals("Unexpected class type for returned object", WampConnectionFactory.class, o.getClass());
        }
    }

    @Test
    public void testDefaultConnectionFactoriesNamesWithNewOneNotPresent() throws Exception {
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put(WampInitialContextFactory.CONNECTION_FACTORY_KEY_PREFIX + "myNewFactory", SERVICE_URL);
        Context ctx = createInitialContext(env);
        // DEFAULT_CONNECTION_FACTORY_NAMES : "ConnectionFactory", "QueueConnectionFactory", "TopicConnectionFactory"
        for (String factoryName : WampInitialContextFactory.DEFAULT_CONNECTION_FACTORY_NAMES) {
            try {
                ctx.lookup(factoryName);
                fail("should have thrown exception due to name not being found");
            } catch (NameNotFoundException nnfe) {
                //expected
            }
        }
    }

    @Test
    public void testQueueBinding() throws Exception {
        String lookupName = "myLookupName";
        String actualName = "myQueueName";

        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put(WampInitialContextFactory.QUEUE_KEY_PREFIX + lookupName, actualName);

        Context ctx = createInitialContext(env);
        Object o = ctx.lookup(lookupName);

        assertNotNull("No object returned", o);
        assertEquals("Unexpected class type for returned object", WampQueue.class, o.getClass());
        assertEquals("Unexpected name for returned object", actualName, ((WampQueue) o).getQueueName());
    }

    @Test
    public void testTopicBinding() throws Exception {
        String lookupName = "myLookupName";
        String actualName = "myTopicName";

        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put(WampInitialContextFactory.TOPIC_KEY_PREFIX + lookupName, actualName);

        Context ctx = createInitialContext(env);
        Object o = ctx.lookup(lookupName);

        assertNotNull("No object returned", o);
        assertEquals("Unexpected class type for returned object", WampTopic.class, o.getClass());
        assertEquals("Unexpected name for returned object", actualName, ((WampTopic) o).getTopicName());
    }

    @Test(expected = OperationNotSupportedException.class)
    public void testContextPreventsUnbind() throws Exception {
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        Context ctx = createInitialContext(env);

        ctx.unbind("lookupName");
    }

    @Test(expected = OperationNotSupportedException.class)
    public void testContextPreventsRebind() throws Exception {
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        Context ctx = createInitialContext(env);

        ctx.rebind("lookupName", new Object());
    }

    @Test(expected = OperationNotSupportedException.class)
    public void testContextPreventsRename() throws Exception {
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        Context ctx = createInitialContext(env);

        ctx.rename("lookupName", "");
    }

    @Test
    public void testConnectionFactory() throws Exception {
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        String factoryName = WampInitialContextFactory.DEFAULT_CONNECTION_FACTORY_NAMES[0];

        env.put(Context.INITIAL_CONTEXT_FACTORY, JmsWampConstants.JNDI_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, SERVICE_URL);
        Context ctx = createInitialContext(env);
        assertNotNull(ctx);

        ConnectionFactory connFactory = (ConnectionFactory) ctx.lookup(factoryName);
        assertNotNull(connFactory);

        assertNotNull(connFactory.createConnection());
    }

}
