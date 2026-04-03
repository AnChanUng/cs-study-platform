---
title: "Thread 활용"
category: java
difficulty: INTERMEDIATE
tags: "스레드, Runnable, ExecutorService, 동기화, 스레드 풀"
---

## 질문
Java에서 스레드를 생성하고 활용하는 방법에 대해 설명해주세요.

## 핵심 키워드
- Thread 클래스 / Runnable 인터페이스
- ExecutorService / 스레드 풀
- synchronized
- 스레드 상태
- CompletableFuture

## 답변

### 스레드 생성 방법

**1. Thread 상속**
```java
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
}
new MyThread().start();
```

**2. Runnable 인터페이스 구현 (권장)**
```java
Runnable task = () -> System.out.println("Runnable: " + Thread.currentThread().getName());
new Thread(task).start();
```

**3. ExecutorService (스레드 풀) - 실무 권장**
```java
ExecutorService executor = Executors.newFixedThreadPool(5);

// Runnable 제출
executor.submit(() -> System.out.println("Task executed"));

// Callable 제출 (결과 반환)
Future<String> future = executor.submit(() -> {
    Thread.sleep(1000);
    return "Result";
});
String result = future.get(); // 결과 대기

executor.shutdown();
```

**4. CompletableFuture (비동기 프로그래밍)**
```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> fetchData())        // 비동기 실행
    .thenApply(data -> processData(data))  // 결과 변환
    .thenApply(result -> format(result))   // 추가 변환
    .exceptionally(ex -> "Error: " + ex.getMessage()); // 예외 처리

// 여러 비동기 작업 병렬 실행
CompletableFuture.allOf(future1, future2, future3)
    .thenRun(() -> System.out.println("모두 완료"));
```

### 스레드 상태

```
NEW → RUNNABLE ⇄ BLOCKED/WAITING/TIMED_WAITING → TERMINATED

new Thread()  → start()  → run()완료 → TERMINATED
                  ↕
               BLOCKED (synchronized 대기)
               WAITING (wait(), join())
               TIMED_WAITING (sleep(), wait(timeout))
```

### 동기화

```java
// synchronized 메서드
public synchronized void increment() {
    count++;
}

// synchronized 블록
public void increment() {
    synchronized (lock) {
        count++;
    }
}

// ReentrantLock
private final ReentrantLock lock = new ReentrantLock();
public void increment() {
    lock.lock();
    try {
        count++;
    } finally {
        lock.unlock();
    }
}
```

### 스레드 풀 종류

```java
Executors.newFixedThreadPool(n);    // 고정 크기 풀
Executors.newCachedThreadPool();    // 필요에 따라 생성
Executors.newSingleThreadExecutor(); // 단일 스레드
Executors.newScheduledThreadPool(n); // 스케줄링 가능

// 실무: 직접 ThreadPoolExecutor 설정 권장
new ThreadPoolExecutor(
    corePoolSize, maxPoolSize,
    keepAliveTime, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(queueCapacity)
);
```

### 면접 팁
실무에서는 `new Thread()`를 직접 사용하지 않고 스레드 풀(ExecutorService)을 사용합니다. 스레드 풀의 적절한 크기 설정(CPU bound vs I/O bound)과 CompletableFuture를 활용한 비동기 프로그래밍을 이해하세요.

## 꼬리 질문
1. 스레드 풀의 적절한 크기를 어떻게 결정하나요?
2. CompletableFuture와 Future의 차이점은?
3. ThreadLocal이란 무엇이며, 언제 사용하나요?
