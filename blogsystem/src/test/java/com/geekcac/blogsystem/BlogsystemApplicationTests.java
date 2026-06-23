package com.geekcac.blogsystem;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogsystemApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testMD5() {
        String s = SecureUtil.md5("123456");
        System.out.println(s);
    }
}
