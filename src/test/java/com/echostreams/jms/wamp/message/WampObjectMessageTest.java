package com.echostreams.jms.wamp.message;

import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.MessageNotWriteableException;
import java.io.IOException;

import static org.junit.Assert.*;

public class WampObjectMessageTest {

    @Test
    public void testBytes() throws JMSException, IOException {
        WampObjectMessage msg;

        msg = new WampObjectMessage();
        String str = "testText";
        msg.setObject(str);

        assertEquals(msg.getObject(), str);
    }

    @Test
    public void testSetObject() throws JMSException {
        WampObjectMessage objectMessage;

        objectMessage = new WampObjectMessage();
        String str = "testText";
        objectMessage.setObject(str);
        assertEquals(str, objectMessage.getObject());
    }

    @Test
    public void testClearBody() throws JMSException {
        WampObjectMessage objectMessage;

        objectMessage = new WampObjectMessage();
        try {
            objectMessage.setObject("String");
            objectMessage.clearBody();
            assertFalse(objectMessage.readOnlyBody);
            assertNull(objectMessage.getObject());
            objectMessage.setObject("String");
            objectMessage.getObject();
        } catch (MessageNotWriteableException mnwe) {
            fail("should be writeable");
        }
    }

    @Test
    public void testToString() throws Exception {
        WampObjectMessage objectMessage;

        objectMessage = new WampObjectMessage();
        assertTrue(objectMessage.toString().startsWith("WampObjectMessage"));
    }
}
