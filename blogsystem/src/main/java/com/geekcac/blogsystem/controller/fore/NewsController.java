package com.geekcac.blogsystem.controller.fore;

import cn.hutool.captcha.ShearCaptcha;
import com.geekcac.blogsystem.domain.Link;
import com.geekcac.blogsystem.domain.News;
import com.geekcac.blogsystem.domain.NewsComment;
import com.geekcac.blogsystem.domain.NewsTag;
import com.geekcac.blogsystem.service.*;
import com.geekcac.blogsystem.utils.*;
import com.geekcac.blogsystem.vo.BlogDetailVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class NewsController {
    public static String theme = "amaze";
    @Autowired
    private ConfigService configService;
    @Autowired
    private TagService tagService;

    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LinkService linkService;

    private static Integer pageSize = 8;

    private static Integer favoriteLinks = 0;

    private static Integer recommendLinks = 1;

    private static Integer personalLinks = 2;



    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    /**
     * 前台首页 获取新闻列表
     *
     * @param request
     * @param pageNum
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    private String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        int foreNewsCount = newsService.getForeNewsCount();
        //2
        PageInfo foreNewsList = newsService.getForeNewsList(pageNum, pageSize);
        //如何把4个数据返回给前端？
//        PageResult pageResult = new PageResult(foreNewsList.getList(), Integer.parseInt(String.valueOf(foreNewsList.getTotal())), pageNum, 1);
        int totalCount = Integer.parseInt(String.valueOf(foreNewsList.getTotal()));
        int totalPages = Integer.parseInt(String.valueOf(foreNewsList.getPages()));
        LimitPage limitPage = new LimitPage(totalCount,pageSize,totalPages,pageNum,foreNewsList.getList());

        //按照发布时间倒序 查询 新闻列表 10
        List<News> top10LatestNews = newsService.getTop10LatestNews();
        //倒序查询10条点击量的新闻
        List<News> top10LatestViews = newsService.selectAllByViews();
        List<NewsTag> newsTags = tagService.selectAllTagNewsCnt();

        request.setAttribute("blogPageResult", limitPage);
        request.setAttribute("newBlogs", top10LatestNews);//最新发布

        request.setAttribute("hotBlogs", top10LatestViews);//点击最多
//        热门标签 需要的结构List<NewsTag>
        request.setAttribute("hotTags", newsTags);//热门标签
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/index";
    }

    //纯粹的接口，只返回数据
    @GetMapping("/fore/newsList")
    //Springboot会自动的把List转换为JSON 数组
    @ResponseBody
    public PageResult foreNewsList(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        /**
         * 1,查询总记录条数 totalCount(前端网页 用来进行分页计算)
         * 2, 根据 页码和 页内记录数查询 某一页的数据
         */
        int foreNewsCount = newsService.getForeNewsCount();
        //2
        PageInfo newsPage = newsService.getForeNewsList(pageNo, pageSize);
        //如何把4个数据返回给前端？
        PageResult pageResult = new PageResult(newsPage.getList(), Integer.parseInt(String.valueOf(newsPage.getTotal())), pageSize, pageNo);
        return pageResult;
    }

    /**
     * 根据id查看详情
     *
     * @param req
     * @param id
     * @return
     */
    @GetMapping("/blog/{id}")
    public String detail(HttpServletRequest req, @PathVariable("id") Integer id) {
        /**
         * 这里新闻标签
         *
         * 使用 新闻和标签表 关联方式 查询
         *
         * 把新闻表中冗余存储的标签 那个列忽略掉，或者删掉
         */
        News newsDetail = newsService.getById(id);
        String newsTags = newsDetail.getNewsTags();
        String[] tagArr = newsTags.split(",");
        Collections.addAll(newsDetail.getNewsTagList(), tagArr);
        if (newsDetail != null) {
            req.setAttribute("blogDetailVO", newsDetail);
        }
        //因为页面不保存配置信息，需要再次发送一遍
        req.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/detail";
    }

    /**
     * 评论操作
     *
     * @param request
     * @param session
     * @param newsId      针对哪个新闻
     * @param verifyCode  验证码
     * @param commentator 评论者名字
     * @param email
     * @param websiteUrl
     * @param commentBody 评论内容
     * @return
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long newsId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam(required = false) String websiteUrl, @RequestParam String commentBody) {
        System.out.println("sdsds"+websiteUrl);
        if (!StringUtils.hasText(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        //从session中取出 生成验证码时候存入的code值
        ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute("verifyCode");
        if (shearCaptcha == null || !shearCaptcha.verify(verifyCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        //保护作用，防止你在别的新闻页面提交针对这个新闻的的评论
        String ref = request.getHeader("Referer");
        if (!StringUtils.hasText(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == newsId || newsId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!StringUtils.hasText(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (!StringUtils.hasText(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (!StringUtils.hasText(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }

        NewsComment comment = new NewsComment();
        comment.setNewsId(newsId);
        comment.setCommentator(MyBlogUtils.cleanString(commentator));
        comment.setEmail(email);
        comment.setCommentatorIp(getIP(request));
        if(StringUtils.hasText(websiteUrl)) {
            if (PatternUtil.isURL(websiteUrl)) {
                comment.setWebsiteUrl(websiteUrl);
            } else {
                return ResultGenerator.genFailResult("URL格式错误");
            }
        }
        comment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(commentService.addComment(comment));
    }


    public String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/tag/{tagName}"})
    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName) {
        return tag(request, tagName, 1);
    }

    /**
     * 标签 下的文章列表
     * 带有分页
     *
     * @return
     */
    @GetMapping({"/tag/{tagName}/{page}"})
    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName, @PathVariable("page") Integer page) {
        PageResult blogPageResult = newsService.getNewsByTagName(tagName, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "标签");
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", tagName);
        request.setAttribute("newBlogs", newsService.getTop10LatestNews());
        request.setAttribute("hotBlogs", Collections.emptyList());
        request.setAttribute("hotTags", tagService.selectAllTagNewsCnt());
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/list";
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        return search(request, keyword, 1);
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {

        LimitPage pageResult = newsService.getBlogsPageBySearch(keyword, page);
        request.setAttribute("blogPageResult", pageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        //按照发布时间倒序 查询 新闻列表 10
        List<News> top10LatestNews = newsService.getTop10LatestNews();
        List<NewsTag> newsTags = tagService.selectAllTagNewsCnt();
        request.setAttribute("newBlogs", top10LatestNews);//最新发布

        request.setAttribute("hotBlogs", newsService.selectAllByViews());//点击最多
//        热门标签 需要的结构List<NewsTag>
        request.setAttribute("hotTags", newsTags);//热门标签
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/list";
    }

    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("pageName", "友情链接");
        Map<Integer, List<Link>> links = linkService.getLinks();
        if(links!=null){
            if(links.containsKey(favoriteLinks)){
                model.addAttribute("favoriteLinks",links.get(favoriteLinks));
            }
            if(links.containsKey(recommendLinks)){
                model.addAttribute("recommendLinks",links.get(recommendLinks));
            }
            if(links.containsKey(personalLinks)){
                model.addAttribute("personalLinks",links.get(personalLinks));
            }
        }
        model.addAttribute("configurations",configService.getAllConfigs());
        return "blog/" + theme + "/link";
    }


    @GetMapping({"/{subUrl}"})
    public String detail(HttpServletRequest request, @PathVariable("subUrl") String subUrl) {
        BlogDetailVO blogDetailVO = newsService.getBlogDetailBySubUrl(subUrl);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations", configService.getAllConfigs());
            return "blog/" + theme + "/detail";
        }
        return "error/error_400";
    }
}
