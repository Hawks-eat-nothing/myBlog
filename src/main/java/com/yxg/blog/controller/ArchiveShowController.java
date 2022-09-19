package com.yxg.blog.controller;

import com.yxg.blog.service.BlogService;
import com.yxg.blog.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {
    @Autowired
    private MemoryService memoryService;

    @GetMapping("/archives")
    public String archives(Model model) {

        model.addAttribute("memories",memoryService.listMemory());
        return "archives";
    }
}
