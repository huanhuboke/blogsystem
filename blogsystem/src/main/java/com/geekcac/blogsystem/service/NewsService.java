package com.geekcac.blogsystem.service;

import com.geekcac.blogsystem.domain.News;
import com.geekcac.blogsystem.domain.NewsCategory;
import com.geekcac.blogsystem.domain.NewsComment;
import com.geekcac.blogsystem.mapper.NewsCategoryMapper;
import com.geekcac.blogsystem.mapper.NewsCommentMapper;
import com.geekcac.blogsystem.mapper.NewsMapper;
import com.geekcac.blogsystem.mapper.NewsTagMapper;
import com.geekcac.blogsystem.utils.LimitPage;
import com.geekcac.blogsystem.utils.MarkDownUtil;
import com.geekcac.blogsystem.utils.PageResult;
import com.geekcac.blogsystem.vo.BlogDetailVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsMapper newsMapper;

    private final NewsTagMapper newsTagMapper;

    private final NewsCategoryMapper categoryMapper;

    private final NewsCommentMapper newsCommentMapper;

    public int getAllNewsCount(String keyword) {
        return 0;
    }

    public PageInfo getAllNews(Integer page, Integer limit, String keyword) {
        PageHelper.startPage(page,limit);
        List<News> newsList =  newsMapper.selectAll(keyword);
        PageInfo<News> newsPage = new PageInfo<>(newsList);
        return newsPage;
    }


    public News getById(Integer id) {
       return newsMapper.selectById(id);
    }

    public int updateById(News news) {
        return newsMapper.updateByPrimaryKey(news);
    }

    public int addNews(News news) {
        return newsMapper.insert(news);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Integer[] ids) {
        int deltags = newsTagMapper.deleteByNewsIds(ids);
        int delNews = newsMapper.deleteByIds(ids);
        return deltags + delNews;
    }

    public PageInfo getForeNewsList(Integer page, Integer limit){
        PageHelper.startPage(page,limit);
        List<News> newsList = newsMapper.selectForeAllNews();
        PageInfo<News> newsPage = new PageInfo<>(newsList);
        return newsPage;
    }

    public int getForeNewsCount() {
        return newsMapper.selectForeNewsCount();
    }

    public List<News> getTop10LatestNews() {
        return newsMapper.selectNewsByTimeDesc();
    }

    public List<News> selectAllByViews(){
        return newsMapper.selectAllByViews();
    }

    public PageResult getNewsByTagName(String tagName, Integer pageNo){
        int count = newsMapper.selectNewsCountByTagName(tagName);
        int pageSize = 10;
        int start = (pageNo - 1) * pageSize;
        List<News> newsList = newsMapper.selectNewsByTagName(tagName,start, pageSize);
        PageResult pageResult = new PageResult(newsList, count, pageSize, pageNo);
        return pageResult;
    }

    public LimitPage getBlogsPageBySearch(String keyword, Integer page) {
        PageHelper.startPage(page,8);
        List<News> newsList = newsMapper.selectAll(keyword);
        PageInfo<News> newsPageInfo = new PageInfo<>(newsList);
        int totalCount = Integer.parseInt(String.valueOf(newsPageInfo.getTotal()));
        int totalPages = Integer.parseInt(String.valueOf(newsPageInfo.getPages()));
        return new LimitPage(totalCount,8,totalPages,page,newsPageInfo.getList());
    }

    public BlogDetailVO getBlogDetailBySubUrl(String subUrl) {
        News news = newsMapper.selectBySubUrl(subUrl);
        BlogDetailVO blogDetailVO = getBlogDetailVO(news);
        if (blogDetailVO != null)
            return blogDetailVO;
        return null;
    }

    private BlogDetailVO getBlogDetailVO(News news) {
        if (news != null && news.getNewsStatus() == 1) {
            news.setNewsViews(Long.valueOf(news.getNewsViews().longValue() + 1L));
            newsMapper.updateByPrimaryKey(news);
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(news, blogDetailVO);
            blogDetailVO.setNewsContent(MarkDownUtil.mdToHtml(blogDetailVO.getNewsContent()));
            NewsCategory newsCategory = categoryMapper.selectByPrimaryKey(news.getNewsCategoryId());
            if (newsCategory == null) {
                newsCategory = new NewsCategory();
                newsCategory.setCategoryId(Integer.valueOf(0));
                newsCategory.setCategoryName("默认分类");
                newsCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            blogDetailVO.setNewsCategoryIcon(newsCategory.getCategoryIcon());
            if (StringUtils.hasText(news.getNewsTags())) {
                List<String> tags = Arrays.asList(news.getNewsTags().split(","));
                blogDetailVO.setNewsTagList(tags);
            }
            List<NewsComment> list = news.getCommentList();
            for(NewsComment newss :list){
                System.out.println("11111111111111111111111111111111111"+newss);
            }
            blogDetailVO.setCommentList(news.getCommentList());
//            System.out.println("sssssssss"+blogDetailVO);
            return blogDetailVO;
        }
        return null;
    }
}
