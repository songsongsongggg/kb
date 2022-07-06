package com.song.kb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.song.kb.domain.Ebook;
import com.song.kb.domain.EbookExample;
import com.song.kb.mapper.EbookMapper;
import com.song.kb.req.EbookQueryReq;
import com.song.kb.req.EbookSaveReq;
import com.song.kb.resp.EbookQueryResp;
import com.song.kb.resp.PageResp;
import com.song.kb.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    private final static Logger LOG = LoggerFactory.getLogger(EbookService.class);
    @Resource
    private EbookMapper ebookMapper;

    /**
     * 分页查询
     *
     * @param req
     * @return
     */
    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
//        LOG.info("总页数：{}",pageInfo.getPageNum());
//        List<EbookQueryResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
////            EbookQueryResp ebookResp = new EbookQueryResp();
////            BeanUtils.copyProperties(ebook,ebookResp);
//            //对象copy
//            EbookQueryResp ebookResp = CopyUtil.copy(ebook, EbookQueryResp.class);
//            respList.add(ebookResp);
//        }
        //列表copy
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 编辑保存或新增
     * @param req
     */
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())){
            //新增
            ebookMapper.insert(ebook);
        }else {
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
}
