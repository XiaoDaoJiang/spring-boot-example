package com.xiaodao.office.excel.service;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.xiaodao.office.excel.dto.UploadData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class UploadDataListener implements ReadListener<UploadData> {

    private static final int BATCH_SIZE = 1000;

    private final ExcelService excelService;

    private final MutableTriple<Integer, List<UploadData>, List<Future<List<UploadData>>>> result;

    List<Future<List<UploadData>>> futureList;

    private List<UploadData> uploadDataList;

    private List<UploadData> errorUploadDateList;

    private int totalDataRow;

    private Map<String, String> localMap;


    public UploadDataListener(ExcelService excelService, MutableTriple<Integer, List<UploadData>, List<Future<List<UploadData>>>> result) {
        this.excelService = excelService;
        this.result = result;
    }


    public void initMap() {
        localMap = IntStream.range(0, 10000).mapToObj(i -> "org-" + i).collect(Collectors.toMap(String::toString, i -> RandomUtil.randomString(4)));
    }

    public void clearMap() {
        localMap.clear();
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        totalDataRow = context.readSheetHolder().getApproximateTotalRowNumber() - 1;
        log.info("开始解析导入数据，总行数{}", totalDataRow);
        int initialCapacity = totalDataRow = Math.max(BATCH_SIZE, totalDataRow);
        uploadDataList = new ArrayList<>(initialCapacity);
        errorUploadDateList = new ArrayList<>(initialCapacity / 2);
        futureList = new ArrayList<>(initialCapacity);
    }

    @Override
    public void invoke(UploadData uploadData, AnalysisContext analysisContext) {
        // 必填校验
        if (excelService.validate(uploadData)) {
            // 保存解析数据，等待批次处理
            uploadDataList.add(uploadData);
        }
        // 校验不通过，记录错误信息
        else {
            errorUploadDateList.add(uploadData);
        }

        // 判断待处理数据需要进行处理
        if (uploadDataList.size() >= BATCH_SIZE) {
            handlerBatch(uploadDataList);
            uploadDataList = new ArrayList<>(totalDataRow - BATCH_SIZE);
        }
    }

    private void handlerBatch(List<UploadData> uploadDataList) {
        futureList.addAll(excelService.dispatchHandlerBatch(uploadDataList, localMap));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (!uploadDataList.isEmpty()) {
            handlerBatch(uploadDataList);
        }
        result.setLeft(totalDataRow);
        result.setRight(futureList);
        result.setMiddle(errorUploadDateList);
        log.info("全部解析完成");
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex(), excelDataConvertException.getColumnIndex());
        }
    }

}
