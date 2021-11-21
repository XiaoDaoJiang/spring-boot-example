package com.xiaodao.office.excel.service;

import cn.hutool.core.util.StrUtil;
import com.xiaodao.office.excel.dto.UploadData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

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
}
