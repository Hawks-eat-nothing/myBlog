package com.yxg.blog.dao;

import com.yxg.blog.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {
    //根据创建时间倒序来排
    List<Comment> findByBlogIdAndParentCommentNull(@Param("blogId") Long blogId, @Param("blogParentId") Long blogParentId);

    //查询父级对象
    Comment findByParentCommentId(@Param("parentCommentId")Long parentcommentid);

    //查询二级回复
    List<Comment> findByBlogIdAndReplyId(@Param("blogId") Long blogId,@Param("childId") Long childId);

    //添加一个评论
    int saveComment(Comment comment);

    //删除评论
    void deleteComment(Long id);


}
