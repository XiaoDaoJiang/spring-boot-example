package com.xiaodao.desensitization.core.regex.masker;

import com.xiaodao.desensitization.core.regex.annotation.RegexDesensitize;

/**
 * {@link RegexDesensitize} 的正则脱敏处理器
 *
 * 
 */
public class DefaultRegexMasker extends AbstractRegexMasker<RegexDesensitize> {

    @Override
    String getRegex(RegexDesensitize annotation) {
        return annotation.regex();
    }

    @Override
    String getReplacer(RegexDesensitize annotation) {
        return annotation.replacer();
    }
}
