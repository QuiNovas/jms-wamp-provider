package com.echostreams.jms.wamp.jndi;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class JNDIStorable implements Referenceable, Externalizable {

    /**
     * Set the properties that will represent the instance in JNDI
     *
     * @param props
     *      The properties to use when building the new isntance.
     *
     * @return a new, unmodifiable, map containing any unused properties, or empty if none were.
     */
    protected abstract Map<String, String> buildFromProperties(Map<String, String> props);

    /**
     * Initialize the instance from properties stored in JNDI
     *
     * @param props
     *      The properties to use when initializing the new instance.
     */
    protected abstract void populateProperties(Map<String, String> props);

    /**
     * set the properties for this instance as retrieved from JNDI
     *
     * @param props
     *      The properties to apply to this instance.
     *
     * @return a new, unmodifiable, map containing any unused properties, or empty if none were.
     */
    public synchronized Map<String, String> setProperties(Map<String, String> props) {
        return buildFromProperties(props);
    }

    /**
     * Get the properties from this instance for storing in JNDI
     *
     * @return the properties
     */
    public synchronized Map<String, String> getProperties() {
        Map<String, String> properties = new LinkedHashMap<>();
        populateProperties(properties);
        return properties;
    }

    /**
     * Retrieve a Reference for this instance to store in JNDI
     *
     * @return the built Reference
     * @throws NamingException
     *         if error on building Reference
     */
    @Override
    public Reference getReference() throws NamingException {
        return com.echostreams.jms.wamp.jndi.JNDIReferenceFactory.createReference(this.getClass().getName(), this);
    }

    /**
     * @see Externalizable#readExternal(ObjectInput)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Map<String, String> props = (Map<String, String>) in.readObject();
        if (props != null) {
            setProperties(props);
        }
    }

    /**
     * @see Externalizable#writeExternal(ObjectOutput)
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getProperties());
    }

    protected String getProperty(Map<String, String> map, String key, String defaultValue) {
        String value = map.get(key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

}
