package com.geekcac.blogsystem.controller.admin;

import com.geekcac.blogsystem.service.TagService;
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
public class TagsController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String index(Model model){
        model.addAttribute("path","tags");
        return "admin/tag";
    }

    @GetMapping("/tags/list")
    @ResponseBody
    public Result list(@RequestParam("page")Integer page, @RequestParam("limit")Integer limit){
        PageInfo pageInfo = tagService.showAll(page, limit);
        int totalCount = Integer.parseInt(String.valueOf(pageInfo.getTotal()));
        int totalPages = Integer.parseInt(String.valueOf(pageInfo.getPages()));
        System.out.println("分页信息"+totalCount+",,,,"+totalPages);
        LimitPage limitPage = new LimitPage(totalCount, limit, totalPages, page, pageInfo.getList());
        return ResultGenerator.genSuccessResult(limitPage);
    }

    @PostMapping("/tags/save")
    @ResponseBody
    public Result save(@RequestParam("tagName")String tagName){
        if(!StringUtils.hasText(tagName)){
            return ResultGenerator.genFailResult("标签名称不规范");
        }
        return ResultGenerator.genSuccessResult(tagService.save(tagName));
    }

    @PostMapping("/tags/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        int deleted = tagService.delAll(ids);
        if (deleted > 0){
            return ResultGenerator.genSuccessResult("删除成功");
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}
