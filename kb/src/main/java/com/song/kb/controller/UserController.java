package com.song.kb.controller;

import com.song.kb.req.UserQueryReq;
import com.song.kb.req.UserSaveReq;
import com.song.kb.resp.CommonResp;
import com.song.kb.resp.UserQueryResp;
import com.song.kb.resp.PageResp;
import com.song.kb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req){

        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){

        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }


}

