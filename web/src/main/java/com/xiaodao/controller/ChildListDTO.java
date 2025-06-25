package com.xiaodao.controller;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author jianghaitao
 * @Classname ParentListDTO
 * @Version 1.0.0
 * @Date 2025-06-24 16:08
 * @Created by jianghaitao
 */
@Data
public class ChildListDTO {

    private Integer id;

    private String name;

    private Date birth;

    private List<String> properties;



}
