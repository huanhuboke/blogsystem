package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.News;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class DaoTest {
    @Autowired
    private NewsMapper newsMapper;

    @Test
    public void testNews() {
        News news = newsMapper.selectByPrimaryKey(5L);
        Assert.notNull(news,"为空");
    }
}
