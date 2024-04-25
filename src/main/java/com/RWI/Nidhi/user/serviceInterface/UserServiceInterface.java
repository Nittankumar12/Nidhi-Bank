package com.RWI.Nidhi.user.serviceInterface;

public interface UserServiceInterface {
    String getEmail(int userId);
    int getAccountIdByUserEmail(String email);
}