package com.xiaodao.office.excel.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.xiaodao.office.excel.dto.UploadData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
public class ExcelService {

    @Resource
    private ImportDataHandler importDataHandler;


    /**
     * 校验必填项
     */
    public boolean validate(UploadData uploadData) {
        if (StrUtil.hasBlank(uploadData.getName(), uploadData.getIdCard(), uploadData.getOrg())) {
            uploadData.setErrorMsg("缺少必填项");
            return false;
        }
        return true;
    }

    public List<Future<List<UploadData>>> dispatchHandlerBatch(List<UploadData> uploadDataList, Map<String, String> localMap) {
        List<Future<List<UploadData>>> futureList = importDataHandler.handle(uploadDataList, localMap);
        return futureList;
    }

    public List<UploadData> importData(MultipartFile file) throws IOException, InterruptedException, ExecutionException {
        String id = "service-" + RandomUtil.randomNumbers(6);
        StopWatch stopWatch = new StopWatch(id);
        MutableTriple<Integer, List<UploadData>, List<Future<List<UploadData>>>> result = new MutableTriple<>();
        UploadDataListener readListener = new UploadDataListener(this, result);

        stopWatch.start("准备数据缓存");
        readListener.initMap();
        stopWatch.stop();

        stopWatch.start("解析导入数据");
        EasyExcelFactory.read(file.getInputStream(), UploadData.class, readListener).sheet().doRead();
        stopWatch.stop();

        //
        stopWatch.start("处理结果汇总");
        List<UploadData> finalResult = new ArrayList<>(result.left);
        log.info("共处理{}条导入数据，分为{}批进行处理", result.left, result.right.size());
        try {
            for (final Future<List<UploadData>> future : result.right) {
                List<UploadData> dataList = future.get();
                finalResult.addAll(dataList);
            }
        }catch (ExecutionException e){
            log.error("任务执行时异常",e.getCause());
        }
        stopWatch.stop();
        readListener.clearMap();

        finalResult.addAll(result.middle);
        return finalResult;
    }
}
