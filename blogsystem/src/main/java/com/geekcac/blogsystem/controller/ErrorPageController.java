package com.geekcac.blogsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 自定义错误试图，实现默认的错误试图ErrorViewResolver，改试图默认只对返回为JSP,HTML有效，
 * 如果想把返回试图改成返回json也可以改
 */
@Controller
public class ErrorPageController implements ErrorViewResolver {

    private static ErrorPageController errorPageController;
    //错误属性
    @Autowired
    private ErrorAttributes errorAttributes;

    public ErrorPageController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    public ErrorPageController() {
        if (errorPageController == null) {
            errorPageController = new ErrorPageController(errorAttributes);
        }
    }

    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        if (HttpStatus.BAD_REQUEST == status) {
            return new ModelAndView("error/error_400");
        }
        if (HttpStatus.NOT_FOUND == status) {
            return new ModelAndView("error/error_404");
        }
        return new ModelAndView("error/error_5xx");
    }
}
