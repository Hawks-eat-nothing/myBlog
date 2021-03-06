package com.yxg.blog.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxg.blog.pojo.Blog;
import com.yxg.blog.pojo.Tag;
import com.yxg.blog.pojo.Type;
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
    public String index(@RequestParam(required = false,defaultValue = "1",value = "pageNum")int pageNum, Model model){
        PageHelper.startPage(pageNum,7);
        List<Blog> allBlog = blogService.getIndexBlog();
        List<Type> allType = typeService.getBlogType();
        List<Tag> allTag = tagService.getBlogTag();
        List<Blog> recommendBlog = blogService.getAllRecommendBlog();

        //得到分页结果对象
        PageInfo pageInfo = new PageInfo(allBlog);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("tags",allTag);
        model.addAttribute("types",allType);
        model.addAttribute("recommendBlogs",recommendBlog);

        return "index";
    }
    @PostMapping("/search")
    public String search(@RequestParam(required = false,defaultValue = "1",value = "pageNum")int pageNum,
                         @RequestParam String query, Model model){

        PageHelper.startPage(pageNum, 7);
        List<Blog> searchBlog = blogService.getSearchBlog(query);
        PageInfo pageInfo = new PageInfo(searchBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }
    @GetMapping("/blog/{id}")
    public String toLogin(@PathVariable Long id, Model model){
        Blog blog = blogService.getDetailedBlog(id);
        model.addAttribute("blog", blog);
        return "blog";
    }
}
