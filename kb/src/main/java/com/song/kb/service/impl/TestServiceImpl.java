package com.song.kb.service.impl;

import com.song.kb.mapper.TestMapper;

import com.song.kb.pojo.Test1;
import com.song.kb.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public Test1 testById(Integer id) {
        return testMapper.selectByPrimaryKey(id);
    }
}
