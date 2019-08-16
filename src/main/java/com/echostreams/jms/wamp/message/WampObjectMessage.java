package com.echostreams.jms.wamp.message;

import com.echostreams.jms.wamp.config.JmsWampConstants;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.util.HashMap;

public class WampObjectMessage extends WampMessage implements ObjectMessage {

    private static final long serialVersionUID = -899234475829789736L;
    private Serializable payload;

    public WampObjectMessage() throws JMSException {
        super();
        headers = new HashMap<>();
        headers.put(PROPERTIES, new HashMap<String, Serializable>());
        setJMSType(JmsWampConstants.OBJECT_MESSAGE);
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
    public void setObject(Serializable object) throws JMSException {
        checkWriteMode();
        if (object == null)
            throw new IllegalArgumentException("Serializable object is null!");

        this.payload = object;
    }

    @Override
    public Serializable getObject() throws JMSException {
        return this.payload;
    }

    @Override
    public String toString() {
        return "WampObjectMessage{" +
                "payload=" + payload +
                '}';
    }
}
