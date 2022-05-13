package com.yxg.blog.controller;

import com.yxg.blog.pojo.Comment;
import com.yxg.blog.pojo.User;
import com.yxg.blog.queryvo.DetailedBlog;
import com.yxg.blog.service.BlogService;
import com.yxg.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")  //展示留言
    public String comments(@PathVariable Long blogId, Model model){
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }

    @PostMapping("/comments")   //提交留言
    public String post(Comment comment, HttpSession session,Model model){
//        Long blogId = comment.getBlog().getId();
        Long blogId = comment.getBlogId();
//        comment.setBlog(blogService.getDetailedBlog(blogId));  //绑定博客与评论
//        comment.setBlogId(blogId);
        User user = (User) session.getAttribute("user");
        if (user != null){   //用户为管理员
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            comment.setAvatar(avatar);
        }

        if (comment.getParentComment().getId()!=null){
            comment.setParentCommentId(comment.getParentComment().getId());
        }
        commentService.saveComment(comment);
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments",comments);
        return "blog::commentList";

//        System.out.println(comment);
//        commentService.saveComment(comment);
//        return "redirect:/comments/" + blogId;
    }

}
