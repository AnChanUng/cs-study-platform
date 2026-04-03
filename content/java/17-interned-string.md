---
title: "Interned String"
category: java
difficulty: INTERMEDIATE
tags: "String intern, String Pool, 메모리 최적화, 문자열 비교"
---

## 질문
Java의 String.intern() 메서드와 Interned String에 대해 설명해주세요.

## 핵심 키워드
- String Pool
- intern() 메서드
- 메모리 최적화
- == vs equals()
- 성능 트레이드오프

## 답변
String.intern()은 문자열을 String Pool에 등록하고, Pool에 이미 같은 값이 있으면 그 참조를 반환하는 메서드입니다.

### intern() 동작 원리

```java
String s1 = new String("hello"); // Heap에 새 객체 생성
String s2 = s1.intern();         // String Pool에 "hello" 등록 (이미 있으면 기존 참조 반환)
String s3 = "hello";             // String Pool의 "hello" 참조

System.out.println(s1 == s2); // false (s1은 Heap, s2는 Pool)
System.out.println(s2 == s3); // true  (둘 다 Pool의 같은 참조)
```

### 메모리 구조

```
리터럴 방식: "hello"
→ String Pool에서 관리, 같은 리터럴은 같은 참조

new 방식: new String("hello")
→ Heap에 새 객체 생성 + Pool에도 "hello" 존재

intern() 호출:
→ Pool에 해당 문자열이 있으면 Pool 참조 반환
→ 없으면 Pool에 추가 후 참조 반환
```

### 활용 사례

```java
// 동일한 문자열이 대량으로 생성되는 경우 메모리 절약
Map<String, List<Record>> groupByCity = new HashMap<>();

for (Record record : records) {
    String city = record.getCity().intern(); // Pool에서 재사용
    groupByCity.computeIfAbsent(city, k -> new ArrayList<>()).add(record);
}
// 같은 도시명이 수만 번 반복되면 메모리 절약 효과 큼
```

### 주의사항

**1. 성능 오버헤드**
- intern()은 Pool 검색에 시간이 소요됩니다
- 대량 호출 시 오히려 성능 저하 가능

**2. 메모리 누수 가능성**
- Java 7 이전: String Pool이 PermGen에 위치 → 크기 제한
- Java 7 이후: String Pool이 Heap에 위치 → GC 대상

**3. 동등성 비교에 intern() 사용은 권장하지 않음**
```java
// Bad: intern()으로 == 비교
if (input.intern() == "expected") { ... }

// Good: equals() 사용
if ("expected".equals(input)) { ... }
```

### String Pool 내부

Java 7+에서 String Pool은 HashMap 기반의 해시 테이블로 구현되어 있습니다.

```bash
# Pool 크기 조정 (JVM 옵션)
-XX:StringTableSize=60013  # 기본값: 60013 (소수)
```

### 면접 팁
intern()은 메모리 최적화를 위한 도구이지만, 무분별한 사용은 오히려 성능을 저하시킵니다. 문자열 비교에는 항상 equals()를 사용하는 것이 올바른 방법입니다. Java의 문자열 리터럴이 자동으로 intern된다는 점도 기억하세요.

## 꼬리 질문
1. 문자열 리터럴이 자동으로 intern되는 이유는?
2. Java 7 전후로 String Pool 위치가 변경된 이유는?
3. String deduplication(G1 GC)이란 무엇인가요?
