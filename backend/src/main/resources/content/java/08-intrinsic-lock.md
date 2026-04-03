---
title: "고유 락(Intrinsic Lock)"
category: java
difficulty: ADVANCED
tags: "고유 락, 모니터, synchronized, ReentrantLock, 동기화"
---

## 질문
Java의 고유 락(Intrinsic Lock)과 모니터(Monitor)에 대해 설명해주세요.

## 핵심 키워드
- 고유 락 (Intrinsic Lock / Monitor Lock)
- synchronized
- wait / notify / notifyAll
- ReentrantLock
- Lock Escalation

## 답변
Java의 모든 객체는 고유 락(Intrinsic Lock, 모니터 락)을 하나씩 가지고 있습니다. synchronized 키워드를 사용하면 이 고유 락을 획득하고 해제하는 방식으로 상호 배제를 구현합니다.

### 고유 락(Monitor) 동작

```java
// 모든 Java 객체는 모니터를 가짐
Object lock = new Object();

synchronized (lock) {     // lock 객체의 고유 락 획득
    // 임계 구역
}                         // 고유 락 자동 해제

// 메서드 수준 - this 객체의 고유 락
public synchronized void method() {
    // this의 고유 락 획득
}

// 정적 메서드 - 클래스 객체의 고유 락
public static synchronized void staticMethod() {
    // MyClass.class의 고유 락 획득
}
```

### 재진입성 (Reentrancy)

고유 락은 재진입이 가능합니다. 같은 스레드가 이미 보유한 락을 다시 획득할 수 있습니다.

```java
public synchronized void outer() {
    inner(); // 같은 스레드가 다시 락 획득 가능 (재진입)
}

public synchronized void inner() {
    // 이미 outer()에서 this의 락을 보유하고 있으므로 진입 가능
}
```

### wait / notify / notifyAll

고유 락과 함께 사용되는 스레드 간 통신 메커니즘입니다.

```java
class SharedBuffer {
    private Queue<Integer> queue = new LinkedList<>();
    private int capacity;

    public synchronized void produce(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // 락을 해제하고 대기
        }
        queue.add(item);
        notifyAll(); // 대기 중인 스레드 깨움
    }

    public synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        int item = queue.poll();
        notifyAll();
        return item;
    }
}
```

### synchronized vs ReentrantLock

```java
// ReentrantLock: 더 세밀한 제어 가능
ReentrantLock lock = new ReentrantLock();

lock.lock();
try {
    // 임계 구역
} finally {
    lock.unlock(); // 반드시 finally에서 해제
}

// tryLock: 락 획득 시도 (타임아웃 설정 가능)
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try { ... } finally { lock.unlock(); }
} else {
    // 락 획득 실패 처리
}
```

| 구분 | synchronized | ReentrantLock |
|------|-------------|--------------|
| 사용법 | 키워드 | API 호출 |
| 해제 | 자동 | 수동 (finally) |
| tryLock | 불가 | 가능 |
| 공정성 | 불공정 | 공정/불공정 선택 |
| Condition | wait/notify | Condition 객체 |
| 인터럽트 | 불가 | lockInterruptibly() |

### JVM 내부: 락 최적화

JVM은 성능을 위해 락을 최적화합니다.
- **바이어스드 락(Biased Lock)**: 단일 스레드가 반복 접근 시 락 오버헤드 제거
- **경량 락(Lightweight Lock)**: CAS 연산으로 락 획득
- **중량 락(Heavyweight Lock)**: OS 뮤텍스 사용 (경합이 심한 경우)

### 면접 팁
synchronized의 동작 원리(모니터 락)와 ReentrantLock의 차이를 명확히 설명하세요. wait/notify의 사용법과 spurious wakeup을 방지하기 위해 while 루프에서 조건을 확인해야 하는 이유도 중요합니다.

## 꼬리 질문
1. wait()을 while 루프에서 호출해야 하는 이유는?
2. ReentrantLock의 공정 모드란 무엇인가요?
3. StampedLock이란 무엇이며, 언제 사용하나요?
