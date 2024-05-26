package com.heymart.balance.service;

public interface UserServiceClient {
    public boolean verifyOwnerIdIsOwner(String token, String ownerId, String OwnerType);
}
