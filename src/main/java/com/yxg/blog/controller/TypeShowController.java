package com.yxg.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxg.blog.pojo.Blog;
import com.yxg.blog.pojo.Type;
import com.yxg.blog.queryvo.FirstPageBlog;
import com.yxg.blog.service.BlogService;
import com.yxg.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeShowController {
    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id, @RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                        Model model){
//        PageHelper.startPage(pagenum, 100);  //开启分页
        List<Type> types = typeService.getBlogType();
        //-1从导航点过来的
        if (id == -1){
            id = types.get(0).getId();
        }
        model.addAttribute("types", types);
        List<FirstPageBlog> blogs = blogService.getByTypeId(id);
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(blogs);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTypeId", id);

        return "types";
    }
}
