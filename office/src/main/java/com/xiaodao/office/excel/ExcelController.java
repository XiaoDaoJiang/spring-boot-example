package com.xiaodao.office.excel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.xiaodao.office.excel.dto.UploadData;
import com.xiaodao.office.excel.service.ExcelService;
import com.xiaodao.office.excel.service.UploadDataListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.slf4j.MDC;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author xiaodao
 */
@Slf4j
@RestController
@RequestMapping("excel")
public class ExcelController {

    private final ExcelService excelService;

    private static final String KEY = "requestId";

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("upload")
    public void upload(@RequestBody @Valid @NotNull MultipartFile file, HttpServletResponse response) throws IOException,
            ExecutionException,
            InterruptedException {

        String id = "controller-" + RandomUtil.randomNumbers(6);
        StopWatch stopWatch = new StopWatch(id);
        MutableTriple<Integer, List<UploadData>, List<Future<List<UploadData>>>> result = new MutableTriple<>();
        UploadDataListener readListener = new UploadDataListener(excelService, result);

        stopWatch.start("准备数据缓存");
        readListener.initMap();
        stopWatch.stop();

        stopWatch.start("解析导入数据");
        EasyExcelFactory.read(file.getInputStream(), UploadData.class, readListener).sheet().doRead();
        stopWatch.stop();

        //
        stopWatch.start("处理结果汇总");
        List<UploadData> finalResult = new ArrayList<>(result.left);
        for (final Future<List<UploadData>> future : result.right) {
            // stopWatch.start("阻塞获取异步任务处理结果");
            List<UploadData> dataList = future.get();
            finalResult.addAll(dataList);
            // stopWatch.stop();

        }
        stopWatch.stop();
        readListener.clearMap();

        finalResult.addAll(result.middle);
        //    export
        if (!finalResult.isEmpty()){
            stopWatch.start("处理完毕，导出错误数据");
            ClassPathResource classPathResource = new ClassPathResource("templates/rankReform.xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("人员导入数据-", "UTF-8").replace("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + System.currentTimeMillis() + ".xlsx");

            EasyExcelFactory.write(response.getOutputStream(), UploadData.class)
                    .withTemplate(classPathResource.getInputStream())
                    .needHead(Boolean.FALSE)
                    .sheet()
                    .doWrite(finalResult);
            stopWatch.stop();
        }
        log.info(stopWatch.prettyPrint());

    }

    @GetMapping("templateWrite")
    public void templateWrite(HttpServletResponse response) throws IOException, InterruptedException {
        MDC.put(KEY, UUID.randomUUID().toString());
        ClassPathResource classPathResource = new ClassPathResource("templates/rankReform.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("人员导入数据-", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + System.currentTimeMillis() + ".xlsx");

        EasyExcelFactory.write(response.getOutputStream(), UploadData.class).withTemplate(classPathResource.getInputStream()).needHead(Boolean.FALSE).sheet().doWrite(data());

        log.info("log.....");
        MDC.remove(KEY);
    }

    private List<UploadData> data() throws InterruptedException {
        Thread.sleep(10000);
        return IntStream.range(0, 5000).mapToObj(i -> new UploadData(RandomUtil.randomString(6),
                        RandomUtil.randomEle(new String[]{"男","女"}), "id" + i,
                        (DateUtil.offsetDay(DateUtil.parseDate("1988-01-14"), RandomUtil.randomInt(-5000,
                                i))).toString(),"org-" + i)).collect(Collectors.toList());
    }

    @GetMapping("hello")
    public String hello() {
        MDC.put(KEY, UUID.randomUUID().toString());
        log.info("log.....");
        MDC.remove(KEY);
        return "hello";
    }
}
