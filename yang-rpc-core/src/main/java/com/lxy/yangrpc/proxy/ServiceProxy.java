package com.lxy.yangrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lxy.yangrpc.RpcApplication;
import com.lxy.yangrpc.config.RpcConfig;
import com.lxy.yangrpc.constant.RpcConstant;
import com.lxy.yangrpc.fault.retry.RetryStrategy;
import com.lxy.yangrpc.fault.retry.RetryStrategyFactory;
import com.lxy.yangrpc.loadbalancer.LoadBalancer;
import com.lxy.yangrpc.loadbalancer.LoadBalancerFactory;
import com.lxy.yangrpc.model.RpcRequest;
import com.lxy.yangrpc.model.RpcResponse;
import com.lxy.yangrpc.model.ServiceMetaInfo;
import com.lxy.yangrpc.registry.Registry;
import com.lxy.yangrpc.registry.RegistryFactory;
import com.lxy.yangrpc.serializer.Serializer;
import com.lxy.yangrpc.serializer.SerializerFactory;
import com.lxy.yangrpc.server.tcp.VertxTcpClient;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务代理（JDK 动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }

            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

            // 发送http请求
            // try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
            //         .body(bodyBytes)
            //         .execute()) {
            //     byte[] result = httpResponse.bodyBytes();
            //     // 反序列化
            //     RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);

            //重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(() ->
                    //发送TCP请求
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );

            return rpcResponse.getData();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
