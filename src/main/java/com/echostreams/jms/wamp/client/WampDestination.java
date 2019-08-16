package com.echostreams.jms.wamp.client;

import javax.jms.Destination;
import javax.jms.JMSException;
import java.io.Serializable;

public interface WampDestination extends Destination, Serializable {
    String getName() throws JMSException;
}
