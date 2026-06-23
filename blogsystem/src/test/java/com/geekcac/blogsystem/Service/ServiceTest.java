package com.geekcac.blogsystem.Service;

import com.geekcac.blogsystem.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private NewsService newsService;

    @Test
    public void testNews() {
        newsService.getBlogDetailBySubUrl("about");
    }
}
