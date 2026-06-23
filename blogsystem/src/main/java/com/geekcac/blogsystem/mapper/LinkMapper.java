package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.Link;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_link】的数据库操作Mapper
* @createDate 2024-10-03 11:37:01
* @Entity com.geekcac.blogsystem.domain.Link
*/
public interface LinkMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Link record);

    int insertSelective(Link record);

    Link selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Link record);

    int updateByPrimaryKey(Link record);

    List<Link> findLinkList();

}
