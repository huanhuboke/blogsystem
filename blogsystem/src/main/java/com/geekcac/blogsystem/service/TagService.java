package com.geekcac.blogsystem.service;

import com.geekcac.blogsystem.domain.NewsTag;
import com.geekcac.blogsystem.mapper.NewsTagMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final NewsTagMapper newsTagMapper;

    public List<NewsTag> selectAllTagNewsCnt() {
        return newsTagMapper.selectAllTagNewsCnt();
    }

    public int deleteTagById(Integer tagId){
        return 0;
    }

    public PageInfo showAll(Integer page,Integer pageSize){
        PageHelper.startPage(page,pageSize);
        List<NewsTag> newsTags = newsTagMapper.querAll();
        return new PageInfo<>(newsTags);
    }

    public int save(String tagName){
        NewsTag newsTag = new NewsTag();
        newsTag.setTagName(tagName);
        int i = newsTagMapper.insertSelective(newsTag);
        return i;
    }

    public int delAll(Integer[] ids){
        return newsTagMapper.deleteByIds(ids);
    }
}
