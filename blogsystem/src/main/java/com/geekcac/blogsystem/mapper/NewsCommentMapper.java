package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.NewsComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【tb_news_comment】的数据库操作Mapper
* @createDate 2024-10-03 11:37:01
* @Entity com.geekcac.blogsystem.domain.NewsComment
*/
public interface NewsCommentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(NewsComment record);

    int insertSelective(NewsComment record);

    NewsComment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsComment record);

    int updateByPrimaryKey(NewsComment record);

    List<NewsComment> selectAll();

    int updateBatch(Integer[] ids);

    int deleteByIds(@Param("ids") Integer[] ids);

    int updateCommentReplyById(@Param("commentId")Integer commentId,@Param("replyBody") String replyBody);

    int getTotalNewsComments(Map paramMap);

    List<NewsComment> queryByNewsId(@Param("newsId") Integer newsId);

}
