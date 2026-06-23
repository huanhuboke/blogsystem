package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【tb_news】的数据库操作Mapper
 * @createDate 2024-10-03 11:37:01
 * @Entity com.geekcac.blogsystem.domain.News
 */

public interface NewsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(News record);

    int insertSelective(News record);

    News selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKey(News record);

    List<News> selectAll(@Param("keywords") String keywords);

    News selectById(Integer id);

    int deleteByIds(@Param("ids") Integer[] ids);

    int selectForeNewsCount();

    List<News> selectForeAllNews();

    List<News> selectNewsByTimeDesc();

    List<News> selectAllByViews();

    List<News> selectNewsByTagName(@Param("tagName") String tagName,@Param("start")int start,@Param("pageSize")int pageSize);

    int selectNewsCountByTagName(String tagName);

    News selectBySubUrl(String subUrl);
}
