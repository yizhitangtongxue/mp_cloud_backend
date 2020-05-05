package com.diancan.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 首页
@Controller
@RequestMapping("/admin/index")
public class IndexController {

    @GetMapping("/index")
    public String login()
    {
        return "/admin/index/index";
    }
}
