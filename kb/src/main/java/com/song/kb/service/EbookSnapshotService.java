package com.song.kb.service;

import com.song.kb.mapper.EbookSnapshotMapperCust;
import com.song.kb.resp.StatisticResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void genSnapshot(){
        ebookSnapshotMapperCust.getSnapshot();
    }

    public List<StatisticResp> getStatistic(){
        List<StatisticResp> statistic = ebookSnapshotMapperCust.getStatistic();
        return statistic;
    }
}
