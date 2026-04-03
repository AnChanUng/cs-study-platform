---
title: "오토 박싱 & 오토언박싱"
category: java
difficulty: BASIC
tags: "오토박싱, 오토언박싱, Wrapper 클래스, 기본 타입, 성능"
---

## 질문
오토 박싱과 오토언박싱이란 무엇이며, 주의사항은 무엇인가요?

## 핵심 키워드
- 기본 타입 (Primitive Type)
- 래퍼 클래스 (Wrapper Class)
- 오토박싱 (Auto-boxing)
- 오토언박싱 (Auto-unboxing)
- 성능 이슈

## 답변

### 오토박싱 (Auto-boxing)
기본 타입을 래퍼 클래스로 자동 변환하는 것입니다.

```java
int primitive = 10;
Integer wrapper = primitive; // 오토박싱: int → Integer
// 내부적으로: Integer wrapper = Integer.valueOf(primitive);
```

### 오토언박싱 (Auto-unboxing)
래퍼 클래스를 기본 타입으로 자동 변환하는 것입니다.

```java
Integer wrapper = Integer.valueOf(20);
int primitive = wrapper; // 오토언박싱: Integer → int
// 내부적으로: int primitive = wrapper.intValue();
```

### 기본 타입과 래퍼 클래스 대응

| 기본 타입 | 래퍼 클래스 |
|-----------|-----------|
| byte | Byte |
| short | Short |
| int | Integer |
| long | Long |
| float | Float |
| double | Double |
| char | Character |
| boolean | Boolean |

### 주의사항

**1. 성능 이슈**
```java
// Bad: 오토박싱으로 인한 성능 저하
Long sum = 0L;
for (long i = 0; i < 1000000; i++) {
    sum += i; // 매번 오토박싱 발생! Long 객체 생성
}

// Good: 기본 타입 사용
long sum = 0L;
for (long i = 0; i < 1000000; i++) {
    sum += i; // 기본 타입 연산
}
```

**2. == 비교 주의**
```java
Integer a = 127;
Integer b = 127;
System.out.println(a == b);     // true (Integer 캐시: -128 ~ 127)

Integer c = 128;
Integer d = 128;
System.out.println(c == d);     // false! (캐시 범위 밖)
System.out.println(c.equals(d)); // true (값 비교)
```

**3. NullPointerException**
```java
Integer wrapper = null;
int primitive = wrapper; // NPE! null을 기본 타입으로 변환 불가
```

**4. 컬렉션에서의 사용**
```java
// 컬렉션은 기본 타입을 직접 저장 불가
List<Integer> list = new ArrayList<>(); // Integer 사용
list.add(10); // 오토박싱
int value = list.get(0); // 오토언박싱
```

### 면접 팁
Integer 캐시(-128~127)로 인해 `==` 비교 결과가 달라지는 것은 자주 출제됩니다. 래퍼 클래스 비교 시 반드시 `equals()`를 사용해야 합니다. 반복문에서 오토박싱으로 인한 성능 저하도 중요한 포인트입니다.

## 꼬리 질문
1. Integer 캐시(IntegerCache)의 범위와 동작 원리는?
2. 래퍼 클래스를 사용해야 하는 경우는 언제인가요?
3. Optional.of()와 Optional.ofNullable()의 차이는?
