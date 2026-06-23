package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.NewsTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_news_tag】的数据库操作Mapper
* @createDate 2024-10-03 11:37:01
* @Entity com.geekcac.blogsystem.domain.NewsTag
*/
public interface NewsTagMapper {

    int deleteByPrimaryKey(Long id);

    int insert(NewsTag record);

    int insertSelective(NewsTag record);

    NewsTag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsTag record);

    int updateByPrimaryKey(NewsTag record);

    List<NewsTag> selectAllTagNewsCnt();

    /**
     * 根据新闻ID删除
     *
     * @param newsIds
     * @return
     */
    int deleteByNewsIds(Integer[] newsIds);

    int deleteByIds(@Param("ids") Integer[] ids);

    List<NewsTag> querAll();


}
