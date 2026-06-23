package com.geekcac.blogsystem.vo;

import com.geekcac.blogsystem.domain.NewsComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDetailVO {
    private Long newsId;

    private String newsTitle;

    private Integer newsCategoryId;

    private Integer commentCount;

    List<NewsComment> commentList;

    private String newsCategoryIcon;

    private String newsCategoryName;

    private String newsCoverImage;

    private Long newsViews;

    private List<String> newsTagList;

    private String newsContent;

    private Byte enableComment;

    private Date createTime;

    @Override
    public String toString() {
        return "BlogDetailVO{" +
                "newsId=" + newsId +
                ", newsTitle='" + newsTitle + '\'' +
                ", newsCategoryId=" + newsCategoryId +
                ", commentCount=" + commentCount +
                ", commentList=" + commentList +
                ", newsCategoryIcon='" + newsCategoryIcon + '\'' +
                ", newsCategoryName='" + newsCategoryName + '\'' +
                ", newsCoverImage='" + newsCoverImage + '\'' +
                ", newsViews=" + newsViews +
                ", newsTagList=" + newsTagList +
                ", newsContent='" + newsContent + '\'' +
                ", enableComment=" + enableComment +
                ", createTime=" + createTime +
                '}';
    }
}
