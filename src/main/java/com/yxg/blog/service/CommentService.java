package com.yxg.blog.service;

import com.yxg.blog.pojo.Comment;

import java.util.List;

public interface CommentService {
    //根据博客id查询评论信息
    List<Comment> listCommentByBlogId(Long blogId);
    //添加保存评论
    int saveComment(Comment comment,Comment parentComment);
    //删除评论
    void deleteComment(Comment comment, Long id);
    // 根据父评论id查询留言信息
    Comment getEmailByParentId(Long parentId);
}
