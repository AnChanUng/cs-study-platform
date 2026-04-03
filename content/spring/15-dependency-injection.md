---
title: DI(의존성 주입)란 무엇인가요?
category: spring
difficulty: BASIC
tags: DI, IoC, Spring
---

## 질문

DI(Dependency Injection, 의존성 주입)란 무엇이며, 왜 사용하나요?

## 답변

DI(의존성 주입)는 객체가 필요로 하는 의존 객체를 직접 생성하지 않고, 외부(스프링 컨테이너)에서 주입받는 디자인 패턴입니다.

핵심 개념:
- 객체 간의 결합도를 낮추어 유연한 코드를 만들 수 있습니다.
- 테스트 시 Mock 객체로 쉽게 교체할 수 있어 단위 테스트가 용이합니다.
- 스프링에서는 IoC 컨테이너가 객체의 생성과 주입을 담당합니다.

주입 방식 3가지:
1. 생성자 주입 (권장): 불변성 보장, 순환 참조 방지
2. Setter 주입: 선택적 의존성에 사용
3. 필드 주입 (@Autowired): 간결하지만 테스트 어려움

생성자 주입 예시:
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}

스프링 공식 문서에서도 생성자 주입을 권장합니다. 필드 주입은 간편하지만 테스트가 어렵고 의존성이 숨겨지는 단점이 있습니다.
