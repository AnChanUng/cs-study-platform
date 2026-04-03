---
title: "문자열 클래스"
category: java
difficulty: BASIC
tags: "String, 문자열, String Pool, intern, 불변 객체"
---

## 질문
Java의 String 클래스의 특징과 String Pool에 대해 설명해주세요.

## 핵심 키워드
- 불변 (Immutable)
- String Pool (String Constant Pool)
- intern()
- == vs equals()
- 메모리 효율

## 답변

### String의 불변성

String은 final 클래스이며, 내부 char 배열(Java 9+부터 byte 배열)도 final입니다. 한 번 생성되면 값을 변경할 수 없습니다.

```java
String str = "Hello";
str.concat(" World"); // 새로운 String 반환, str은 변경 안 됨
System.out.println(str); // "Hello"

str = str.concat(" World"); // 새 객체를 str에 재할당
System.out.println(str); // "Hello World"
```

### String Pool

JVM이 문자열 리터럴을 관리하기 위해 힙 영역에 유지하는 특별한 공간입니다. 동일한 문자열 리터럴은 하나의 인스턴스만 생성됩니다.

```java
String s1 = "Hello";       // String Pool에 저장
String s2 = "Hello";       // Pool에서 같은 인스턴스 재사용
String s3 = new String("Hello"); // Heap에 새 객체 생성

System.out.println(s1 == s2);     // true  (같은 참조)
System.out.println(s1 == s3);     // false (다른 참조)
System.out.println(s1.equals(s3)); // true  (같은 값)

// intern(): String Pool에 등록
String s4 = s3.intern();
System.out.println(s1 == s4);     // true (Pool의 같은 인스턴스)
```

### 문자열 비교

```java
// == : 참조(주소) 비교
// equals() : 값(내용) 비교

String a = "hello";
String b = "hello";
String c = new String("hello");

a == b           // true  (String Pool)
a == c           // false (다른 객체)
a.equals(c)      // true  (같은 값)
"hello".equals(a) // true  (NullPointerException 방지 팁)
```

### 주요 String 메서드

```java
String str = "Hello, World!";

str.length();          // 13
str.charAt(0);         // 'H'
str.substring(0, 5);   // "Hello"
str.indexOf("World");  // 7
str.contains("World"); // true
str.replace("World", "Java"); // "Hello, Java!"
str.toUpperCase();     // "HELLO, WORLD!"
str.trim();            // 앞뒤 공백 제거
str.split(",");        // ["Hello", " World!"]
str.isEmpty();         // false
str.isBlank();         // false (Java 11+)
```

### 문자열 생성 방법별 메모리

```
"Hello"          → String Pool에서 관리, 재사용
new String("Hello") → Heap에 새 객체 + Pool에도 "Hello" 존재
```

### 면접 팁
`==`과 `equals()`의 차이, String Pool의 동작 원리, String이 불변인 이유(보안, 캐싱, 스레드 안전)를 확실히 이해하세요. `new String("abc")`가 최대 몇 개의 객체를 생성하는지도 자주 출제됩니다.

## 꼬리 질문
1. `new String("abc")`는 최대 몇 개의 객체를 생성하나요?
2. String.intern()은 언제 사용하며, 주의사항은?
3. Java 9에서 String 내부 구현이 변경된 이유는?
