---
title: @Component, @Service, @Repository, @Controller 차이는?
category: spring
difficulty: BASIC
tags: Component, Annotation, Bean
---

## 질문

@Component, @Service, @Repository, @Controller 어노테이션의 차이점은 무엇인가요?

## 답변

4개 모두 스프링 빈으로 등록하는 어노테이션이며, @Component를 기반으로 한 파생 어노테이션입니다.

@Component:
- 가장 기본적인 빈 등록 어노테이션
- "이 클래스를 스프링이 관리해줘"라는 의미
- 특정 역할이 정해지지 않은 일반 컴포넌트에 사용

@Service:
- 비즈니스 로직을 담당하는 서비스 계층에 사용
- @Component와 기능적 차이는 없지만 역할을 명확히 구분
- 트랜잭션 처리의 기준이 되는 계층

@Repository:
- 데이터 접근 계층(DAO)에 사용
- DB 관련 예외를 스프링의 DataAccessException으로 자동 변환
- JPA, MyBatis 등과 함께 사용

@Controller:
- 웹 요청을 처리하는 컨트롤러 계층에 사용
- @RequestMapping과 함께 HTTP 요청을 매핑
- @RestController = @Controller + @ResponseBody

계층 구조:
Controller → Service → Repository
(요청 처리)  (비즈니스 로직)  (데이터 접근)

@ComponentScan:
- @SpringBootApplication에 포함된 어노테이션
- 지정된 패키지 하위의 @Component 계열 어노테이션을 자동 탐색하여 빈으로 등록
