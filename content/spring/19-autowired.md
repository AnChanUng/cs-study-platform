---
title: @Autowired는 어떻게 동작하나요?
category: spring
difficulty: BASIC
tags: Autowired, DI, 의존성주입
---

## 질문

@Autowired는 어떻게 동작하며, 주의할 점은 무엇인가요?

## 답변

@Autowired는 스프링 컨테이너에서 해당 타입의 빈을 찾아 자동으로 주입해주는 어노테이션입니다.

동작 원리:
1. 스프링 컨테이너가 빈을 생성
2. @Autowired가 붙은 곳을 찾음
3. 해당 타입의 빈을 컨테이너에서 검색
4. 찾은 빈을 자동으로 주입

주입 방식 3가지:

1. 필드 주입:
@Autowired
private OrderRepository orderRepository;
→ 간결하지만 테스트 어려움, 순환 참조 발견 늦음

2. Setter 주입:
@Autowired
public void setOrderRepository(OrderRepository repo) { ... }
→ 선택적 의존성에 적합

3. 생성자 주입 (권장):
public OrderService(OrderRepository orderRepository) { ... }
→ 불변성 보장, 컴파일 시점에 누락 확인, 순환 참조 즉시 발견
→ 생성자가 1개면 @Autowired 생략 가능

같은 타입의 빈이 여러 개일 때:
- @Primary: 우선순위 빈 지정
- @Qualifier("이름"): 특정 빈 이름으로 지정
- 타입 대신 이름으로 매칭

주의사항:
- 필드 주입은 스프링 공식적으로 권장하지 않음
- 순환 참조(A→B→A) 발생 시 생성자 주입은 즉시 에러, 필드 주입은 런타임에 발견
- 생성자 주입을 사용하면 final 키워드로 불변성을 보장할 수 있음
