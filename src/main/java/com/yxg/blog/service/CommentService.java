package com.yxg.blog.service;

import com.yxg.blog.pojo.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getCommentByBlogId(Long blogId);

    int saveComment(Comment comment);

    List<Comment> listCommentByBlogId(Long blogId);
}
