package com.xiaodao.javaparser.dto;

import lombok.Data;

import java.util.List;

/**
 * 学生
 *
 * @author jianghaitao
 * @Classname StudentDTO
 * @Version 1.0.0
 * @Date 2024-04-18 09:35
 * @Created by jianghaitao
 */
@Data
public class StudentDTO {

    private Integer studentId;
    private String name;
    private int age;
    private String gender;

    private Integer classId;

    private List<Integer> subjectIds;

}
