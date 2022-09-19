package com.yxg.blog.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxg.blog.pojo.Blog;
import com.yxg.blog.pojo.Tag;
import com.yxg.blog.pojo.Type;
import com.yxg.blog.queryvo.DetailedBlog;
import com.yxg.blog.queryvo.FirstPageBlog;
import com.yxg.blog.queryvo.NewComment;
import com.yxg.blog.queryvo.RecommendBlog;
import com.yxg.blog.service.BlogService;
import com.yxg.blog.service.TagService;
import com.yxg.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@RequestParam(required = false,defaultValue = "1",value = "pageNum")int pageNum, Model model, RedirectAttributes attributes){
        PageHelper.startPage(pageNum,10);
        //查询博客列表
//        List<Blog> allBlog = blogService.getIndexBlog();
//
//        List<Type> allType = typeService.getBlogType();
//        List<Tag> allTag = tagService.getBlogTag();
        //查询最新推荐博客
        List<RecommendBlog> recommendBlog = blogService.getRecommendedBlog();
        List<NewComment> newComments = blogService.getNewComment();
        List<FirstPageBlog> allFirstPageBlog = blogService.getAllFirstPageBlog();
        //得到分页结果对象
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(allFirstPageBlog);
        model.addAttribute("pageInfo",pageInfo);
//        model.addAttribute("tags",allTag);
//        model.addAttribute("types",allType);
        model.addAttribute("recommendBlogs",recommendBlog);
        model.addAttribute("newComments",newComments);
        return "index";
    }
    @PostMapping("/search")
    public String search(@RequestParam(required = false,defaultValue = "1",value = "pageNum")int pageNum,
                         @RequestParam String query, Model model){

        PageHelper.startPage(pageNum, 7);
        List<FirstPageBlog> searchBlog = blogService.getSearchBlog(query);
        PageInfo pageInfo = new PageInfo(searchBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    //博客信息
    @GetMapping("/footer/blogmessage")
    public String blogMessage(Model model){
        int blogTotal = blogService.getBlogTotal();
        int blogViewTotal = blogService.getBlogViewTotal();
        int blogCommentTotal = blogService.getBlogCommentTotal();
        int blogMessageTotal = blogService.getBlogMessageTotal();

        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("blogViewTotal",blogViewTotal);
        model.addAttribute("blogCommentTotal",blogCommentTotal);
        model.addAttribute("blogMessageTotal",blogMessageTotal);
        return "index :: blogMessage";
    }

    //博客详情页
    @GetMapping("/blog/{id}")
    public String toLogin(@PathVariable Long id, Model model){
        DetailedBlog detailedBlog = blogService.getDetailedBlog(id);
        model.addAttribute("blog", detailedBlog);
        return "blog";
    }
}
