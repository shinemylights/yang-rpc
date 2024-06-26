package com.lxy.provider;

import com.lxy.common.service.UserService;
import com.lxy.yangrpc.bootstrap.ProviderBootstrap;
import com.lxy.yangrpc.model.ServiceRegisterInfo;


import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者示例
 */
public class ProviderExample {

    // region http
    // public static void main(String[] args) {
    //     //RPC框架初始化
    //     RpcApplication. init();
    //     //注册服务
    //     String serviceName = UserService. class. getName() ;
    //     LocalRegistry. register (serviceName, UserServiceImpl. class) ;
    //     //注册服务到注册中心
    //     RpcConfig rpcConfig = RpcApplication. getRpcConfig() ;
    //     RegistryConfig registryConfig = rpcConfig. getRegistryConfig() ;
    //     Registry registry =
    //             RegistryFactory. getInstance (registryConfig. getRegistry());
    //     ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo() ;
    //     serviceMetaInfo. setServiceName (serviceName) ;
    //     serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
    //     serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
    //     try {
    //         registry. register (serviceMetaInfo) ;
    //     } catch (Exception e) {
    //         throw new RuntimeException(e) ;
    //     }
    //
    //     //启动web服务
    //     HttpServer httpServer = new VertxHttpServer() ;
    //     httpServer. doStart (RpcApplication. getRpcConfig (). getServerPort());
    // }
    // endregion

    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }


}

