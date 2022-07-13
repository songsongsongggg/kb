package com.song.kb.mapper;


import com.song.kb.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotMapperCust {

    void getSnapshot();

    List<StatisticResp> getStatistic();
}
