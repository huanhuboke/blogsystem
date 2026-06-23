package com.geekcac.blogsystem.controller.admin;

import com.geekcac.blogsystem.domain.NewsCategory;
import com.geekcac.blogsystem.service.CategoryService;
import com.geekcac.blogsystem.utils.LimitPage;
import com.geekcac.blogsystem.utils.Result;
import com.geekcac.blogsystem.utils.ResultGenerator;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String index(Model model){
        model.addAttribute("path","categories");
        return "admin/category";
    }

    @GetMapping("/categories/list")
    @ResponseBody
    public Result list(@RequestParam("page")Integer page,@RequestParam("limit")Integer limit){
        PageInfo pageInfo = categoryService.showAll(page, limit);
        int totalCount = Integer.parseInt(String.valueOf(pageInfo.getTotal()));
        int totalPages = Integer.parseInt(String.valueOf(pageInfo.getPages()));
        System.out.println("分页信息"+totalCount+",,,,"+totalPages);
        LimitPage limitPage = new LimitPage(totalCount, limit, totalPages, page, pageInfo.getList());
        return ResultGenerator.genSuccessResult(limitPage);
    }

    @PostMapping("/categories/save")
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName, @RequestParam("categoryIcon") String categoryIcon){
        if(!StringUtils.hasText(categoryName)){
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if(!StringUtils.hasText(categoryIcon)){
            return ResultGenerator.genFailResult("请输入分类图标！");
        }
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setCategoryIcon(categoryIcon);
        newsCategory.setCategoryName(categoryName);
        return ResultGenerator.genSuccessResult(categoryService.insert(newsCategory));
    }

    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(@RequestParam("categoryName") String categoryName, @RequestParam("categoryIcon") String categoryIcon,@RequestParam("categoryId")Integer categoryId){
        if(!StringUtils.hasText(categoryName)){
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if(!StringUtils.hasText(categoryIcon)){
            return ResultGenerator.genFailResult("请输入分类图标！");
        }
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setCategoryId(categoryId);
        newsCategory.setCategoryIcon(categoryIcon);
        newsCategory.setCategoryName(categoryName);
        return ResultGenerator.genSuccessResult(categoryService.update(newsCategory));
    }

    @PostMapping("/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        int deleted = categoryService.delete(ids);
        if (deleted > 0){
            return ResultGenerator.genSuccessResult("删除成功");
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}
