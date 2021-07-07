package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/*
simple aspect -> 透過pointcut 匹配符合條件的 join point 並實作 advice
被pointcut 標記的class spring AOP庫 會將該class 動態代理(dynamic proxy), 把advice加入到class中, 並建立Bean
advice + class == 代理類別
 */

@Component
@Aspect
public class LogAspect {
//    切入點，匹配哪些class與subClass要實作共通工作，以execution為例

/*
    * com.example.demo.Service..*.*(..)
1. * 回傳值，若有指定，需給予完整套件路徑 -> com.example.demo.objResponse.ProductResponse
2. com.example.demo.Service..* 代表該套件與其子套件，若不想包含子套件，com.example.demo.Service.*
3. .* 方法名稱規則 -> 所有方法都可使用，若需匹配create開頭之方法，create*
4. (..)方法參數規則 -> 不限定參數，若限定一個字串，(String), 第一個為字串 其餘不限 -> (String, ..)
* 第一個為字串 第二個是ProductRequest -> (String, com.example.demo.objRequest.ProductRequest)
 */

    @Pointcut("execution(* com.example.demo.Service..*(..))")
    public void pointcut() {

    }

//    advice 共通工作
//    JoinPoint -> 被pointCut匹配到的方法 若不需獲取匹配方法的資訊，可以省略此處的args
//    used annotation to set the advice process time, the parameter set how the advice do for
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("=====before advice starts=====");
        System.out.println(getMethodName(joinPoint));
        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
        System.out.println("=====before advice ends=====");
    }

    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("=====after advice starts=====");
        System.out.println("=====after advice ends=====");
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("=====after returning advice starts=====");

        if (result != null) {
            System.out.println(result);
        }
        System.out.println("=====after returning advice ends=====");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("=====around advice starts=====");
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long spendTime = System.currentTimeMillis() - startTime;
        System.out.println("Time spend: " + spendTime);
        System.out.println("=====around advice ends=====");

        return result;
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
        System.out.println("=====after throwing advice starts=====");
        System.out.println(getMethodName(joinPoint));
        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
        System.out.println(throwable.getMessage());
        System.out.println("=====after throwing advice ends=====");
    }

    private String getMethodName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getName();
    }
}
