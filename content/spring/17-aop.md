---
title: AOP(관점 지향 프로그래밍)란 무엇인가요?
category: spring
difficulty: BASIC
tags: AOP, Aspect, Proxy
---

## 질문

AOP(Aspect-Oriented Programming)란 무엇이며, 스프링에서 어떻게 동작하나요?

## 답변

AOP(관점 지향 프로그래밍)는 핵심 비즈니스 로직과 공통 관심사(로깅, 보안, 트랜잭션 등)를 분리하는 프로그래밍 기법입니다.

왜 필요한가:
여러 클래스에 반복되는 코드(로깅, 인증 체크, 트랜잭션 처리 등)를 매번 작성하면 코드가 지저분해집니다. AOP를 사용하면 이런 횡단 관심사를 한 곳에서 관리할 수 있습니다.

핵심 용어:
- Aspect: 공통 관심사를 모듈화한 것 (예: 로깅 Aspect)
- Advice: 실제 실행되는 코드 (@Before, @After, @Around 등)
- JoinPoint: Advice가 적용될 수 있는 지점 (메서드 호출 등)
- Pointcut: 어떤 JoinPoint에 Advice를 적용할지 정하는 표현식
- Weaving: Aspect를 실제 코드에 적용하는 과정

Advice 종류:
- @Before: 메서드 실행 전
- @After: 메서드 실행 후 (성공/실패 무관)
- @AfterReturning: 정상 리턴 후
- @AfterThrowing: 예외 발생 후
- @Around: 메서드 전후 모두 제어 (가장 강력)

Spring AOP의 동작 원리:
- 프록시 패턴 기반 (JDK Dynamic Proxy 또는 CGLIB)
- 메서드 레벨에서만 AOP 적용 가능
- @Transactional, @Cacheable, @Secured 모두 AOP 기반으로 동작

AspectJ와의 차이:
- Spring AOP: 런타임 프록시 방식, 메서드만 지원
- AspectJ: 컴파일/로드 타임 위빙, 필드·생성자 등도 지원
