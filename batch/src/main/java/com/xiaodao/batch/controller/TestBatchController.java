package com.xiaodao.batch.controller;

import com.xiaodao.batch.domain.Car;
import com.xiaodao.batch.domain.CarDto;
import com.xiaodao.batch.extension.CarMapper;
import com.xiaodao.common.entity.User;
import com.xiaodao.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestBatchController {

    @Autowired
    private IUserService userService;

    @Qualifier("myConversionService")
    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/hello")
    public String hello() {
        final User user = userService.queryById(1L);
        return "Hello, World!" + user.getName();
    }

    @RequestMapping("/convert")
    public String convert() {

        final CarMapper carConverter = applicationContext.getBean(CarMapper.class);

        final Car car = new Car();
        final CarDto carDto = conversionService.convert(car, CarDto.class);
        final User user = userService.queryById(1L);
        return "Hello, World!" + user.getName();
    }
}