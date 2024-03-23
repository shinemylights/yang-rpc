package com.lxy.yangrpc.server;

/**
 * HTTP 服务器接口
 * 编写一个web服务器的接口HttpServer,定义统一的启动服务器方法,便于后续的扩展，比如
 * 实现多种不同的web服务器。
 */
public interface HttpServer {

    /**
     * 启动服务器
     *
     * @param port
     */
    void doStart(int port);
}
