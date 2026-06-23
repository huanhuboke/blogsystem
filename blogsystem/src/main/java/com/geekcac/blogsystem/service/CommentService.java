package com.geekcac.blogsystem.service;

import com.geekcac.blogsystem.domain.NewsComment;
import com.geekcac.blogsystem.mapper.NewsCommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final NewsCommentMapper newsCommentMapper;


    public int addComment(NewsComment comment) {
        return newsCommentMapper.insert(comment);
    }

    public PageInfo getAll(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<NewsComment> commentList = newsCommentMapper.selectAll();
        PageInfo<NewsComment> newsCommentPageInfo = new PageInfo<>(commentList);
        return newsCommentPageInfo;
    }

    public int batchComments(Integer[] ids) {
       return newsCommentMapper.updateBatch(ids);
    }

    public int deleteByIds(Integer[] ids) {
        return newsCommentMapper.deleteByIds(ids);
    }

    public int replyComments(Integer commentId,String replyBody){
        return newsCommentMapper.updateCommentReplyById(commentId,replyBody);
    }
}
