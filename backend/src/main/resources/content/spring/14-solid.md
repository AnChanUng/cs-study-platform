---
title: "SOLID 원칙"
category: spring
difficulty: INTERMEDIATE
tags: "SOLID, 객체지향, SRP, OCP, LSP, ISP, DIP"
---

## 질문
SOLID 원칙이란 무엇이며, 각 원칙에 대해 설명해주세요.

## 핵심 키워드
- 단일 책임 원칙 (SRP)
- 개방-폐쇄 원칙 (OCP)
- 리스코프 치환 원칙 (LSP)
- 인터페이스 분리 원칙 (ISP)
- 의존 역전 원칙 (DIP)

## 답변
SOLID는 로버트 마틴(Uncle Bob)이 정리한 객체 지향 설계의 5가지 원칙입니다. 이 원칙들을 준수하면 유지보수가 쉽고 확장 가능한 소프트웨어를 설계할 수 있습니다.

### 1. SRP (Single Responsibility Principle) - 단일 책임 원칙

하나의 클래스는 하나의 책임만 가져야 합니다.

```java
// Bad: 여러 책임
public class UserService {
    public void createUser(User user) { ... }
    public void sendEmail(String email) { ... } // 이메일 발송은 별도 책임
    public String exportToCsv(List<User> users) { ... } // 내보내기도 별도
}

// Good: 책임 분리
public class UserService { public void createUser(User user) { ... } }
public class EmailService { public void sendEmail(String email) { ... } }
public class UserExporter { public String exportToCsv(List<User> users) { ... } }
```

### 2. OCP (Open-Closed Principle) - 개방-폐쇄 원칙

확장에는 열려 있고, 변경에는 닫혀 있어야 합니다.

```java
// Bad: 새 할인 타입 추가 시 기존 코드 수정 필요
public int calculateDiscount(String type, int price) {
    if ("FIXED".equals(type)) return price - 1000;
    else if ("PERCENT".equals(type)) return (int)(price * 0.9);
    // 새 타입 추가 시 여기를 수정해야 함
}

// Good: 인터페이스 + 새 구현체 추가로 확장
public interface DiscountPolicy {
    int discount(int price);
}
public class FixedDiscount implements DiscountPolicy { ... }
public class PercentDiscount implements DiscountPolicy { ... }
// 새 할인: 기존 코드 수정 없이 새 클래스만 추가
```

### 3. LSP (Liskov Substitution Principle) - 리스코프 치환 원칙

하위 타입은 상위 타입을 대체할 수 있어야 합니다.

```java
// Bad: 정사각형이 직사각형을 상속하면 LSP 위반
class Rectangle {
    void setWidth(int w) { this.width = w; }
    void setHeight(int h) { this.height = h; }
}
class Square extends Rectangle {
    void setWidth(int w) { this.width = w; this.height = w; } // 예상과 다른 동작
}

// Rectangle을 기대하는 코드에서 Square를 넣으면 오작동
```

### 4. ISP (Interface Segregation Principle) - 인터페이스 분리 원칙

클라이언트가 자신이 사용하지 않는 메서드에 의존하지 않아야 합니다.

```java
// Bad: 하나의 큰 인터페이스
public interface Worker {
    void work();
    void eat();
    void sleep();
}
// 로봇은 eat(), sleep()이 불필요

// Good: 인터페이스 분리
public interface Workable { void work(); }
public interface Eatable { void eat(); }
public interface Sleepable { void sleep(); }

public class Human implements Workable, Eatable, Sleepable { ... }
public class Robot implements Workable { ... }
```

### 5. DIP (Dependency Inversion Principle) - 의존 역전 원칙

고수준 모듈이 저수준 모듈에 의존하지 않고, 둘 다 추상화에 의존해야 합니다.

```java
// Bad: 구체 클래스에 직접 의존
public class OrderService {
    private MySqlOrderRepository repository = new MySqlOrderRepository();
}

// Good: 추상화(인터페이스)에 의존
public class OrderService {
    private final OrderRepository repository; // 인터페이스에 의존

    public OrderService(OrderRepository repository) {
        this.repository = repository; // DI로 주입
    }
}
```

Spring의 DI(Dependency Injection)는 DIP를 자연스럽게 구현할 수 있게 해줍니다.

### SOLID와 Spring

| 원칙 | Spring에서의 적용 |
|------|-----------------|
| SRP | @Service, @Repository 등 계층 분리 |
| OCP | 인터페이스 + 다양한 구현체 |
| LSP | 인터페이스 기반 프로그래밍 |
| ISP | 작은 인터페이스 설계 |
| DIP | @Autowired, 생성자 주입 |

### 면접 팁
각 원칙을 코드 예시와 함께 설명하고, Spring에서 어떻게 적용되는지 연결하세요. 특히 OCP와 DIP는 Spring의 핵심 철학과 밀접한 관련이 있습니다.

## 꼬리 질문
1. SOLID 원칙 중 가장 중요하다고 생각하는 원칙과 그 이유는?
2. Spring의 DI가 DIP를 어떻게 구현하나요?
3. 실무에서 SOLID 원칙을 위반한 코드를 리팩토링한 경험은?
