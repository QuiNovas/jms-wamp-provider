package com.echostreams.jms.wamp.exceptions;

import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class WampJMSExceptionTest {

    @Test(expected = JMSRuntimeException.class)
    public void testConvertsJMSExceptionToJMSRuntimeException() {
        throw WampJMSException.createRuntimeException(new JMSException("error"));
    }

    @Test(expected = IllegalStateRuntimeException.class)
    public void testConvertsIllegalStateExceptionToIlleglStateRuntimeException() {
        throw WampJMSException.createRuntimeException(new javax.jms.IllegalStateException("error"));
    }

    @Test(expected = InvalidClientIDRuntimeException.class)
    public void testConvertsInvalidClientIDExceptionToInvalidClientIDRuntimeException() {
        throw WampJMSException.createRuntimeException(new InvalidClientIDException("error"));
    }

    @Test(expected = InvalidDestinationRuntimeException.class)
    public void testConvertsInvalidDestinationExceptionToInvalidDestinationRuntimeException() {
        throw WampJMSException.createRuntimeException(new InvalidDestinationException("error"));
    }

    @Test(expected = InvalidSelectorRuntimeException.class)
    public void testConvertsInvalidSelectorExceptionToInvalidSelectorRuntimeException() {
        throw WampJMSException.createRuntimeException(new InvalidSelectorException("error"));
    }

    @Test(expected = JMSSecurityRuntimeException.class)
    public void testConvertsJMSSecurityExceptionToJMSSecurityRuntimeException() {
        throw WampJMSException.createRuntimeException(new JMSSecurityException("error"));
    }

    @Test(expected = MessageFormatRuntimeException.class)
    public void testConvertsMessageFormatExceptionToMessageFormatRuntimeException() {
        throw WampJMSException.createRuntimeException(new MessageFormatException("error"));
    }

    @Test(expected = MessageNotWriteableRuntimeException.class)
    public void testConvertsMessageNotWriteableExceptionToMessageNotWriteableRuntimeException() {
        throw WampJMSException.createRuntimeException(new MessageNotWriteableException("error"));
    }

    @Test(expected = ResourceAllocationRuntimeException.class)
    public void testConvertsResourceAllocationExceptionToResourceAllocationRuntimeException() {
        throw WampJMSException.createRuntimeException(new ResourceAllocationException("error"));
    }

    @Test(expected = TransactionInProgressRuntimeException.class)
    public void testConvertsTransactionInProgressExceptionToTransactionInProgressRuntimeException() {
        throw WampJMSException.createRuntimeException(new TransactionInProgressException("error"));
    }

    @Test(expected = TransactionRolledBackRuntimeException.class)
    public void testConvertsTransactionRolledBackExceptionToTransactionRolledBackRuntimeException() {
        throw WampJMSException.createRuntimeException(new TransactionRolledBackException("error"));
    }

    @Test(expected = JMSRuntimeException.class)
    public void testConvertsNonJMSExceptionToJMSRuntimeException() {
        throw WampJMSException.createRuntimeException(new IOException());
    }
}
