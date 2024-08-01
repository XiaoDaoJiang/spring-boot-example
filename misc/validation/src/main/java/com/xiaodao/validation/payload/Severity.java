package com.xiaodao.validation.payload;

import javax.validation.Payload;

public interface Severity extends Payload {

    String getMsgPrefix();

    class Info implements Severity {
        public static String msgPrefix = "info:";

        @Override
        public String getMsgPrefix() {
            return msgPrefix;
        }
    }

    class Error implements Severity {
        public static String msgPrefix = "error:";

        @Override
        public String getMsgPrefix() {
            return msgPrefix;
        }
    }
}
