package com.flarze.hashstash.data.instagram_login;

public interface AuthenticationListener {
    void onTokenReceived(String auth_token);
}
