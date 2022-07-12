package com.song.kb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.song.kb.domain.Content;
import com.song.kb.domain.Doc;
import com.song.kb.domain.DocExample;
import com.song.kb.exception.BusinessException;
import com.song.kb.exception.BusinessExceptionCode;
import com.song.kb.mapper.ContentMapper;
import com.song.kb.mapper.DocMapper;
import com.song.kb.mapper.DocMapperCust;
import com.song.kb.req.DocQueryReq;
import com.song.kb.req.DocSaveReq;
import com.song.kb.resp.DocQueryResp;
import com.song.kb.resp.PageResp;
import com.song.kb.util.CopyUtil;
import com.song.kb.util.RedisUtil;
import com.song.kb.util.RequestContext;
import com.song.kb.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WsService wsService;

//    @Resource
//    private RocketMQTemplate rocketMQTemplate;

    /**
     * 查询所有
     *
     * @return
     */
    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        LOG.info("doc封装：{}", docList);
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
     *
     * @param req
     */
    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            doc.setId(snowFlake.nextId());
            doc.setVoteCount(0);
            doc.setViewCount(0);
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除
     */
    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    /**
     * 点赞
     *
     * @param id
     */
    public void vote(Long id) {
        // docMapperCust.increaseVoteCount(id)
        // 远程IP+doc.id作为key,24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        // 推送消息
        // 异步化
        Doc doc = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");// 日志流水号
        wsService.sendInfo("【" + doc.getName() + "】被点赞!",logId);
        // RocketMQ
//        rocketMQTemplate.convertAndSend("VOTE_TOPIC","【" + doc.getName() + "】被点赞!");
    }

    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }
}
