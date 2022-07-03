package com.song.kb.controller;

import com.song.kb.domain.Ebook;
import com.song.kb.resp.CommonResp;
import com.song.kb.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kb")
public class EbookController {

    @Autowired
    private EbookService ebookService;
    

    @GetMapping("/ebook")
    public CommonResp list(){

        CommonResp<Object> resp = new CommonResp<>();
        List<Ebook> list = ebookService.list();
        resp.setContent(list);
        return resp;
    }

}

