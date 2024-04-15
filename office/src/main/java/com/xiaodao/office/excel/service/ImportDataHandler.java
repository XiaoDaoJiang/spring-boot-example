package com.xiaodao.office.excel.service;

import cn.hutool.core.collection.ListUtil;
import com.xiaodao.office.excel.dto.UploadData;
import com.xiaodao.office.excel.model.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class ImportDataHandler implements Handler<UploadData> {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TaskHandler taskHandler;

    @Override
    public List<Future<List<UploadData>>> handle(List<UploadData> uploadDataList, Map<String, String> dic) {
        //    接受处理数据，开始分批次提交给线程池处理
        List<List<UploadData>> split = ListUtil.split(uploadDataList, 300);
        List<Future<List<UploadData>>> futureList = new ArrayList<>(split.size());
        for (final List<UploadData> dataList : split) {
            Future<List<UploadData>> future = threadPoolExecutor.submit(new Callable<List<UploadData>>() {
                @Override
                public List<UploadData> call() throws Exception {

                    taskHandler.process(dataList, dic);
                    return dataList;
                }
            });
            futureList.add(future);
        } return futureList;
    }

}
