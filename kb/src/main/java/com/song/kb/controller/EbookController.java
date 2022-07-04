package com.song.kb.controller;

import com.song.kb.req.EbookReq;
import com.song.kb.resp.CommonResp;
import com.song.kb.resp.EbookResp;
import com.song.kb.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;
    

    @GetMapping("/list")
    public CommonResp list(EbookReq req){

        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

}

