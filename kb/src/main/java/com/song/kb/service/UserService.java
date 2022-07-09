package com.song.kb.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.song.kb.domain.User;
import com.song.kb.domain.UserExample;
import com.song.kb.exception.BusinessException;
import com.song.kb.exception.BusinessExceptionCode;
import com.song.kb.mapper.UserMapper;
import com.song.kb.req.UserQueryReq;
import com.song.kb.req.UserSaveReq;
import com.song.kb.resp.PageResp;
import com.song.kb.resp.UserQueryResp;
import com.song.kb.util.CopyUtil;
import com.song.kb.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 分页查询
     *
     * @param req
     * @return
     */
    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo("%" + req.getLoginName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}", pageInfo.getTotal());
//        LOG.info("总页数：{}",pageInfo.getPageNum());
//        List<UserQueryResp> respList = new ArrayList<>();
//        for (User user : userList) {
////            UserQueryResp userResp = new UserQueryResp();
////            BeanUtils.copyProperties(user,userResp);
//            //对象copy
//            UserQueryResp userResp = CopyUtil.copy(user, UserQueryResp.class);
//            respList.add(userResp);
//        }
        //列表copy
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存或新增
     *
     * @param req
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            User userDB = selectByLoginName(req.getLoginName());
            if (ObjectUtils.isEmpty(userDB)){
                //新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else{
                //用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            //更新
            user.setLoginName(null);//loginName为空时 下面不会执行loginName字段
            user.setPassword(null);//password
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }else{
            return userList.get(0);
        }
    }

}
