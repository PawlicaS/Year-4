//package ie.szymon.rest.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//@Aspect
//@Slf4j
//@Component
//public class Aspects {
//    @Pointcut("execution(* ie.szymon.repo.*(..))")
//    public void xRepoMethods() {
//    }
//
//    @Before("xRepoMethods()")
//    public void theBeforeAction(JoinPoint joinPoint){
//        log.info(joinPoint.getSignature().toShortString() + " with arguments " + Arrays.toString(joinPoint.getArgs()));
//    }
//
//    @AfterReturning(pointcut = "xRepoMethods()", returning = "returnValue")
//    public void theAfterReturningAction(JoinPoint joinPoint, Object returnValue){
//        log.info(joinPoint.getSignature().toShortString() + "\tReturn Value = " + returnValue);
//    }
//}
