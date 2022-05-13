package com.yxg.blog.service;

import com.yxg.blog.pojo.User;

public interface UserService {
    public User checkUser(String username,String password);
}
