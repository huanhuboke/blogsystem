package com.geekcac.blogsystem.service;

import com.geekcac.blogsystem.domain.NewsCategory;
import com.geekcac.blogsystem.mapper.NewsCategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final NewsCategoryMapper newsCategoryMapper;

    public List<NewsCategory> getAllCategories() {
        return newsCategoryMapper.selectAl();
    }

    public PageInfo showAll(Integer page,Integer pageSize){
        PageHelper.startPage(page,pageSize);
        List<NewsCategory> newsCategories = newsCategoryMapper.selectAl();
        PageInfo<NewsCategory> categoryPageInfo = new PageInfo<>(newsCategories);
        return categoryPageInfo;
    }

    public int update (NewsCategory newsCategory){
        return newsCategoryMapper.updateByPrimaryKeySelective(newsCategory);
    }

    public int insert(NewsCategory newsCategory){
        return newsCategoryMapper.insertSelective(newsCategory);
    }

    public int delete(Integer[] ids){
        return newsCategoryMapper.deleteByIds(ids);
    }
}
