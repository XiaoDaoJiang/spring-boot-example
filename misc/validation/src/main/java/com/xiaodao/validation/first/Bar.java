package com.xiaodao.validation.first;

import com.xiaodao.validation.ScriptConstraint;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jianghaitao
 * @Classname Foor
 * @Version 1.0.0
 * @Date 2024-10-30 14:11
 * @Created by jianghaitao
 */
@Accessors(chain = true)
@Data
public class Bar {

    // @ScriptConstraint(lang = "qlExpress", script = "barName.length() > 5", alias = "barName")
    private String barName;

    private String barAddress;

}
