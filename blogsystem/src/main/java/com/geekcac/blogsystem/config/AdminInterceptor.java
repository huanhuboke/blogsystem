package com.geekcac.blogsystem.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //放行true 拦截false
        String requestURI = String.valueOf(request.getRequestURL());
        String servletPath = request.getServletPath();
        if(servletPath.startsWith("/admin")){
            //判断是否是管理员，从session中取值loginUserId，能得到值:是管理员，否则:不是管理员
            HttpSession session = request.getSession();
            Object loginUserId = session.getAttribute("loginUserId");
            if(loginUserId==null){
                request.getSession().setAttribute("errorMsg","请重新登陆");
                response.sendRedirect(request.getContextPath() + "/admin/login");
                return false;
            }else{
                return true;
            }
        }
        //其他非保护路径为true
        return true;
    }
}
