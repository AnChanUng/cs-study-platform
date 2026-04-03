---
title: "Casting(형변환)"
category: java
difficulty: BASIC
tags: "캐스팅, 업캐스팅, 다운캐스팅, 형변환, instanceof"
---

## 질문
Java에서 업캐스팅과 다운캐스팅에 대해 설명해주세요.

## 핵심 키워드
- 업캐스팅 (Upcasting)
- 다운캐스팅 (Downcasting)
- instanceof
- ClassCastException
- 다형성

## 답변
캐스팅(형변환)은 데이터의 타입을 변환하는 것입니다. Java에서 참조 타입의 캐스팅은 상속 관계에서 업캐스팅과 다운캐스팅으로 나뉩니다.

### 업캐스팅 (Upcasting)
하위 클래스 타입을 상위 클래스 타입으로 변환합니다. **자동(묵시적)**으로 수행됩니다.

```java
class Animal {
    void eat() { System.out.println("먹는다"); }
}

class Dog extends Animal {
    void bark() { System.out.println("멍멍"); }
}

// 업캐스팅: 자동 변환
Animal animal = new Dog(); // Dog → Animal
animal.eat();              // O - Animal의 메서드
// animal.bark();          // X - Animal 타입이므로 Dog의 메서드 접근 불가
```

### 다운캐스팅 (Downcasting)
상위 클래스 타입을 하위 클래스 타입으로 변환합니다. **명시적**으로 수행해야 합니다.

```java
Animal animal = new Dog(); // 업캐스팅
Dog dog = (Dog) animal;    // 다운캐스팅: 명시적 변환 필요
dog.bark();                // O - Dog의 메서드 사용 가능

// 잘못된 다운캐스팅 → ClassCastException
Animal animal2 = new Animal();
Dog dog2 = (Dog) animal2;  // 런타임 에러! Animal은 Dog가 아님
```

### instanceof로 안전한 다운캐스팅

```java
if (animal instanceof Dog) {
    Dog dog = (Dog) animal;
    dog.bark();
}

// Java 16+ Pattern Matching
if (animal instanceof Dog dog) {
    dog.bark(); // 자동 캐스팅
}
```

### 기본 타입 형변환

```java
// 자동 형변환 (Widening): 작은 → 큰
int intVal = 10;
double doubleVal = intVal; // int → double (자동)

// 강제 형변환 (Narrowing): 큰 → 작은
double d = 3.14;
int i = (int) d; // double → int (명시적, 소수점 손실)

// 형변환 순서
// byte → short → int → long → float → double
//         char ↗
```

### 면접 팁
업캐스팅은 다형성의 기반입니다. `Animal animal = new Dog()`에서 animal.eat()을 호출하면 Dog의 오버라이딩된 메서드가 실행된다는 점(런타임 다형성)을 설명할 수 있어야 합니다.

## 꼬리 질문
1. ClassCastException을 방지하는 방법은?
2. 업캐스팅 후 오버라이딩된 메서드는 어떤 것이 호출되나요?
3. 제네릭에서의 타입 캐스팅 문제는?
