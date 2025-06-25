package com.xiaodao.controller;
import com.xiaodao.controller.ChildListDTO;
import java.util.Date;
import java.util.ArrayList;

import com.xiaodao.CommonResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("hello")
    public CommonResult<String> hello() {
        return CommonResult.success("hello world");
    }


    @RequestMapping("helloForm")
    public CommonResult<User> helloForm(User user) {
        return CommonResult.success(user);
    }

    @RequestMapping("get")
    private CommonResult<HelloController.User> testGet(@RequestBody User user) {
        log.info("hello get：{}", user);
        return CommonResult.success(user);
    }

    @PostMapping("map")
    private CommonResult<HelloController.User> testMap(@RequestBody User user) {
        log.info("hello map：{}", user);
        return CommonResult.success(user);
    }

    @Data
    public static class User {
        private String name;

        private Map<String,Object> properties;
    }


    @GetMapping("hello/{name}")
    public CommonResult<ParentListDTO> testUnw() {
        final ParentListDTO.ExtentParentListVO data = new ParentListDTO.ExtentParentListVO();
        // final ParentListDTO data = new ParentListDTO();
        data.setId(0);
        data.setName("jht");
        data.setBirth(new Date());
        data.setOther("123");
        data.setProperties(List.of("property1", "property2"));

        final ChildListDTO childListDTO = new ChildListDTO();
        childListDTO.setId(2);
        childListDTO.setName("jht2");
        childListDTO.setBirth(new Date());
        childListDTO.setProperties(List.of("childProperty1", "childProperty2"));

        data.setChildListDTO(childListDTO);

        return CommonResult.success(data);
    }
}
