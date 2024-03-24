package com.lxy.yangrpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxy.yangrpc.model.RpcRequest;
import com.lxy.yangrpc.model.RpcResponse;


import java.io.IOException;

/**
 * Json 序列化器
 */
public class JsonSerializer implements Serializer {
    /*
    ObjectMapper 是 Jackson JSON处理库中的一个核心类。它提供了多种功能，主要包括：
    1.读取和写入JSON：它可以从字符串、文件或流中读取JSON，并将其转换为Java对象；反之亦然，可以将Java对象序列化为JSON字符串。
    2.数据绑定：支持将JSON数据绑定到Java对象，包括简单类型如int、String、复杂类型如用户自定义的POJO（Plain Old Java Object）。
    3.工厂API：ObjectMapper 可以创建ObjectReader和ObjectWriter，这两个类提供了更高级的读写API。
    */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> classType) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, classType);
        if (obj instanceof RpcRequest) {
            return handleRequest((RpcRequest) obj, classType);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse((RpcResponse) obj, classType);
        }
        return obj;
    }

    /**
     * 由于 Object 的原始对象会被擦除，导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     *
     * 在反序列化过程中，原始类型信息丢失是一个常见的问题，尤其是在处理多态性时。这通常发生在以下情况：
     * 类型擦除：在Java中，泛型信息在编译时会被擦除，这意味着在运行时，泛型类型的具体信息不可用。因此，当反序列化泛型类型的对象时，
     * 如果没有额外的类型信息，反序列化的结果可能不是预期的具体类型，而是某个更通用的类型，如LinkedHashMap。
     *
     * 为了解决这个问题，可以采取以下措施：
     * 包含类型信息：在序列化时，将对象的类型信息一起序列化，这样在反序列化时就可以使用这些信息来正确地还原对象。
     * 自定义序列化器：实现自定义的序列化器和反序列化器，以处理特定类型的转换逻辑。
     *
     * @param rpcRequest rpc 请求
     * @param type       类型
     * @return {@link T}
     * @throws IOException IO异常
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        // 循环处理每个参数的类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            // 如果类型不同，则重新处理一下类型
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argBytes, clazz);
            }
        }
        return type.cast(rpcRequest);
    }

    /**
     * 由于 Object 的原始对象会被擦除，导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     *
     * @param rpcResponse rpc 响应
     * @param type        类型
     * @return {@link T}
     * @throws IOException IO异常
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        // 处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes, rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }
}
