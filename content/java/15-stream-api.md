---
title: "Stream API"
category: java
difficulty: INTERMEDIATE
tags: "Stream, 람다, 함수형, 중간 연산, 최종 연산"
---

## 질문
Java의 Stream API에 대해 설명하고, 주요 연산에 대해 설명해주세요.

## 핵심 키워드
- Stream 생성
- 중간 연산 (filter, map, sorted)
- 최종 연산 (collect, forEach, reduce)
- 지연 평가 (Lazy Evaluation)
- 병렬 스트림

## 답변
Stream API는 Java 8에서 도입된 함수형 스타일의 데이터 처리 API입니다. 컬렉션 데이터를 선언적으로 처리할 수 있으며, 내부 반복을 사용합니다.

### 기본 사용

```java
List<String> names = Arrays.asList("김철수", "이영희", "박민수", "김영수", "이수진");

List<String> result = names.stream()           // 스트림 생성
    .filter(name -> name.startsWith("김"))     // 중간 연산: 필터링
    .map(String::toUpperCase)                  // 중간 연산: 변환
    .sorted()                                  // 중간 연산: 정렬
    .collect(Collectors.toList());             // 최종 연산: 수집
```

### 주요 중간 연산

```java
// filter: 조건에 맞는 요소만 선택
stream.filter(x -> x > 10)

// map: 각 요소를 변환
stream.map(user -> user.getName())
stream.map(User::getName)  // 메서드 참조

// flatMap: 중첩 구조를 평탄화
stream.flatMap(list -> list.stream())

// sorted: 정렬
stream.sorted()
stream.sorted(Comparator.comparing(User::getAge).reversed())

// distinct: 중복 제거
stream.distinct()

// peek: 중간에 처리 (디버깅용)
stream.peek(System.out::println)

// limit / skip
stream.limit(5)  // 처음 5개
stream.skip(3)   // 처음 3개 건너뜀
```

### 주요 최종 연산

```java
// collect: 결과를 컬렉션으로 수집
List<String> list = stream.collect(Collectors.toList());
Set<String> set = stream.collect(Collectors.toSet());
Map<Long, User> map = stream.collect(Collectors.toMap(User::getId, Function.identity()));
String joined = stream.collect(Collectors.joining(", "));

// groupingBy: 그룹핑
Map<String, List<User>> byDept = users.stream()
    .collect(Collectors.groupingBy(User::getDepartment));

// forEach: 각 요소에 대해 수행
stream.forEach(System.out::println);

// reduce: 누적 연산
int sum = numbers.stream().reduce(0, Integer::sum);
Optional<Integer> max = numbers.stream().reduce(Integer::max);

// count, min, max, anyMatch, allMatch
long count = stream.count();
boolean hasAdult = users.stream().anyMatch(u -> u.getAge() >= 18);
```

### 지연 평가 (Lazy Evaluation)

중간 연산은 최종 연산이 호출될 때까지 실행되지 않습니다.

```java
List<String> result = names.stream()
    .filter(name -> {
        System.out.println("filter: " + name); // 최종 연산 전에는 실행 안 됨
        return name.length() > 2;
    })
    .map(name -> {
        System.out.println("map: " + name);
        return name.toUpperCase();
    })
    .collect(Collectors.toList()); // 이 시점에 위의 연산들이 실행됨
```

### 병렬 스트림

```java
// 병렬 처리로 대용량 데이터 처리 속도 향상
long count = bigList.parallelStream()
    .filter(item -> item.getValue() > 100)
    .count();

// 주의: 순서 보장 안 됨, 스레드 안전한 연산만 사용
```

### 면접 팁
Stream의 특징(일회성, 지연 평가, 내부 반복)을 이해하고, 실무에서 자주 사용하는 패턴(filter-map-collect, groupingBy)을 코드로 작성할 수 있어야 합니다. 병렬 스트림의 주의사항(오버헤드, 스레드 안전성)도 알아두세요.

## 꼬리 질문
1. Stream이 일회성인 이유는 무엇인가요?
2. parallelStream은 언제 사용하면 좋고, 주의사항은?
3. for문과 Stream의 성능 차이는?
