package com.echostreams.jms.wamp.message;

import org.junit.Test;

import javax.jms.JMSException;
import java.io.IOException;

import static org.junit.Assert.*;

public class WampTextMessageTest {

    @Test
    public void testSetText() throws JMSException {
        WampTextMessage textMessage;

        textMessage = new WampTextMessage();
        String str = "testText";
        textMessage.setText(str);
        assertEquals(textMessage.getText(), str);
    }

    @Test
    public void testClearBody() throws JMSException, IOException {
        WampTextMessage textMessage;

        textMessage = new WampTextMessage();
        textMessage.setText("string");
        textMessage.clearBody();
        assertNull(textMessage.getText());
    }

    @Test
    public void testNullText() throws Exception {
        WampTextMessage textMessage;

        textMessage = new WampTextMessage();
        textMessage.setText(null);
        assertNull(textMessage.getText());
    }

    @Test
    public void testToString() throws Exception {
        WampTextMessage textMessage;

        textMessage = new WampTextMessage();
        assertTrue(textMessage.toString().startsWith("WampTextMessage"));
    }

}
