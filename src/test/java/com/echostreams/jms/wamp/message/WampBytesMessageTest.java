package com.echostreams.jms.wamp.message;

import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;

import static org.junit.Assert.*;

public class WampBytesMessageTest {

    @Test
    public void testClearBodyOnNewMessage() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeInt(1);
        bytesMessage.clearBody();
        assertFalse(bytesMessage.readOnlyBody);
    }

    @Test
    public void testGetBodyLength() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        int len = 10;
        for (int i = 0; i < len; i++) {
            bytesMessage.writeLong(5L);
        }

        bytesMessage.reset();
        assertTrue(bytesMessage.getBodyLength() == (len * 8));
    }

    @Test
    public void testReadBoolean() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeBoolean(true);
        bytesMessage.reset();
        assertTrue(bytesMessage.readBoolean());
    }

    @Test
    public void testReadByte() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeByte((byte) 2);
        bytesMessage.reset();
        assertTrue(bytesMessage.readByte() == 2);
    }

    @Test
    public void testReadUnsignedByte() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeByte((byte) 2);
        bytesMessage.reset();
        assertTrue(bytesMessage.readUnsignedByte() == 2);
    }

    @Test
    public void testReadShort() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeShort((short) 3000);
        bytesMessage.reset();
        assertTrue(bytesMessage.readShort() == 3000);
    }

    @Test
    public void testReadUnsignedShort() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeShort((short) 3000);
        bytesMessage.reset();
        assertTrue(bytesMessage.readUnsignedShort() == 3000);
    }

    @Test
    public void testReadChar() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeChar('a');
        bytesMessage.reset();
        assertTrue(bytesMessage.readChar() == 'a');
    }

    @Test
    public void testReadInt() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeInt(3000);
        bytesMessage.reset();
        assertTrue(bytesMessage.readInt() == 3000);
    }

    @Test
    public void testReadLong() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeLong(3000);
        bytesMessage.reset();
        assertTrue(bytesMessage.readLong() == 3000);
    }

    @Test
    public void testReadFloat() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeFloat(3.3f);
        bytesMessage.reset();
        assertTrue(bytesMessage.readFloat() == 3.3f);
    }

    @Test
    public void testReadDouble() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        bytesMessage.writeDouble(3.3d);
        bytesMessage.reset();
        assertTrue(bytesMessage.readDouble() == 3.3d);
    }

    @Test
    public void testReadUTF() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        String str = "this is a test";
        bytesMessage.writeUTF(str);
        bytesMessage.reset();
        assertTrue(bytesMessage.readUTF().equals(str));
    }

    @Test
    public void testReadBytesbyteArray() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        byte[] data = new byte[50];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        bytesMessage.writeBytes(data);
        bytesMessage.reset();
        byte[] test = new byte[data.length];
        bytesMessage.readBytes(test);
        for (int i = 0; i < test.length; i++) {
            assertTrue(test[i] == i);
        }
    }

    @Test
    public void testWriteObject() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        try {
            bytesMessage.writeObject("pulsar");
            bytesMessage.writeObject(Boolean.TRUE);
            bytesMessage.writeObject(Character.valueOf('q'));
            bytesMessage.writeObject(Byte.valueOf((byte) 1));
            bytesMessage.writeObject(Short.valueOf((short) 3));
            bytesMessage.writeObject(Integer.valueOf(3));
            bytesMessage.writeObject(Long.valueOf(300L));
            bytesMessage.writeObject(Float.valueOf(3.3f));
            bytesMessage.writeObject(Double.valueOf(3.3));
            bytesMessage.writeObject(new byte[3]);
        } catch (MessageFormatException mfe) {
            fail("objectified primitives should be allowed");
        }
        try {
            bytesMessage.writeObject(new Object());
            fail("only objectified primitives are allowed");
        } catch (MessageFormatException mfe) {
        }
    }

    @Test
    public void testReset() throws JMSException {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        try {
            bytesMessage.writeDouble(24.5);
            bytesMessage.writeLong(311);
        } catch (MessageNotWriteableException mnwe) {
            fail("should be writeable");
        }
        bytesMessage.reset();
        try {
            assertEquals(bytesMessage.readDouble(), 24.5, 0);
            assertEquals(bytesMessage.readLong(), 311);
        } catch (MessageNotReadableException mnre) {
            fail("should be readable");
        }
    }

    @Test
    public void testToString() throws Exception {
        WampBytesMessage bytesMessage;

        bytesMessage = new WampBytesMessage();
        assertTrue(bytesMessage.toString().startsWith("WampBytesMessage"));
    }
}
