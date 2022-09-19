package com.yxg.blog.service.impl;

import com.yxg.blog.dao.BlogDao;
import com.yxg.blog.exception.NotFoundException;
import com.yxg.blog.pojo.Blog;
import com.yxg.blog.pojo.BlogAndTag;
import com.yxg.blog.pojo.Tag;
import com.yxg.blog.queryvo.*;
import com.yxg.blog.service.BlogService;
import com.yxg.blog.util.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogDao blogDao;

    @Override
    public DetailedBlog getDetailedBlog(Long id) {
        DetailedBlog blog = blogDao.getDetailedBlog(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        String content = blog.getContent();
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));  //将Markdown格式转换成html
        blogDao.updateViews(id);
        // 文章评论数量更新
        blogDao.getCommentCountById(id);
        return blog;
    }

    @Override
    public List<FirstPageBlog> getByTypeId(Long typeId) {
        return blogDao.getByTypeId(typeId);
    }

    @Override
    public List<BlogQuery> getAllBlog() {
        return blogDao.getAllBlogQuery();
    }

    @Override
    public void deleteBlog(Long id) {
        blogDao.deleteBlog(id);
    }


    @Override
    public ShowBlog getBlogById(Long id) {
        return blogDao.getBlogById(id);
    }

    @Override
    public int updateBlog(ShowBlog showBlog) {
        showBlog.setUpdateTime(new Date());
        return blogDao.updateBlog(showBlog);
    }

    @Override
    public List<BlogQuery> getBlogBySearch(SearchBlog searchBlog) {
        return blogDao.searchByTitleAndType(searchBlog);
    }

    @Override
    public List<FirstPageBlog> getAllFirstPageBlog() {
        return blogDao.getFirstPageBlog();
    }

    @Override
    public List<RecommendBlog> getRecommendedBlog() {
        List<RecommendBlog> allRecommendBlog = blogDao.getAllRecommendBlog();
        return allRecommendBlog;
    }

    @Override
    public List<FirstPageBlog> getSearchBlog(String query) {
        return blogDao.getSearchBlog(query);
    }

    //统计博客总数
    @Override
    public Integer getBlogTotal() {
        return blogDao.getBlogTotal();
    }

    //统计访问总数
    @Override
    public Integer getBlogViewTotal() {
        return blogDao.getBlogViewTotal();
    }

    //统计评论总数
    @Override
    public Integer getBlogCommentTotal() {
        return blogDao.getBlogCommentTotal();
    }

    //统计留言总数
    @Override
    public Integer getBlogMessageTotal() {
        return blogDao.getBlogMessageTotal();
    }

    //查询最新评论
    // TODO: 存在缓存中
    @Override
    public List<NewComment> getNewComment() {
        return blogDao.getNewComment();
    }

    @Override
    public int saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        blog.setCommentCount(0);
        return blogDao.saveBlog(blog);
    }


}
