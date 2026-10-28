package csc.zjzw.csc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* csc.zjzw.csc.service..*(..))")
    public void servicePointcut() {
    }
    @Around("servicePointcut()")
    public Object logCost(ProceedingJoinPoint pjp) throws Throwable{
        String method = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();
        try {
            // 🔑 执行目标方法 —— 不调用它，方法【根本不会执行】
            Object result = pjp.proceed();

            long cost = System.currentTimeMillis() - start;
            logger.info("[AOP] {} 执行成功，耗时 {} ms", method, cost);
            return result;

        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - start;
            logger.warn("[AOP] {} 执行异常，耗时 {} ms，异常：{}", method, cost, e.getClass().getSimpleName());
            throw e;    // 🔑 必须重新抛出！否则异常被吞掉
        }
    }
}
