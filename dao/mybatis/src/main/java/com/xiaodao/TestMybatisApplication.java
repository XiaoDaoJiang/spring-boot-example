package com.xiaodao;

import com.xiaodao.mybatis.dao.TcScaleTemplateDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackageClasses = TcScaleTemplateDao.class)
@SpringBootApplication
public class TestMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestMybatisApplication.class, args);
    }
}
