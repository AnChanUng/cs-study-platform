---
title: "Primitive type & Reference type"
category: java
difficulty: BASIC
tags: "기본 타입, 참조 타입, 메모리, null, 성능"
---

## 질문
Java의 기본 타입(Primitive type)과 참조 타입(Reference type)의 차이점에 대해 설명해주세요.

## 핵심 키워드
- 8가지 기본 타입
- 스택 vs 힙 저장
- null 가능 여부
- 성능 차이
- 래퍼 클래스

## 답변

### 기본 타입 (Primitive Type) - 8가지

| 타입 | 크기 | 기본값 | 범위 |
|------|------|--------|------|
| byte | 1바이트 | 0 | -128 ~ 127 |
| short | 2바이트 | 0 | -32,768 ~ 32,767 |
| int | 4바이트 | 0 | 약 -21억 ~ 21억 |
| long | 8바이트 | 0L | 매우 큼 |
| float | 4바이트 | 0.0f | IEEE 754 |
| double | 8바이트 | 0.0d | IEEE 754 |
| char | 2바이트 | '\u0000' | 0 ~ 65,535 |
| boolean | 1비트* | false | true/false |

### 참조 타입 (Reference Type)

클래스, 인터페이스, 배열, 열거형 등 기본 타입을 제외한 모든 타입입니다.

```java
String str = "Hello";     // 참조 타입
int[] arr = {1, 2, 3};    // 배열 (참조 타입)
User user = new User();   // 클래스 (참조 타입)
List<Integer> list = new ArrayList<>(); // 인터페이스 (참조 타입)
```

### 주요 차이점

| 구분 | 기본 타입 | 참조 타입 |
|------|-----------|-----------|
| 저장 값 | 실제 값 | 메모리 주소(참조) |
| 저장 위치 | Stack | 참조는 Stack, 객체는 Heap |
| null | 불가 | 가능 |
| 기본값 | 0, false 등 | null |
| 제네릭 | 사용 불가 | 사용 가능 |
| 성능 | 빠름 | 느림 (간접 참조) |

### 메모리 구조

```java
int num = 10;              // Stack에 값 10 직접 저장
String str = "Hello";      // Stack에 참조값, Heap에 "Hello" 객체

Stack:           Heap:
┌──────────┐    ┌─────────────┐
│ num = 10 │    │ "Hello"     │
│ str = ───┼───→│             │
└──────────┘    └─────────────┘
```

### null 처리

```java
int num = 0;        // 기본 타입은 null 불가
Integer wrap = null; // 참조 타입은 null 가능

// NullPointerException 주의
String str = null;
str.length();       // NPE 발생!

// null-safe 처리
if (str != null) {
    str.length();
}

// Optional 사용 (권장)
Optional.ofNullable(str)
    .map(String::length)
    .orElse(0);
```

### 비교 연산

```java
// 기본 타입: == 으로 값 비교
int a = 10;
int b = 10;
a == b; // true (값 비교)

// 참조 타입: == 은 참조 비교, equals()로 값 비교
String s1 = new String("hello");
String s2 = new String("hello");
s1 == s2;       // false (참조 비교)
s1.equals(s2);  // true (값 비교)
```

### 면접 팁
기본 타입은 스택에 직접 저장되어 접근이 빠르고, 참조 타입은 힙에 객체가 저장되어 간접 참조 오버헤드가 있습니다. 성능이 중요한 경우 기본 타입을 사용하고, 제네릭이나 null이 필요한 경우 래퍼 클래스를 사용합니다.

## 꼬리 질문
1. 기본 타입을 제네릭에서 사용할 수 없는 이유는?
2. 기본 타입 배열과 참조 타입 배열의 메모리 차이는?
3. Java에서 unsigned 정수를 사용하는 방법은?
