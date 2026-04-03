---
title: AOP (Aspect-Oriented Programming)
category: Spring
date: 2026-04-04
---

## AOP란?

**관점 지향 프로그래밍(Aspect-Oriented Programming)**의 약자로, 핵심 비즈니스 로직과 공통 관심사(로깅, 보안, 트랜잭션 등)를 분리하는 프로그래밍 기법이다.

## 왜 필요한가?

여러 메서드에 반복되는 코드(로깅, 인증 체크 등)를 매번 복붙하면 코드가 지저분해진다. AOP를 쓰면 이런 **횡단 관심사(Cross-Cutting Concern)**를 한 곳에 모아서 관리할 수 있다.

## 핵심 용어

| 용어 | 설명 |
|------|------|
| **Aspect** | 공통 관심사를 모듈화한 것 (예: 로깅 Aspect) |
| **Advice** | 실제 실행되는 코드. 언제 실행되는지에 따라 종류가 나뉨 |
| **JoinPoint** | Advice가 적용될 수 있는 지점 (메서드 호출, 예외 발생 등) |
| **Pointcut** | 어떤 JoinPoint에 Advice를 적용할지 정하는 표현식 |
| **Weaving** | Aspect를 실제 코드에 적용하는 과정 |

## Advice 종류

- **@Before** — 메서드 실행 전
- **@After** — 메서드 실행 후 (성공/실패 무관)
- **@AfterReturning** — 메서드 정상 리턴 후
- **@AfterThrowing** — 예외 발생 후
- **@Around** — 메서드 실행 전후 모두 (가장 강력)

## Spring AOP 예시

```java
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.example.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 실제 메서드 실행
        long time = System.currentTimeMillis() - start;
        log.info("{} 실행 시간: {}ms", joinPoint.getSignature(), time);
        return result;
    }
}
```

## 면접 포인트

- AOP는 **프록시 패턴** 기반으로 동작한다 (Spring AOP = JDK Dynamic Proxy 또는 CGLIB)
- Spring AOP는 **메서드 레벨**에서만 동작 (AspectJ는 필드, 생성자 등도 가능)
- 대표적 활용: **@Transactional**, **@Cacheable**, **@Secured** 모두 AOP 기반
