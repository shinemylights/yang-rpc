package com.lxy.springbootprovider;


import com.lxy.common.model.User;
import com.lxy.common.service.UserService;
import com.lxy.yangrpcspringbootstarter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
