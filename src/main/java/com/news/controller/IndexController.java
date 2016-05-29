package com.news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Robson Martins
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping
    public String getIndexPage() {
        return "index";
    }
}
