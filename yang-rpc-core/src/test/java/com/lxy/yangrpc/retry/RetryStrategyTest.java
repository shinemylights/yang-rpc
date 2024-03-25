package com.lxy.yangrpc.retry;

import com.lxy.yangrpc.fault.retry.FixedIntervalRetryStrategy;
import com.lxy.yangrpc.fault.retry.NoRetryStrategy;
import com.lxy.yangrpc.fault.retry.RetryStrategy;

import com.lxy.yangrpc.model.RpcResponse;
import org.junit.Test;

/**
 * 重试策略测试
 */
public class RetryStrategyTest {

    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}