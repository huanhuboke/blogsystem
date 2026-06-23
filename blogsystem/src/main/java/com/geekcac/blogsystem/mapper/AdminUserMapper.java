package com.geekcac.blogsystem.mapper;

import com.geekcac.blogsystem.domain.AdminUser;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【tb_admin_user】的数据库操作Mapper
* @createDate 2024-10-03 11:37:01
* @Entity com.geekcac.blogsystem.domain.AdminUser
*/

public interface AdminUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);
    //查询用户名和密码
    AdminUser selectByUsernamePasswd(AdminUser adminUser);

}
