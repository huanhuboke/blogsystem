package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.Config;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tb_config】的数据库操作Mapper
* @createDate 2024-10-03 11:37:01
* @Entity com.geekcac.blogsystem.domain.Config
*/

public interface ConfigMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Config record);

    int insertSelective(Config record);

    Config selectByPrimaryKey(String configName);

    int updateByPrimaryKeySelective(Config record);

    int updateByPrimaryKey(Config record);

    List<Config> selectALL();
}
