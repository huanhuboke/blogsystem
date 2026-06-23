package com.geekcac.blogsystem.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CaptchaController {
    //验证码生成
    @GetMapping("/common/kaptcha")
    public void defaultCaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        //设置 浏览器不要缓存
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        //设置内容类型是image图片  png   ，以往  text/html 网页     application/json json格式数据
        httpServletResponse.setContentType("image/png");
        //hutool提供的                                             图片的宽       高       字符个数 4位      文字粗细
        ShearCaptcha shearCaptcha= CaptchaUtil.createShearCaptcha(150, 30, 4, 3);
        String code = shearCaptcha.getCode();
        // 验证码存入session      在登陆验证的controller里，可以从session（和用户一一对应的，每个人看到的都是自己的验证码） 中取出 生成验证码时候存入的验证码字符串
        httpServletRequest.getSession().setAttribute("verifyCode", shearCaptcha);

        // 输出图片流   ，把生成的图片（内存中生成的图片，以字节流的形式）写出到响应流
        shearCaptcha.write(httpServletResponse.getOutputStream());
    }
}
