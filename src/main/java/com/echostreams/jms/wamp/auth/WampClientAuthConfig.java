package com.echostreams.jms.wamp.auth;

public class WampClientAuthConfig {

    private static String authType;
    private static String authId;
    private static String authKey;
    private static String authPublicKey;

    public WampClientAuthConfig() {
    }

    public WampClientAuthConfig(String authType, String authId, String authKey) {
        WampClientAuthConfig.authType = authType;
        WampClientAuthConfig.authId = authId;
        WampClientAuthConfig.authKey = authKey;
    }

    public WampClientAuthConfig(String authType, String authId, String authKey, String authPublicKey) {
        new WampClientAuthConfig(authType, authId, authKey);
        WampClientAuthConfig.authPublicKey = authPublicKey;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        WampClientAuthConfig.authType = authType;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        WampClientAuthConfig.authId = authId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        WampClientAuthConfig.authKey = authKey;
    }

    public String getAuthPublicKey() {
        return authPublicKey;
    }

    public void setAuthPublicKey(String authPublicKey) {
        WampClientAuthConfig.authPublicKey = authPublicKey;
    }

    @Override
    public String toString() {
        return "WampClientAuthConfig{" +
                "authType='" + authType + '\'' +
                ", authId='" + authId + '\'' +
                ", authKey='" + authKey + '\'' +
                ", authPublicKey='" + authPublicKey + '\'' +
                '}';
    }
}
