package com.echostreams.jms.wamp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializer<T> implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectSerializer.class);

    private static final long serialVersionUID = -310657388999555600L;

    public ObjectSerializer() {

    }

    public byte[] objectToByteArray(T paramT) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
            out.writeObject(paramT);
            out.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to serialize object.", e);
            throw new RuntimeException("Failed to serialize object.", e);
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                // ignore close exception
            }
        }
        return baos.toByteArray();
    }

    /*public T byteArrayToObject(Message<byte[]> message) {
        ByteArrayInputStream bais = new ByteArrayInputStream(message.getData());
        try (ObjectInputStream in = new ObjectInputStream(bais)) {
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Failed to serialize object.", e);
            throw new RuntimeException("Failed to deserialize object.", e);
        }
    }*/
}
