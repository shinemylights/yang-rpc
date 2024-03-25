package com.lxy.consumer;


import com.lxy.common.model.User;
import com.lxy.common.service.UserService;
import com.lxy.yangrpc.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {


    public static void main(String[] args) {
        // 静态代理
        //UserService userService = new UserServiceProxy();
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("wonyoung");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }

        // System.out.println(userService.getUser(user).getName());
        // System.out.println(userService.getUser(user).getName());
        // System.out.println(userService.getUser(user).getName());

    }
}
