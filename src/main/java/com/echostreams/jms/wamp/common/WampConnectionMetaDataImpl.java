package com.echostreams.jms.wamp.common;



import java.util.Enumeration;
import java.util.NoSuchElementException;

public class WampConnectionMetaDataImpl implements javax.jms.ConnectionMetaData {
    @Override
    public int getJMSMajorVersion() {
        return WampJMSProviderVersion.getJMSMajorVersion();
    }

    @Override
    public int getJMSMinorVersion() {
        return WampJMSProviderVersion.getJMSMinorVersion();
    }

    @Override
    public String getJMSProviderName() {
        return "Apache Pulsar";
    }

    @Override
    public String getJMSVersion() {
        return WampJMSProviderVersion.getJMSMajorVersion() + "." + WampJMSProviderVersion.getJMSMinorVersion();
    }

    @Override
    public Enumeration<Object> getJMSXPropertyNames() {
        return new Enumeration<Object>() {

            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public Object nextElement() {
                throw new NoSuchElementException();
            }
        };
    }

    @Override
    public int getProviderMajorVersion() {
        return WampJMSProviderVersion.getProviderMajorVersion();
    }

    @Override
    public int getProviderMinorVersion() {
        return WampJMSProviderVersion.getProviderMinorVersion();
    }

    @Override
    public String getProviderVersion() {
        return WampJMSProviderVersion.getProviderMajorVersion() + "." + WampJMSProviderVersion.getProviderMinorVersion();
    }
}
