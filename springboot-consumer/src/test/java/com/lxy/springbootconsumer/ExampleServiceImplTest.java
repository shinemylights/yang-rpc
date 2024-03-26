package com.lxy.springbootconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 单元测试
 */
@SpringBootTest
class ExampleServiceImplTest {

    @Resource
    private ServiceImpl exampleService;

    @Test
    void test1() {
        exampleService.test();
    }
}