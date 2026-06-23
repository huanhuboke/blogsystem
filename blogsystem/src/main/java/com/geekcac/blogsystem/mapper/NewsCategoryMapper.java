package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.NewsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_news_category】的数据库操作Mapper
* @createDate 2024-10-03 11:37:01
* @Entity com.geekcac.blogsystem.domain.NewsCategory
*/
public interface NewsCategoryMapper {

    int deleteByPrimaryKey(Long id);

    int insert(NewsCategory record);

    int insertSelective(NewsCategory record);

    NewsCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NewsCategory record);

    int updateByPrimaryKey(NewsCategory record);

    List<NewsCategory> selectAl();

    int deleteByIds(@Param("ids") Integer[] ids);



}
