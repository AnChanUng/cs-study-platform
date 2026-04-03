---
title: "객체 지향 프로그래밍(OOP)"
category: software-engineering
difficulty: BASIC
tags: "OOP, 캡슐화, 상속, 다형성, 추상화"
---

## 질문
객체 지향 프로그래밍의 4가지 특징에 대해 설명해주세요.

## 핵심 키워드
- 캡슐화 (Encapsulation)
- 상속 (Inheritance)
- 다형성 (Polymorphism)
- 추상화 (Abstraction)
- SOLID 원칙

## 답변
객체 지향 프로그래밍(OOP, Object-Oriented Programming)은 프로그램을 객체(Object)들의 모임으로 바라보는 프로그래밍 패러다임입니다. 각 객체는 데이터(속성)와 기능(메서드)을 가지며, 객체 간 상호작용으로 프로그램이 동작합니다.

### 4대 특성

**1. 캡슐화 (Encapsulation)**
데이터와 메서드를 하나의 단위로 묶고, 외부에서 내부 구현을 숨기는 것입니다.

```java
public class Account {
    private int balance; // 외부 접근 차단

    public void deposit(int amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public int getBalance() {
        return this.balance;
    }
}
```

- 정보 은닉으로 데이터를 보호
- 접근 제어자(public, private, protected)로 제어
- 변경의 영향 범위를 최소화

**2. 상속 (Inheritance)**
기존 클래스의 속성과 메서드를 새로운 클래스가 물려받는 것입니다.

```java
public class Animal {
    protected String name;
    public void eat() { System.out.println("먹는다"); }
}

public class Dog extends Animal {
    public void bark() { System.out.println("멍멍"); }
}
```

- 코드 재사용성 향상
- 계층적 관계 표현
- 주의: 과도한 상속은 결합도를 높임 → "상속보다 조합(Composition)을 선호하라"

**3. 다형성 (Polymorphism)**
같은 인터페이스로 다른 동작을 수행하는 것입니다.

```java
// 오버라이딩 (런타임 다형성)
Animal dog = new Dog();
Animal cat = new Cat();
dog.speak(); // "멍멍"
cat.speak(); // "야옹"

// 오버로딩 (컴파일타임 다형성)
int add(int a, int b) { return a + b; }
double add(double a, double b) { return a + b; }
```

- 유연하고 확장 가능한 코드 작성
- 인터페이스 기반 프로그래밍의 핵심

**4. 추상화 (Abstraction)**
복잡한 시스템에서 핵심적인 부분만 모델링하는 것입니다.

```java
public interface Vehicle {
    void start();
    void stop();
    void accelerate(int speed);
}
// 내부 구현은 숨기고 필요한 기능만 정의
```

- 불필요한 세부사항을 숨김
- 인터페이스와 추상 클래스로 구현

### OOP의 장점
- 코드 재사용성 향상
- 유지보수 용이
- 대규모 소프트웨어 개발에 적합
- 현실 세계의 모델링이 자연스러움

### 면접 팁
4가지 특성을 단순 암기가 아닌 코드 예시와 함께 설명하세요. "상속보다 조합을 선호하라"는 원칙과 SOLID 원칙까지 연결하면 깊이 있는 답변이 됩니다.

## 꼬리 질문
1. 상속보다 조합(Composition)을 선호하는 이유는?
2. 추상 클래스와 인터페이스의 차이는?
3. SOLID 원칙에 대해 설명해주세요.
