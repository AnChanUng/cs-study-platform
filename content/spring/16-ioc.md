---
title: IoC(제어의 역전)란 무엇인가요?
category: spring
difficulty: BASIC
tags: IoC, DI, Spring Container
---

## 질문

IoC(Inversion of Control, 제어의 역전)란 무엇이며, 스프링에서 어떻게 구현되나요?

## 답변

IoC(제어의 역전)는 객체의 생성, 생명주기 관리 등의 제어권을 개발자가 아닌 프레임워크(스프링 컨테이너)가 담당하는 것을 말합니다.

기존 방식 (개발자가 직접 제어):
- new 키워드로 객체를 직접 생성
- 객체 간 의존 관계를 직접 설정

IoC 방식 (스프링이 제어):
- 스프링 컨테이너가 객체(Bean)를 생성하고 관리
- 필요한 곳에 자동으로 의존성 주입 (DI)

스프링에서의 구현:
1. @Component, @Service, @Repository 등으로 빈 등록
2. @ComponentScan으로 빈을 자동 탐색
3. @Autowired 또는 생성자 주입으로 의존성 주입

IoC의 장점:
- 객체 간 결합도 감소
- 코드 재사용성과 유연성 증가
- 단위 테스트 용이
- 객체의 생명주기를 프레임워크가 관리하므로 개발자는 비즈니스 로직에 집중 가능

IoC 컨테이너 = BeanFactory (기본) + ApplicationContext (확장)
ApplicationContext가 BeanFactory를 상속하며, 메시지 소스, 이벤트 발행 등 추가 기능을 제공합니다.
