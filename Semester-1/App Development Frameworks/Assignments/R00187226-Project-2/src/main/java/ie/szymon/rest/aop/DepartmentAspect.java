package ie.szymon.rest.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class DepartmentAspect {
    @Pointcut("execution(* ie.szymon.rest.repos.DepartmentRepo.*(..))")
    public void repoMethods() {
    }

    @Before("repoMethods()")
    public void theBeforeAction(JoinPoint joinPoint){
        log.info(joinPoint.getSignature().toShortString() + " with arguments " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "repoMethods()", returning = "returnValue")
    public void theAfterReturningAction(JoinPoint joinPoint, Object returnValue){
        log.info(joinPoint.getSignature().toShortString() + "\tReturn Value = " + returnValue);
    }
}
