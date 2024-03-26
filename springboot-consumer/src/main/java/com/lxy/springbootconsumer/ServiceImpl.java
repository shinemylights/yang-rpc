package com.lxy.springbootconsumer;


import com.lxy.common.model.User;
import com.lxy.common.service.UserService;
import com.lxy.yangrpcspringbootstarter.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * 示例服务实现类
 */
@Service
public class ServiceImpl {

    /**
     * 使用 Rpc 框架注入
     */
    @RpcReference
    private UserService userService;

    /**
     * 测试方法
     */
    public void test() {
        User user = new User();
        user.setName("wonyoung");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
