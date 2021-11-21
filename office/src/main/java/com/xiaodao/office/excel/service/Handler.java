package com.xiaodao.office.excel.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface Handler<T> {

    List<Future<List<T>>> handle(List<T> uploadDataList, Map<String, String> dic);

}
