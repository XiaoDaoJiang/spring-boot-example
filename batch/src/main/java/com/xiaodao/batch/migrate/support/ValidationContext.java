package com.xiaodao.batch.migrate.support;

import lombok.Data;

@Data
public class ValidationContext {
    int totalCount;
    int successCount;
    int invalidCount;
}
