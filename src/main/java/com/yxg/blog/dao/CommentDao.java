package com.yxg.blog.dao;

import com.yxg.blog.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {
    //查询父级对象
    //根据id为“-1”和博客id查询出所有父评论（父级评论id为‘-1’）
    List<Comment> findByBlogIdParentIdNull(@Param("blogId") Long blogId, @Param("blogParentId") Long blogParentId);

    //查询一级回复
    //根据父评论的id查询出一级子回复
    List<Comment> findByBlogIdParentIdNotNull(@Param("blogId") Long blogId,@Param("id") Long id);

    //查询二级回复
    //根据子回复的id循环迭代查询出所有子集回复
    List<Comment> findByBlogIdAndReplyId(@Param("blogId") Long blogId, @Param("childId") Long childId);

    //添加一个评论
    int saveComment(Comment comment);

    //删除评论
    void deleteComment(Long id);

    // 根据父评论id查询留言信息
    Comment getEmailByParentId(Long parentId);

}
