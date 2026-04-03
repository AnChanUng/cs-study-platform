---
title: "String & StringBuffer & StringBuilder"
category: java
difficulty: BASIC
tags: "String, StringBuffer, StringBuilder, 불변, 가변"
---

## 질문
String, StringBuffer, StringBuilder의 차이점에 대해 설명해주세요.

## 핵심 키워드
- 불변 (Immutable) vs 가변 (Mutable)
- String Pool
- 스레드 안전성
- 성능
- 문자열 연산

## 답변

### String - 불변 (Immutable)
String은 한 번 생성되면 값을 변경할 수 없습니다. 문자열을 조작하면 새로운 String 객체가 생성됩니다.

```java
String str = "Hello";
str = str + " World"; // 새로운 String 객체 생성, 기존 "Hello"는 GC 대상

// 내부적으로:
// "Hello" 객체 → 버려짐
// "Hello World" 객체 → str이 새로 참조
```

### StringBuffer - 가변, 스레드 안전
내부 버퍼를 사용하여 문자열을 변경합니다. synchronized 키워드로 스레드 안전성을 보장합니다.

```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World"); // 같은 객체 내에서 변경 (새 객체 생성 안 함)
System.out.println(sb.toString()); // "Hello World"
```

### StringBuilder - 가변, 스레드 비안전
StringBuffer와 동일하지만 동기화가 없어 단일 스레드 환경에서 더 빠릅니다.

```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
sb.insert(5, ",");
sb.delete(0, 5);
sb.reverse();
```

### 비교

| 특성 | String | StringBuffer | StringBuilder |
|------|--------|-------------|--------------|
| 가변성 | 불변 | 가변 | 가변 |
| 스레드 안전 | O (불변) | O (synchronized) | X |
| 성능 | 느림 (문자열 연산) | 중간 | 빠름 |
| 사용 상황 | 변경 적은 문자열 | 멀티스레드 | 단일 스레드 |

### 성능 비교

```java
// Bad: 반복문에서 String 연결 → O(n^2)
String result = "";
for (int i = 0; i < 10000; i++) {
    result += i; // 매번 새 String 객체 생성
}

// Good: StringBuilder 사용 → O(n)
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

### String이 불변인 이유
1. **String Pool**: 같은 값의 String을 공유하여 메모리 절약
2. **보안**: 네트워크 연결, 파일 경로 등에 사용되는 문자열 보호
3. **해시코드 캐싱**: HashMap의 키로 사용 시 해시값을 캐싱 가능
4. **스레드 안전**: 불변이므로 동기화 불필요

### 면접 팁
"반복문에서 문자열을 연결할 때 String 대신 StringBuilder를 사용해야 하는 이유"는 매우 자주 출제됩니다. Java 컴파일러가 단순한 String 연결(+)을 StringBuilder로 최적화해주지만, 반복문 내에서는 매번 새 StringBuilder가 생성되므로 직접 사용해야 합니다.

## 꼬리 질문
1. String Pool이란 무엇인가요?
2. `new String("abc")`와 `"abc"`의 차이는?
3. Java 컴파일러가 String 연결을 최적화하는 방법은?
