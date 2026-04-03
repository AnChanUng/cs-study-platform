---
title: "함수형 프로그래밍"
category: software-engineering
difficulty: INTERMEDIATE
tags: "함수형 프로그래밍, 순수 함수, 불변성, 고차 함수, 람다"
---

## 질문
함수형 프로그래밍이란 무엇이며, 객체 지향 프로그래밍과의 차이점을 설명해주세요.

## 핵심 키워드
- 순수 함수 (Pure Function)
- 불변성 (Immutability)
- 고차 함수 (Higher-order Function)
- 부수 효과 (Side Effect)
- 일급 시민 (First-class Citizen)

## 답변
함수형 프로그래밍(Functional Programming)은 순수 함수를 조합하여 프로그램을 구성하는 프로그래밍 패러다임입니다. 상태 변경과 부수 효과를 최소화하여 코드의 예측 가능성을 높입니다.

### 핵심 개념

**1. 순수 함수 (Pure Function)**
같은 입력에 항상 같은 출력을 반환하며, 부수 효과가 없는 함수입니다.

```java
// 순수 함수
int add(int a, int b) {
    return a + b; // 항상 같은 결과, 외부 상태 변경 없음
}

// 비순수 함수
int count = 0;
int addAndCount(int a, int b) {
    count++; // 외부 상태 변경 (부수 효과)
    return a + b;
}
```

**2. 불변성 (Immutability)**
데이터를 변경하지 않고, 새로운 데이터를 생성합니다.

```java
// 가변 (OOP 스타일)
List<Integer> list = new ArrayList<>();
list.add(1); // 기존 리스트 수정

// 불변 (FP 스타일)
List<Integer> newList = List.of(1, 2, 3);
List<Integer> result = Stream.concat(newList.stream(), Stream.of(4))
    .collect(Collectors.toList()); // 새 리스트 생성
```

**3. 고차 함수 (Higher-order Function)**
함수를 인자로 받거나 함수를 반환하는 함수입니다.

```java
// 함수를 인자로 받음
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0) // 함수(람다)를 인자로 전달
    .collect(Collectors.toList());

// 함수를 반환
Function<Integer, Function<Integer, Integer>> adder = a -> b -> a + b;
Function<Integer, Integer> add5 = adder.apply(5);
add5.apply(3); // 8
```

**4. 일급 시민 (First-class Citizen)**
함수를 변수에 할당하고, 인자로 전달하고, 반환값으로 사용할 수 있습니다.

```java
Function<Integer, Integer> square = x -> x * x;
Function<Integer, Integer> doubleIt = x -> x * 2;

// 함수 합성
Function<Integer, Integer> squareThenDouble = square.andThen(doubleIt);
squareThenDouble.apply(3); // 18 (3^2 = 9, 9*2 = 18)
```

### OOP vs FP

| 구분 | OOP | FP |
|------|-----|-----|
| 중심 | 객체 | 함수 |
| 상태 | 가변 상태 | 불변 상태 |
| 부수 효과 | 허용 | 최소화 |
| 추상화 | 데이터 추상화 | 행위 추상화 |
| 동시성 | 동기화 필요 | 자연스러운 동시성 |

### Java에서의 함수형 프로그래밍

```java
// Stream API + Lambda
List<String> names = users.stream()
    .filter(u -> u.getAge() > 20)
    .map(User::getName)
    .sorted()
    .collect(Collectors.toList());

// Optional: null 안전한 체이닝
Optional.ofNullable(user)
    .map(User::getAddress)
    .map(Address::getCity)
    .orElse("Unknown");
```

### 면접 팁
현대 프로그래밍에서는 OOP와 FP를 함께 사용하는 멀티 패러다임이 일반적입니다. Java 8+ Stream API, Optional, Lambda 등을 활용한 경험을 구체적으로 설명하세요.

## 꼬리 질문
1. 불변성이 동시성 프로그래밍에서 유리한 이유는?
2. Java의 Stream API에서 함수형 프로그래밍 원칙은?
3. 모나드(Monad)란 무엇인가요?
