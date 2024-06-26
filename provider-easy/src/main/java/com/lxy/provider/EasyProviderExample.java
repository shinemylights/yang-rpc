package com.lxy.provider;


import com.lxy.common.service.UserService;
import com.lxy.yangrpc.server.HttpServer;
import com.lxy.yangrpc.server.VertxHttpServer;
import com.lxy.yangrpc.registry.LocalRegistry;


/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
