---
title: "Blocking/Non-blocking & Sync/Async"
category: network
difficulty: INTERMEDIATE
tags: "블로킹, 논블로킹, 동기, 비동기, I/O 모델"
---

## 질문
Blocking과 Non-blocking, Synchronous와 Asynchronous의 차이를 설명해주세요.

## 핵심 키워드
- Blocking / Non-blocking
- Synchronous / Asynchronous
- 제어권
- 결과 확인 방식
- I/O 모델

## 답변
이 네 가지 개념은 서로 다른 관점에서 I/O 동작을 설명합니다. Blocking/Non-blocking은 **제어권**에 관한 것이고, Sync/Async는 **결과 처리 방식**에 관한 것입니다.

### Blocking vs Non-blocking

**관점: 호출된 함수가 제어권을 언제 돌려주는가?**

**Blocking**: 호출된 함수가 작업이 완료될 때까지 제어권을 돌려주지 않습니다. 호출자는 대기합니다.

**Non-blocking**: 호출된 함수가 즉시 제어권을 돌려줍니다. 호출자는 다른 작업을 할 수 있습니다.

```java
// Blocking
InputStream is = socket.getInputStream();
int data = is.read();  // 데이터가 올 때까지 블로킹

// Non-blocking
channel.configureBlocking(false);
int bytesRead = channel.read(buffer);  // 즉시 반환 (데이터 없으면 0 또는 -1)
```

### Synchronous vs Asynchronous

**관점: 작업 완료를 누가 확인하는가?**

**Synchronous**: 호출자가 작업 완료를 직접 확인합니다. 결과를 기다리거나 주기적으로 확인합니다.

**Asynchronous**: 호출된 측이 작업 완료 시 콜백으로 알려줍니다. 호출자는 결과를 신경 쓰지 않습니다.

```java
// Synchronous
Future<Result> future = executor.submit(task);
Result result = future.get();  // 결과를 직접 확인

// Asynchronous
CompletableFuture.supplyAsync(() -> doWork())
    .thenAccept(result -> handleResult(result));  // 콜백으로 결과 처리
```

### 4가지 조합

**1. Sync + Blocking (가장 일반적)**
함수 호출 후 결과가 올 때까지 대기합니다.
```
A: 작업 요청 → 대기... → 결과 받음
```
예: 일반적인 파일 읽기, JDBC 쿼리

**2. Sync + Non-blocking**
함수는 즉시 반환하지만, 호출자가 주기적으로 완료 여부를 확인합니다.
```
A: 작업 요청 → 다른 일 → "완료?" → "아직" → 다른 일 → "완료?" → "완료!"
```
예: 폴링(Polling) 방식

**3. Async + Non-blocking (이상적)**
함수는 즉시 반환하고, 완료 시 콜백으로 알려줍니다.
```
A: 작업 요청 → 다른 일 → ... → 콜백 호출됨!
```
예: Node.js I/O, Java NIO + CompletableFuture

**4. Async + Blocking (비효율적)**
비동기 호출을 했지만 결국 블로킹되는 경우입니다.
```
A: 비동기 작업 요청 → 결국 블로킹 대기...
```
예: Node.js에서 MySQL 드라이버가 내부적으로 Blocking I/O 사용

### 비유로 이해하기

```
카페에서 커피를 주문하는 상황:

Sync + Blocking: 카운터 앞에서 커피 나올 때까지 줄 서서 기다림
Sync + Non-blocking: 진동벨 없이 주기적으로 "커피 됐나요?" 확인
Async + Non-blocking: 진동벨 받고 자리에서 다른 일 하다가 벨 울리면 가져감
Async + Blocking: 진동벨 받았지만 카운터 앞에서 계속 기다림
```

### 면접 팁
두 개념의 구분 기준을 명확히 하세요: Blocking/Non-blocking은 "제어권 반환 시점", Sync/Async는 "결과 확인 주체"입니다. 실무에서는 Async + Non-blocking 조합이 가장 효율적이며, 이것이 Node.js와 Spring WebFlux의 핵심 원리입니다.

## 꼬리 질문
1. Java NIO에서 Non-blocking I/O는 어떻게 동작하나요?
2. Node.js의 이벤트 루프와 비동기 I/O의 관계는?
3. Spring MVC와 Spring WebFlux의 I/O 처리 방식 차이는?
