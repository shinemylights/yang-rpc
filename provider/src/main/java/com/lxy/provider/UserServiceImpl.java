package com.lxy.provider;


import com.lxy.common.model.User;
import com.lxy.common.service.UserService;

/**
 * 用户服务实现类
 */
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        user.setName("love "+user.getName());
        return user;
    }
}
