package com.xiaodao.office.excel.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xiaodao.office.excel.dto.BaseInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BaseInfoListener extends AnalysisEventListener<BaseInfo> {

    private List<BaseInfo> baseInfos;

    public BaseInfoListener(List<BaseInfo> baseInfos) {
        this.baseInfos = baseInfos;
    }

    @Override
    public void invoke(BaseInfo data, AnalysisContext context) {
        baseInfos.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("共读取数据{}行", baseInfos.size());
    }
}
