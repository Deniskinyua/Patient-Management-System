package com.pms.pmsmodule.CommonLogger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CommonLoggerAspect {
    // Only log methods within classes annotated with @RestController
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("➡️ [Controller] Entering: {}.{}() with arguments: {}", className, methodName, args);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // continue to the actual method
        long duration = System.currentTimeMillis() - start;

        log.info("⬅️ [Controller] Exiting: {}.{}() with result: {} (Execution time: {} ms)",
                className, methodName, result, duration);

        return result;
    }
}
