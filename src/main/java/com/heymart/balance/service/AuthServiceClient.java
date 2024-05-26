package com.heymart.balance.service;

public interface AuthServiceClient {
    public boolean verifyUserAuthorization(String action, String AuthorizationHeader);
}
