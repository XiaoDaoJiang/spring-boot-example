package com.xiaodao.controller;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@RestController
public class HelloController {

    @Timed
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @Timed
    @RequestMapping("/hello2")
    public String hello2(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        StringBuilder stringBuilder = new StringBuilder();
        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            String paramValues = request.getParameter(parameter);
            if (paramValues != null && !paramValues.isEmpty()) {
                stringBuilder.append(parameter);
                stringBuilder.append("=");
                stringBuilder.append(paramValues);
                stringBuilder.append("&");
            }
        }
        log.info(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
