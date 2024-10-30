package com.xiaodao.validation.mapping;

import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.cfg.defs.*;

import java.util.HashMap;
import java.util.Map;

public class ConstraintDefFactory {

    private static final Map<String, Class<? extends ConstraintDef<?, ?>>> constraintMap = new HashMap<>();

    static {
        constraintMap.put("NotNull", NotNullDef.class);
        constraintMap.put("Min", MinDef.class);
        constraintMap.put("Max", MaxDef.class);
        constraintMap.put("Size", SizeDef.class);
        constraintMap.put("AssertTrue", AssertTrueDef.class);
        // 添加更多内置约束或自定义约束
    }

    /*
    public static GenericConstraintDef<?> createConstraintDef(ValidationConfig.ConstraintDefinition def) throws Exception {
        Class<? extends ConstraintDef<?, ?>> defClass = constraintMap.get(def.getType());
        if (defClass == null) {
            throw new IllegalArgumentException("Unsupported constraint type: " + def.getType());
        }

        ConstraintDef<?, ?> constraintDef = defClass.getDeclaredConstructor().newInstance();

        // 设置属性
        if (def.getValue() != null) {
            if (constraintDef instanceof MinDef) {
                ((MinDef) constraintDef).value(Long.parseLong(def.getValue().toString()));
            }
            // 处理其他带参数的约束
            // 例如，SizeDef 的 min 和 max
            // 可以根据需要扩展
        }

        if (def.getMessage() != null) {
            constraintDef.message(def.getMessage());
        }

        // return constraintDef;
        return null;
    }
    */
}
