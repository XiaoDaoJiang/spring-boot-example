package com.xiaodao.controller.async;

import cn.hutool.core.util.RandomUtil;
import com.xiaodao.CommonResult;
import com.xiaodao.config.MdcTaskDecorator;
import com.xiaodao.exception.enums.GlobalErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
@RestController
public class TestAsyncController {

    @Autowired
    private MdcTaskDecorator mdcTaskDecorator;


    private final Map<String, DeferredResult<CommonResult>> allDeferredResult = new HashMap<>();

    @RequestMapping("/grab_votes")
    public DeferredResult<CommonResult> testAsync(@RequestParam String reqId) {

        log.info("已提交抢票请求，正在为您监控抢票结果...");
        final DeferredResult<CommonResult> commonResultDeferredResult = new DeferredResult<>(3000L,
                CommonResult.error(GlobalErrorCodeConstants.ASYNC_TIME_OUT)
        );

        // 注意：这里的 callback 需要手动复制上下文，不清除上下文
        commonResultDeferredResult.onTimeout(mdcTaskDecorator.decorate(() -> {
            log.info("onTimeout 抢票结果已超时，reqId:{}", reqId);
            allDeferredResult.remove(reqId);
        }, false));

        // 最后调用自定义的回调，清除上下文
        commonResultDeferredResult.onCompletion(mdcTaskDecorator.decorate(() -> {
            if (!allDeferredResult.containsKey(reqId)) {
                log.info("onCompletion 抢票结果已超时，reqId:{}", reqId);
            } else {
                log.info("抢票结果已返回，reqId:{}", reqId);
                allDeferredResult.remove(reqId);
            }
        }, true));
        allDeferredResult.put(reqId, commonResultDeferredResult);
        return commonResultDeferredResult;
    }

    @RequestMapping("/grab_votes_result")
    public CommonResult<String> testGetAsyncResult(@RequestParam String reqId) {
        log.info("查询抢票结果{}...", reqId);
        if (allDeferredResult.containsKey(reqId)) {
            final boolean testGetAsyncResult = allDeferredResult.get(reqId).setResult(
                    CommonResult.success("", RandomUtil.randomStringUpper(2) + RandomUtil.randomNumbers(4))
            );
            if (testGetAsyncResult) {
                log.info("抢票成功，已为您自动占座{}...", reqId);
                return CommonResult.success("抢票成功，已为您自动占座");
            } else {
                log.info("抢票失败{}...", reqId);
                return CommonResult.success("抢票失败");
            }

        } else {
            log.info("不存在对应的抢票记录{}...", reqId);
            return CommonResult.success("不存在对应的抢票记录");
        }

    }


    @RequestMapping("/req_message")
    public Callable<CommonResult<String>> testAsyncReqValidCodeMsg(@RequestParam String mobile) {
        log.info("正在发送短信验证码到手机{}...", mobile);
        return () -> {
            Thread.sleep(1000);
            log.info("短信验证码发送成功{}...", mobile);
            return CommonResult.success("短信验证码发送成功");
        };

    }

}
