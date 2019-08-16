package com.echostreams.jms.wamp.jndi;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.util.Hashtable;

public abstract class LazyCreateContext extends ReadOnlyContext {

    @SuppressWarnings("unused")
    public LazyCreateContext(Hashtable<?, ?> environment) {
        super(environment);
    }

    @SuppressWarnings("unchecked")
    public Object lookup(String name) throws NamingException {
        try {
            return super.lookup(name);
        } catch (NameNotFoundException e) {
            Object answer = createEntry(name);
            if (answer == null) {
                throw e;
            }
            internalBind(name, answer);
            return answer;
        }
    }

    protected abstract Object createEntry(String name);
}
