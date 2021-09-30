package com.xiaodao.springbootjpa.config;

import com.xiaodao.springbootjpa.annotation.Master;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class DataSourceAop {


	/*@Pointcut("!@annotation(com.xiaodao.mybatismultidatasource.annotation.Master) " +
			"&& (execution(* com.xiaodao.mybatismultidatasource.service..*.select*(..)) " +
			"|| execution(* com.xiaodao.mybatismultidatasource.service..*.get*(..)))")
	public void readPointcut() {

	}

	@Pointcut("@annotation(com.xiaodao.mybatismultidatasource.annotation.Master) " +
			"|| execution(* com.xiaodao.mybatismultidatasource.service..*.insert*(..)) " +
			"|| execution(* com.xiaodao.mybatismultidatasource.service..*.add*(..)) " +
			"|| execution(* com.xiaodao.mybatismultidatasource.service..*.update*(..)) " +
			"|| execution(* com.xiaodao.mybatismultidatasource.service..*.edit*(..)) " +
			"|| execution(* com.xiaodao.mybatismultidatasource.service..*.delete*(..)) " +
			"|| execution(* com.xiaodao.mybatismultidatasource.service..*.remove*(..))")
	public void writePointcut() {

	}

	@Before("readPointcut()")
	public void read() {
		DBContextHolder.slave();
	}

	@Before("writePointcut()")
	public void write() {
		DBContextHolder.master();
	}*/

	/*@Pointcut("@annotation(com.xiaodao.mybatismultidatasource.annotation.Master)")
	public void writePointcut() {

	}

	@Before("writePointcut()")
	public void write() {
		DBContextHolder.master();
	}*/


	/**
	 * 另一种写法：if...else... 判断哪些需要读从数据库，其余的走主数据库
	 */
	@Before("execution(* com.xiaodao..service.impl.*.*(..))")
	public void before(JoinPoint jp) {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		String methodName = signature.getName();

		if (signature.getMethod().isAnnotationPresent(Master.class) || !StringUtils.startsWithAny(methodName, "get", "select", "find")) {
			DBContextHolder.master();
		} else {
			DBContextHolder.slave();
		}
	}

	@After("execution(* com.xiaodao..service.impl.*.*(..))")
	public void after(JoinPoint jp) {
		DBContextHolder.clearDataSourceKey();
	}
}