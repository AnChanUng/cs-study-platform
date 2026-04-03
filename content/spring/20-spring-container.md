---
title: 스프링 컨테이너와 빈 생명주기를 설명해주세요
category: spring
difficulty: INTERMEDIATE
tags: Container, Bean, Lifecycle
---

## 질문

스프링 컨테이너란 무엇이며, 빈의 생명주기는 어떻게 되나요?

## 답변

스프링 컨테이너는 빈(Bean)의 생성, 의존성 주입, 관리, 소멸까지 전체 생명주기를 담당하는 핵심 모듈입니다.

컨테이너 종류:
- BeanFactory: 가장 기본적인 컨테이너, 지연 로딩
- ApplicationContext: BeanFactory를 확장, 즉시 로딩, 이벤트 발행, 메시지 소스, AOP 등 지원
- 실무에서는 ApplicationContext를 사용

빈 생명주기:
1. 스프링 컨테이너 생성
2. 빈 객체 생성 (new)
3. 의존성 주입 (DI)
4. 초기화 콜백 (@PostConstruct)
5. 사용
6. 소멸 콜백 (@PreDestroy)
7. 스프링 컨테이너 종료

초기화/소멸 콜백 방법:
1. @PostConstruct / @PreDestroy (권장)
2. InitializingBean / DisposableBean 인터페이스
3. @Bean(initMethod, destroyMethod)

빈 스코프:
- singleton (기본): 컨테이너에 1개만 존재
- prototype: 요청할 때마다 새로 생성
- request: HTTP 요청마다 생성 (웹)
- session: HTTP 세션마다 생성 (웹)

싱글톤이 기본인 이유:
- 매번 새 객체를 생성하면 메모리 낭비
- 대부분의 서비스는 상태를 갖지 않으므로(stateless) 공유 가능
- 주의: 싱글톤 빈에 상태(멤버 변수)를 저장하면 동시성 문제 발생
