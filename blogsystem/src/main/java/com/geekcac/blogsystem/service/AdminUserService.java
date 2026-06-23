package com.geekcac.blogsystem.service;

import cn.hutool.crypto.SecureUtil;
import com.geekcac.blogsystem.domain.AdminUser;
import com.geekcac.blogsystem.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserMapper adminUserMapper;


    public AdminUser login(AdminUser adminUser) {
        //需要把用户输入的密码，md5(用户输入的密码),然后再和DB中比对
        String md5Password = SecureUtil.md5(adminUser.getLoginPassword());
        adminUser.setLoginPassword(md5Password);
        AdminUser user = adminUserMapper.selectByUsernamePasswd(adminUser);
        return user;
    }
}
