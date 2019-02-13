package com.org.edwin.annotation.aop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.MessageProducer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.org.edwin.annotation.util.AppResult;



@Component
@Aspect
public class AppSignValidAspect {
	
	
	@Pointcut(value = "@annotation(com.org.edwin.annotation.customAnnotation.AppSignValid)")
	public void controllerAspect() {
		
	}
	
	@Around("controllerAspect()")
	public Object doBefore(final ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("aop");
		return joinPoint.proceed();
	}
	
	
	@AfterReturning(value = "controllerAspect()")
	public void after(final JoinPoint joinPoint) throws IOException {
	}
}
