### 拦截请求与响应完成日志记录
1. 不能重复多次读取 request InputStream，包装器模式，替换带缓存的 HttpServletRequestWrapper
   * Spring Web 实现 ContentCachingRequestWrapper
   * 自定义 [CacheRequestBodyWrapper.java](src%2Fmain%2Fjava%2Fcom%2Fxiaodao%2Ffilter%2Fdemo%2FCacheRequestBodyWrapper.java)
2. 拦截请求与响应
   * 基于过滤器（OncePerRequestFilter）
   * 拦截器（）
   * 请求：url/content-type/headers/params/body(包括form提交和json)
      * 请求参数主要封装为一个Map（query->xxx,body->……），最后保存对应的JSON字符串
   * 响应：
       
3. 读取请求与响应
   * 请求
      * query:工具类 ServletUtil.getParamMap(streamRequestWrapper)
      * body:工具类 cn.hutool.extra.servlet.ServletUtil.getBody
      * 实现 RequestBodyAdvice 类拦截请求body 
   * 响应
     * 实现 ResponseBodyAdvice 拦截返回 result<body> 公共结果类，并保存在请求上下文中
4. 日志脱敏
   注解+自定义化序列化器
5. MDC实现日志trace
   * SLFJ MDC （映射诊断上下文）
     * 跨线程传递
     主线程先将当前上下文copy（MDC.getCopyOfContextMap()）出来，跟随任务一起传递到子线程中，工作线程手动将副本关联（MDC.setContextMap(Map<String, String> contextMap)）
     > 在这种情况下，建议在向执行器提交任务之前在原始（主）线程上调用 MDC.getCopyOfContextMap()。当任务运行时，作为其第一个操作，它应该调用 MDC.setContextMap() 将原始 MDC 值的存储副本与新的 Executor 托管线程关联起来。


   * SkyWalking TraceContext.traceId()
   * Logback Appender layout Pattern
     ```xml
     <!-- 格式化输出：%d 表示日期，%X{tid} SkWalking 链路追踪编号，%thread 表示线程名，%-5level：级别从左显示 5 个字符宽度，%msg：日志消息，%n是换行符 -->
     <property name="PATTERN_DEFAULT" value="%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}} | %highlight(${LOG_LEVEL_PATTERN:-%5p} ${PID:- }) | %boldYellow(%thread [%tid]) %boldGreen(%-40.40logger{39}) | %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}"/>

     ```

### 各组件请求，响应处理顺序
1. Filter begin
2. RequestMappingHandlerMapping
3. HandlerInterceptor preHandle
4. request body advice 
   1. beforeBodyRead
   2. afterBodyRead
5. XXXController
6. ResponseBodyAdvice beforeBodyWrite
7. HandlerInterceptor postHandle
8. HandlerInterceptor afterCompletion
9. Filter End 