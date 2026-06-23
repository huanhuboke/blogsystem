package com.geekcac.blogsystem.controller.admin;

import com.geekcac.blogsystem.domain.NewsComment;
import com.geekcac.blogsystem.service.CommentService;
import com.geekcac.blogsystem.utils.PageResult;
import com.geekcac.blogsystem.utils.Result;
import com.geekcac.blogsystem.utils.ResultGenerator;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "comments");
        return "admin/comment";
    }

    @GetMapping("/comments/list")
    @ResponseBody
    public Result List(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        PageInfo commentList = commentService.getAll(page, limit);
        PageResult pageResult = new PageResult(commentList.getList(), Integer.parseInt(String.valueOf(commentList.getTotal())), page, limit);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    @PostMapping("/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        if (commentService.batchComments(ids) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("审核失败");
        }
    }

    @PostMapping("/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        int deleted = commentService.deleteByIds(ids);
        if (deleted > 0){
            return ResultGenerator.genSuccessResult("删除成功");
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    @PostMapping("/comments/reply")
    @ResponseBody
    public Result doReply(@RequestParam("commentId") Integer commentId,
                           @RequestParam("replyBody") String replyBody) {
        if (commentId == null || commentId < 1 || !StringUtils.hasText(replyBody)) {
            return ResultGenerator.genFailResult("参数异常!");
        }
        if (commentService.replyComments(commentId, replyBody)>0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("回复失败");
        }
    }
}
