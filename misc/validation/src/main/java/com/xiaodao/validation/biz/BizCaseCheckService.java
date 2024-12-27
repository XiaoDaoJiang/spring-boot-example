package com.xiaodao.validation.biz;

/**
 * 自定义业务场景校验服务
 *
 * @author jianghaitao
 * @Classname BizCheckService
 * @Version 1.0.0
 * @Date 2024-12-18 11:57
 * @Created by jianghaitao
 */
public interface BizCaseCheckService<T> {

    String checkBizCase(T t);

}
