package com.pms.pmsmodule.CommonLogger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of REST controller methods.
 *
 * <p>This class uses Spring AOP to intercept and log method entry and exit
 * for any class annotated with {@code @RestController}.</p>
 *
 * <p>It logs the method name, arguments, return value, and execution duration.</p>
 *
 * <p>The use of {@code @Around} advice enables pre- and post-execution logging in a single place.</p>
 *
 * @author DenisKinyua
 * @since 1.0
 */

@Aspect
@Component
public class CommonLoggerAspect {

    private static final Logger log = LoggerFactory.getLogger(CommonLoggerAspect.class);

    /**
     * Logs the execution details of controller methods.
     *
     * <p>This advice applies to all methods within classes annotated with {@code @RestController}.
     * It logs method name, input parameters, output result, and execution time in milliseconds.</p>
     *
     * @param joinPoint the join point providing reflective access to the target method
     * @return the result of the method execution
     * @throws Throwable if the underlying method throws any exception
     */
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // Log method entry with parameters
        log.info("➡️ [Controller] Entering: {}.{}() with arguments: {}", className, methodName, args);


        long start = System.currentTimeMillis();

        // Proceed with the method execution
        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;

        // Log method exit with return value and execution time
        log.info("⬅️ [Controller] Exiting: {}.{}() with result: {} (Execution time: {} ms)",
                className, methodName, result, duration);

        return result;
    }
}
