package com.yxg.blog.controller;

import com.yxg.blog.annotation.AccessLimit;
import com.yxg.blog.pojo.Blog;
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
    public String comments(@PathVariable Long blogId, Model model) {

        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }

    @PostMapping("/comments")   //新增评论
    @AccessLimit(seconds = 15, maxCount = 3) //15秒内 允许请求3次
    public String post(Comment comment, HttpSession session, Model model) {
        Long blogId = comment.getBlogId();
        User user = (User) session.getAttribute("user");
        if (user != null) {   //用户为管理员
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            //设置头像
            comment.setAvatar(avatar);
        }
        Long parentId = comment.getParentComment().getId();
        Comment parentComment = null;
        if (parentId != null) {
            comment.setParentCommentId(parentId);
            parentComment = commentService.getEmailByParentId(parentId);
        }
        commentService.saveComment(comment,parentComment);
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }
    //删除评论
    @GetMapping("/comment/{blogId}/{id}/delete")
    public String delete(@PathVariable Long blogId,
                         @PathVariable Long id,
                         Comment comment,
                         HttpSession session,
                         Model model) {

        User user = (User) session.getAttribute("user");
        if (user!=null){
            commentService.deleteComment(comment,id);
        }

        DetailedBlog detailedBlog = blogService.getDetailedBlog(blogId);
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("blog",detailedBlog);
        model.addAttribute("comments",comments);
        return "blog";
    }
}
