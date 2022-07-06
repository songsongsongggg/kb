package com.song.kb.controller;

import com.song.kb.req.EbookQueryReq;
import com.song.kb.req.EbookSaveReq;
import com.song.kb.resp.CommonResp;
import com.song.kb.resp.EbookQueryResp;
import com.song.kb.resp.PageResp;
import com.song.kb.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;
    

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req){

        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req){

        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){

        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }


}

