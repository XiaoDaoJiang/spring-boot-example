package com.xiaodao.validation.biz;

import com.xiaodao.validation.first.Car;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author jianghaitao
 * @Classname BizCaseCheckServiceImpl
 * @Version 1.0.0
 * @Date 2024-12-18 11:59
 * @Created by jianghaitao
 */
@Service("carCaseCheckService")
public class CarCaseCheckServiceImpl implements BizCaseCheckService<Car> {

    @Override
    public String checkBizCase(Car car) {
        return StringUtils.isEmpty(car.getLicensePlate()) ? "车牌号不能为空" : "车牌号格式不正确";
    }

}
