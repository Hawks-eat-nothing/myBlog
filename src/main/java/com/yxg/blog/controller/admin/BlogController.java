package com.yxg.blog.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxg.blog.pojo.Blog;
import com.yxg.blog.pojo.User;
import com.yxg.blog.service.BlogService;
import com.yxg.blog.service.TagService;
import com.yxg.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    public void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.getAllType());
        model.addAttribute("tags", tagService.getAllTag());
    }

    @GetMapping("/blogs")//后台显示博客列表
    public String blogs(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                        Model model){
        PageHelper.startPage(pagenum,5);
        List<Blog> allBlog = blogService.getAllBlog();
        //得到分页结果对象
        PageInfo pageInfo = new PageInfo(allBlog);
        model.addAttribute("pageInfo",pageInfo);
        setTypeAndTag(model);
        return "admin/blog_management";
    }

    @PostMapping("/blogs/search")
    public String searchBlogs(Blog blog,
                              @RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                              Model model){
        PageHelper.startPage(pagenum,5);
        List<Blog> allBlog = blogService.searchAllBlog(blog);
        //得到分页对象
        PageInfo pageInfo = new PageInfo(allBlog);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("message","查询成功");
        setTypeAndTag(model);
        return "admin/blog_management::blogList";
    }

    @GetMapping("/blogs/input") //去新增博客页面
    public String toAddBlog(Model model){
        model.addAttribute("blog", new Blog());  //返回一个blog对象给前端th:object
        setTypeAndTag(model);
        return "admin/blog_post";
    }

    @GetMapping("/blogs/{id}/input") //去编辑博客页面
    public String toEditBlog(@PathVariable Long id, Model model){
        Blog blog = blogService.getBlog(id);
        blog.init(); //将tags集合转换为tagIds字符串
        model.addAttribute("blog",blog);
        setTypeAndTag(model);
        return "admin/blog_post";
    }

    @PostMapping("/blogs") //新增、编辑博客
    public String addBlog(Blog blog, HttpSession session, RedirectAttributes attributes){
        //设置user属性
        blog.setUser((User) session.getAttribute("user"));
        //设置用户id
        blog.setUserId(blog.getUser().getId());
        //设置blog的type
        blog.setType(typeService.getType(blog.getType().getId()));
        //设置blog中typeId属性
        blog.setTypeId(blog.getType().getId());
        //给blog中的List<Tag>赋值
        blog.setTags(tagService.getTagByString(blog.getTagIds()));

        if (blog.getId() == null) {   //id为空，则为新增
            blogService.saveBlog(blog);
        } else {
            blogService.updateBlog(blog);
        }

        attributes.addFlashAttribute("message", "新增成功");
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String deleteBlogs(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }

}
