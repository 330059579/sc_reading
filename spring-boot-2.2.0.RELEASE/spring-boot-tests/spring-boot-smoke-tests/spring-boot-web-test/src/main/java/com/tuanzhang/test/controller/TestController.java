package com.tuanzhang.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TestController {

    @RequestMapping("/test.htm")
    @ResponseBody
    public String test(){
        return "tuanzhang_test";
    }
}
