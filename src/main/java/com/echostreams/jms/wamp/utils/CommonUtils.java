package com.echostreams.jms.wamp.utils;

public class CommonUtils {

    public static byte[] copy( byte[] array )
    {
        byte[] result = new byte[array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        return result;
    }
}
