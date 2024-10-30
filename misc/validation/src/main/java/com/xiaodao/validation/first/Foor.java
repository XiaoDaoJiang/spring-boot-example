package com.xiaodao.validation.first;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ScriptAssert;

/**
 * @author jianghaitao
 * @Classname Foor
 * @Version 1.0.0
 * @Date 2024-10-30 14:11
 * @Created by jianghaitao
 */
@Accessors(chain = true)
@Data
// @ScriptAssert(
//         lang = "javascript",  // 使用JavaScript脚本语言
//         script = "_this.password == _this.confirmPassword", // 校验逻辑
//         message = "Passwords do not match"
// )
@ScriptAssert(lang = "groovy", script = "_this.foorName!=null && _this.foorName.length()>2",
        message = "Express orders must have a delivery date")
public class Foor {

    private String foorName;

}
