package com.xiaodao.translation.core.translator;

import cn.hutool.core.map.BiMap;
import com.xiaodao.translation.core.TranslateResult;
import com.xiaodao.translation.core.annotation.IndexDicField;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IndexTranslator extends AbstractTranslator<IndexDicField> {

    Map<String, BiMap<String, String>> dicMap = new ConcurrentHashMap<>(16);

    @Override
    public TranslateResult translate(String src, IndexDicField type) {
        final BiMap<String, String> indexBiMap = dicMap.computeIfAbsent(type.dicField().dicType(), k -> {
            BiMap<String, String> biMap = new BiMap<>(new HashMap<>(8, 1));
            for (int i = 0; i < type.dicKeys().length; i++) {
                biMap.put(type.dicKeys()[i], type.dicValues()[i]);
            }
            return biMap;
        });

        final String mappingValue = indexBiMap.getOrDefault(src, indexBiMap.getKey(src));
        if (mappingValue != null) {
            return TranslateResult.of(getFiledName(type.dicField()), mappingValue);
        }

        return defaultResult(type.dicField());
    }

}
