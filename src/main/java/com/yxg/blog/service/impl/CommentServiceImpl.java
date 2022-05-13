package com.yxg.blog.service.impl;

import com.yxg.blog.dao.BlogDao;
import com.yxg.blog.dao.CommentDao;
import com.yxg.blog.pojo.Comment;
import com.yxg.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private BlogDao blogDao;

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    @Override
    public List<Comment> getCommentByBlogId(Long blogId) {  //查询父评论
        //没有父节点的默认为-1
        List<Comment> comments = commentDao.findByBlogIdAndParentCommentNull(blogId, Long.parseLong("-1"));
        return comments;
    }

    @Override
    //接收回复的表单
    public int saveComment(Comment comment) {
        //获得父id
        Long parentCommentId = comment.getParentComment().getId();
        //没有父级评论默认是-1
        if (parentCommentId != -1) {
            //有父级评论
            comment.setParentComment(commentDao.findByParentCommentId(comment.getParentCommentId()));
        } else {
            //没有父级评论
            comment.setParentCommentId((long) -1);
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentDao.saveComment(comment);
    }

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        List<Comment> comments = commentDao.findByBlogIdAndParentCommentNull(blogId, Long.parseLong("-1"));
        for (Comment comment : comments) {
            Long id = comment.getId();
            String parentNickname1 = comment.getNickname();
            List<Comment> childComments = commentDao.findByBlogIdAndParentCommentNull(blogId, id);
            combineChildren(blogId,childComments,parentNickname1);
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return comments;
    }

    private void combineChildren(Long blogId, List<Comment> childComments, String parentNickname1) {
        //判断是否有一级子评论、
        if (childComments.size()>0){
            //循环找出子评论的id
            for (Comment childComment : childComments) {
                String parentNickname = childComment.getNickname();
                childComment.setParentNickname(parentNickname1);
                tempReplys.add(childComment);
                Long childId = childComment.getId();
                //查出子二级评论
                recursively(blogId,childId,parentNickname);
            }
        }
    }

    private void recursively(Long blogId, Long childId, String parentNickname1) {
        //        根据子一级评论的id找到子二级评论
        List<Comment> replyComments = commentDao.findByBlogIdAndReplyId(blogId,childId);
        if (replyComments.size()>0){
            for (Comment replyComment : replyComments) {
                String parentNickname = replyComment.getNickname();
                replyComment.setParentNickname(parentNickname1);
                Long replyId = replyComment.getId();
                tempReplys.add(replyComment);
                recursively(blogId,replyId,parentNickname);
            }
        }
    }

}
