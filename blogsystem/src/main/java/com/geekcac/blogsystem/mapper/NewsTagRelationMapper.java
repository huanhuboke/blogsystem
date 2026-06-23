package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.NewsTagRelation;

/**
* @author Administrator
* @description 针对表【tb_news_tag_relation】的数据库操作Mapper
* @createDate 2024-10-03 11:37:01
* @Entity com.geekcac.blogsystem.domain.NewsTagRelation
*/
public interface NewsTagRelationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(NewsTagRelation record);

    int insertSelective(NewsTagRelation record);

    NewsTagRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsTagRelation record);

    int updateByPrimaryKey(NewsTagRelation record);

}
