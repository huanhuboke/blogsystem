package com.geekcac.blogsystem.controller.admin;

import cn.hutool.captcha.ShearCaptcha;
import com.geekcac.blogsystem.domain.AdminUser;
import com.geekcac.blogsystem.service.AdminUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminUerController {

    @Autowired
    private AdminUserService adminUserService;

    //要访问thymeleaf制作的页面时候，要首先先访问控制器，
    //然后由控制器帮咱们跳转到页面，不能直接访问页面
    @GetMapping("/login")
    public String login(){
        System.out.println("方法进来了");
        //指向页面
        return "/admin/login";
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("userName")String userName, @RequestParam("password")String password, @RequestParam("verifyCode")String verifyCode, HttpSession session){
        /**
         *
         * 1，验证输入信息是否为空
         *    如果为空 ，要在session中保存错误信息，返回登录页
         * 2，调用service 验证登陆
         *      登陆成功 跳转页面进入后台首页
         *      return "redirect:/admin/index";
         */
        if (userName == null || "".equals(userName.trim())){
            session.setAttribute("errorMsg", "用户名不能为空");
            return "redirect:/admin/login";
        }
        if (password == null || "".equals(password.trim())){
            session.setAttribute("errorMsg", "密码不能为空");
            return "redirect:/admin/login";
        }
        if (verifyCode == null || "".equals(verifyCode.trim())){
            session.setAttribute("errorMsg", "验证码不能为空");
            return "redirect:/admin/login";
        }
        ShearCaptcha verifyCode1 = (ShearCaptcha) session.getAttribute("verifyCode");
        if (!verifyCode.equals(verifyCode1.getCode())){
            //如果用户输入的验证码，和之前生成后保存的 不一致
            session.setAttribute("errorMsg", "验证码错误");
            return "redirect:/admin/login";
        }
        AdminUser adminUser = new AdminUser();
        adminUser.setLoginUserName(userName);
        adminUser.setLoginPassword(password);
        AdminUser loginUser = adminUserService.login(adminUser);
        if (loginUser != null){
            //登陆成功
            session.setAttribute("loginUser", loginUser.getNickName());
            session.setAttribute("loginUserId", loginUser.getAdminUserId());
            return "redirect:/admin/index";
        }else {
            session.setAttribute("errorMsg", "登陆失败");
            return "redirect:/admin/login";
        }
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        //类别数量  ctrl  + Alt 鼠标左键点击，跳转到函数实现
        //todo: 文章类别数量  单独的一个查询
        request.setAttribute("categoryCount", 0);
        //新闻总数
        request.setAttribute("blogCount", 200);
        //链接总数
        request.setAttribute("linkCount", 0);
        //标签总数
        request.setAttribute("tagCount", 0);
        //评论总数
        request.setAttribute("commentCount", 0);
        return "admin/index";
    }
}
