package com.song.kb.controller;

import com.song.kb.domain.Demo;
import com.song.kb.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kb")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/world")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/song")
    public String hello1(String name) {
        return "Hello World post: " + name;
    }

    @GetMapping("/demo")
    public Demo hello2(){
        return testService.selectByPrimaryKey(1);
    }

}

