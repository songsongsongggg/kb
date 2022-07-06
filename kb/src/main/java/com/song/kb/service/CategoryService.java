package com.song.kb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.song.kb.domain.Category;
import com.song.kb.domain.CategoryExample;
import com.song.kb.mapper.CategoryMapper;
import com.song.kb.req.CategoryQueryReq;
import com.song.kb.req.CategorySaveReq;
import com.song.kb.resp.CategoryQueryResp;
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
public class CategoryService {
    private final static Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 查询所有
     *
     * @return
     */
    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        //列表copy
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return list;
    }
    /**
     * 分页查询
     *
     * @param req
     * @return
     */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
//        LOG.info("总页数：{}",pageInfo.getPageNum());
//        List<CategoryQueryResp> respList = new ArrayList<>();
//        for (Category category : categoryList) {
////            CategoryQueryResp categoryResp = new CategoryQueryResp();
////            BeanUtils.copyProperties(category,categoryResp);
//            //对象copy
//            CategoryQueryResp categoryResp = CopyUtil.copy(category, CategoryQueryResp.class);
//            respList.add(categoryResp);
//        }
        //列表copy
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存或新增
     * @param req
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())){
            //新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else {
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }
}
