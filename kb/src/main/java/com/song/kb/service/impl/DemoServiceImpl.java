package com.song.kb.service.impl;


import com.song.kb.domain.Demo;
import com.song.kb.mapper.DemoMapper;
import com.song.kb.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements TestService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public Demo selectByPrimaryKey(Integer id) {
        return demoMapper.selectByPrimaryKey(id);
    }
}
