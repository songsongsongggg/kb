package com.song.kb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class TestController {

    @GetMapping("/world")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/song")
    public String hello1(String name) {
        return "Hello World post: " + name;
    }
}
