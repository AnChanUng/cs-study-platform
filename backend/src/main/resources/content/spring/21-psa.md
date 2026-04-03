---
title: PSA(서비스 추상화)란 무엇인가요?
category: spring
difficulty: INTERMEDIATE
tags: PSA, 추상화, Spring
---

## 질문

스프링의 PSA(Portable Service Abstraction)란 무엇인가요?

## 답변

PSA(서비스 추상화)는 특정 기술에 종속되지 않도록 추상화 계층을 제공하여, 기술이 바뀌어도 코드를 수정하지 않아도 되게 하는 스프링의 핵심 원칙입니다.

스프링 3대 핵심 기술:
1. IoC/DI (제어의 역전 / 의존성 주입)
2. AOP (관점 지향 프로그래밍)
3. PSA (서비스 추상화)

PSA 예시:

1. @Transactional:
- JDBC, JPA, Hibernate 어떤 기술을 사용하든 @Transactional 하나로 트랜잭션 관리
- 내부 구현이 바뀌어도 코드 변경 불필요

2. @Cacheable:
- EhCache, Redis, Caffeine 등 캐시 구현체가 바뀌어도 동일한 어노테이션 사용

3. Spring Web MVC:
- Servlet 기반이든 Reactive 기반이든 @Controller, @GetMapping 동일하게 사용

PSA의 장점:
- 기술 변경 시 코드 수정 최소화
- 테스트 용이 (추상화된 인터페이스로 Mock 가능)
- 학습 비용 감소 (일관된 API)
- 특정 벤더에 종속되지 않음

핵심 포인트:
PSA 덕분에 개발자는 "어떤 기술을 쓸 것인가"보다 "무엇을 할 것인가"에 집중할 수 있습니다. 설정만 바꾸면 구현 기술을 교체할 수 있는 유연성을 제공합니다.
