package com.song.kb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.song.kb.domain.Doc;
import com.song.kb.domain.DocExample;
import com.song.kb.mapper.DocMapper;
import com.song.kb.req.DocQueryReq;
import com.song.kb.req.DocSaveReq;
import com.song.kb.resp.DocQueryResp;
import com.song.kb.resp.PageResp;
import com.song.kb.util.CopyUtil;
import com.song.kb.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {
    private final static Logger LOG = LoggerFactory.getLogger(DocService.class);
    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 查询所有
     *
     * @return
     */
    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        //列表copy
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }
    /**
     * 分页查询
     *
     * @param req
     * @return
     */
    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
//        LOG.info("总页数：{}",pageInfo.getPageNum());
//        List<DocQueryResp> respList = new ArrayList<>();
//        for (Doc doc : docList) {
////            DocQueryResp docResp = new DocQueryResp();
////            BeanUtils.copyProperties(doc,docResp);
//            //对象copy
//            DocQueryResp docResp = CopyUtil.copy(doc, DocQueryResp.class);
//            respList.add(docResp);
//        }
        //列表copy
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存或新增
     * @param req
     */
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if (ObjectUtils.isEmpty(req.getId())){
            //新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        }else {
            //更新
            docMapper.updateByPrimaryKey(doc);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }
}
