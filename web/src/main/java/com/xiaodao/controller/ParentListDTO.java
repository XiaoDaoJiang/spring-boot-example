package com.xiaodao.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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
public class ParentListDTO {

    private Integer id;

    private String name;

    private Date birth;

    protected String other;

    private List<String> properties;

    @JsonUnwrapped
    protected ChildListDTO childListDTO;

    protected HelloController.User user = new HelloController.User();

    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.NONE
    )
    public static class ExtentParentListVO extends ParentListDTO{


    }
}
