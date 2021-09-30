package com.xiaodao.mybatismultidatasource.model;

import java.io.Serializable;
import lombok.Data;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    private Long id;

    private String name;

    private Byte age;

    private static final long serialVersionUID = 1L;
}