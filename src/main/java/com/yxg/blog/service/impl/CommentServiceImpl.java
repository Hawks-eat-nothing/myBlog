package com.yxg.blog.service.impl;

import com.yxg.blog.dao.BlogDao;
import com.yxg.blog.dao.CommentDao;
import com.yxg.blog.pojo.Comment;
import com.yxg.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    // TODO: 自动导入Java邮件发送实现类
    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        //查询出父节点
        List<Comment> comments = commentDao.findByBlogIdParentIdNull(blogId, Long.parseLong("-1"));
        for (Comment comment : comments) {
            Long id = comment.getId();
            String parentNickName = comment.getNickname();
            List<Comment> childComments = commentDao.findByBlogIdParentIdNotNull(blogId, id);
            //查询出子评论
            combineChildren(blogId, childComments, parentNickName);
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return comments;
    }

    @Override
    //接收回复的表单
    public int saveComment(Comment comment,Comment parentComment) {
         comment.setCreateTime(new Date());
         int comments = commentDao.saveComment(comment);
         //文章评论数
        blogDao.getCommentCountById(comment.getBlogId());

        //判断是否有父评论,有的话就发送邮件通知
        if (!StringUtils.isEmpty(parentComment)){
            String parentNickname = parentComment.getNickname();
            String nickname = comment.getNickname();
            String title = parentComment.getBlog().getTitle();
            String content = "亲爱的"+parentNickname+"您在【X.FreeNotes】"+title+"的评论收到了来自"+nickname+"的回复，内容如下："+"\r\n" + "\r\n"+comment.getContent();
            String email = parentComment.getEmail();

            //发送邮件
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("X.FreeNotes 评论回复");
            simpleMailMessage.setText(content);
            simpleMailMessage.setFrom("yaxing_guo@outlook.com");
            simpleMailMessage.setTo(email);
            javaMailSender.send(simpleMailMessage);
        }
        return comments;
    }

    @Override
    public void deleteComment(Comment comment, Long id) {
        commentDao.deleteComment(id);
        blogDao.getCommentCountById(comment.getBlogId());
    }

    @Override
    public Comment getEmailByParentId(Long parentId) {
        return commentDao.getEmailByParentId(parentId);
    }

    /**
     * 查询出子评论
     *
     * @param blogId
     * @param childComments   所有子评论
     * @param parentNickname1 父评论姓名
     */
    private void combineChildren(Long blogId, List<Comment> childComments, String parentNickname1) {
        //判断是否有一级子评论、
        if (childComments.size() > 0) {
            //循环找出子评论的id
            for (Comment childComment : childComments) {
                String parentNickname = childComment.getNickname();
                childComment.setParentNickname(parentNickname1);
                tempReplys.add(childComment);
                Long childId = childComment.getId();
                //查出子二级评论
                recursively(blogId, childId, parentNickname);
            }
        }
    }

    /**
     *
     * @param blogId
     * @param childId 子评论id
     * @param parentNickname1 子评论用户名
     */
    private void recursively(Long blogId, Long childId, String parentNickname1) {
        //        根据子一级评论的id找到子二级评论
        List<Comment> replyComments = commentDao.findByBlogIdAndReplyId(blogId, childId);
        if (replyComments.size() > 0) {
            for (Comment replyComment : replyComments) {
                String parentNickname = replyComment.getNickname();
                replyComment.setParentNickname(parentNickname1);
                Long replyId = replyComment.getId();
                tempReplys.add(replyComment);
                recursively(blogId, replyId, parentNickname);
            }
        }
    }

}
