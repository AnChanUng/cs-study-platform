---
title: "스트레티지 패턴(Strategy Pattern)"
category: spring
difficulty: INTERMEDIATE
tags: "전략 패턴, 디자인 패턴, 행위 패턴, DI, 알고리즘 교체"
---

## 질문
전략 패턴(Strategy Pattern)이란 무엇이며, Spring에서 어떻게 활용되나요?

## 핵심 키워드
- 알고리즘 캡슐화
- 런타임 전략 교체
- 인터페이스 기반
- 의존성 주입 (DI)
- OCP 원칙

## 답변
전략 패턴(Strategy Pattern)은 알고리즘 군을 정의하고, 각각을 캡슐화하여 상호 교환 가능하게 만드는 행위 패턴입니다. 실행 중에 알고리즘을 교체할 수 있습니다.

### 구현 예시: 할인 정책

```java
// 전략 인터페이스
public interface DiscountStrategy {
    int discount(int price);
    String getType();
}

// 구체적 전략들
@Component
public class FixedDiscount implements DiscountStrategy {
    public int discount(int price) { return price - 1000; }
    public String getType() { return "FIXED"; }
}

@Component
public class PercentDiscount implements DiscountStrategy {
    public int discount(int price) { return (int)(price * 0.9); }
    public String getType() { return "PERCENT"; }
}

@Component
public class NoDiscount implements DiscountStrategy {
    public int discount(int price) { return price; }
    public String getType() { return "NONE"; }
}

// 컨텍스트 (전략을 사용하는 클래스)
@Service
public class PaymentService {
    private final Map<String, DiscountStrategy> strategyMap;

    // Spring DI로 모든 전략을 자동 주입
    public PaymentService(List<DiscountStrategy> strategies) {
        this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(
                DiscountStrategy::getType,
                Function.identity()
            ));
    }

    public int calculatePrice(String discountType, int originalPrice) {
        DiscountStrategy strategy = strategyMap.getOrDefault(discountType,
            strategyMap.get("NONE"));
        return strategy.discount(originalPrice);
    }
}
```

### if-else를 전략 패턴으로 제거

```java
// Before: if-else 지옥
public int calculateDiscount(String type, int price) {
    if ("FIXED".equals(type)) {
        return price - 1000;
    } else if ("PERCENT".equals(type)) {
        return (int)(price * 0.9);
    } else if ("VIP".equals(type)) {
        return (int)(price * 0.8);
    }
    // 새 할인 타입 추가 시 여기를 수정해야 함 → OCP 위반
    return price;
}

// After: 전략 패턴
// 새 할인 타입 추가 시 새 클래스만 추가 → OCP 준수
@Component
public class VipDiscount implements DiscountStrategy {
    public int discount(int price) { return (int)(price * 0.8); }
    public String getType() { return "VIP"; }
}
```

### Spring에서의 전략 패턴 활용

Spring의 많은 부분이 전략 패턴으로 설계되어 있습니다:
- **TransactionManager**: PlatformTransactionManager 인터페이스의 다양한 구현체
- **ViewResolver**: 다양한 뷰 기술 지원
- **HandlerMapping**: 다양한 URL 매핑 전략
- **PasswordEncoder**: BCrypt, Argon2 등 다양한 암호화 전략

### 장점
- 알고리즘 교체가 자유로움 (런타임)
- if-else 조건문 제거
- OCP 준수 (새 전략 추가 시 기존 코드 수정 불필요)
- 테스트가 용이 (각 전략을 독립적으로 테스트)

### 면접 팁
전략 패턴은 Spring의 DI와 가장 잘 어울리는 패턴입니다. List나 Map으로 전략을 자동 주입받는 방법을 코드로 보여주면 실무적인 답변이 됩니다.

## 꼬리 질문
1. 전략 패턴과 팩토리 패턴의 차이는?
2. 전략 패턴에서 전략이 상태를 가져도 되나요?
3. Java의 Comparator가 전략 패턴의 예시인 이유는?
