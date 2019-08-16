package com.echostreams.jms.wamp.message;

import com.echostreams.jms.wamp.config.JmsWampConstants;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class WampTextMessage extends WampMessage implements TextMessage {

    private static final long serialVersionUID = 3988939655140380666L;

    private String payload;

    public WampTextMessage() throws JMSException {
        super();
        headers = new HashMap<>();
        headers.put(PROPERTIES, new HashMap<String, Serializable>());
        setJMSType(JmsWampConstants.TEXT_MESSAGE);
    }

    @Override
    public void clearBody() throws JMSException {
        payload = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBody(Class<T> c) throws JMSException {
        return (T) payload;
    }

    @Override
    public void setText(String string) throws JMSException {
        payload = string;
    }

    @Override
    public String getText() throws JMSException {
        return payload;
    }

    @Override
    public String toString() {
        return "WampTextMessage{" +
                "payload='" + payload + '\'' +
                '}';
    }
}
