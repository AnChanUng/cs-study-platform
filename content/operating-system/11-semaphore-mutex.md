---
title: "세마포어(Semaphore) & 뮤텍스(Mutex)"
category: operating-system
difficulty: INTERMEDIATE
tags: "세마포어, 뮤텍스, 동기화, 상호배제, P/V 연산"
---

## 질문
세마포어와 뮤텍스의 차이점에 대해 설명해주세요.

## 핵심 키워드
- 뮤텍스 (Mutex)
- 이진 세마포어 / 카운팅 세마포어
- P(wait) / V(signal) 연산
- 소유권
- 동기화 도구

## 답변

### 뮤텍스 (Mutex - Mutual Exclusion)
뮤텍스는 임계 구역에 대한 상호 배제를 보장하는 동기화 도구입니다. 한 번에 하나의 스레드만 자원에 접근할 수 있습니다.

**특징:**
- 잠금(Lock)과 해제(Unlock) 두 가지 연산만 존재
- **소유권 개념이 있음**: 잠금을 획득한 스레드만 해제 가능
- 이진 상태 (잠김/해제)

```java
// Java에서 뮤텍스 역할
private final Object mutex = new Object();

public void criticalSection() {
    synchronized (mutex) {
        // 임계 구역 - 한 스레드만 진입 가능
    }
}
```

### 세마포어 (Semaphore)
세마포어는 정수 변수와 P(wait), V(signal) 두 가지 원자적 연산으로 구성된 동기화 도구입니다.

**P(wait) 연산**: 세마포어 값을 1 감소. 값이 음수이면 대기
**V(signal) 연산**: 세마포어 값을 1 증가. 대기 중인 프로세스를 깨움

```
P(S) {
    S--;
    if (S < 0) block(); // 대기 큐에 추가
}

V(S) {
    S++;
    if (S <= 0) wakeup(); // 대기 큐에서 하나 깨움
}
```

**이진 세마포어(Binary Semaphore)**: 0 또는 1만 가짐 (뮤텍스와 유사)
**카운팅 세마포어(Counting Semaphore)**: 여러 개의 자원에 대한 동시 접근 제어

```java
// Java 세마포어 - 동시에 3개 스레드만 접근 허용
Semaphore semaphore = new Semaphore(3);

public void accessResource() throws InterruptedException {
    semaphore.acquire();  // P 연산
    try {
        // 임계 구역 - 최대 3개 스레드 동시 진입 가능
        doWork();
    } finally {
        semaphore.release();  // V 연산
    }
}
```

### 뮤텍스 vs 세마포어 비교

| 구분 | 뮤텍스 | 세마포어 |
|------|--------|---------|
| 동기화 대상 수 | 1개 | N개 |
| 소유권 | 있음 (Lock한 스레드만 Unlock) | 없음 (다른 스레드가 signal 가능) |
| 목적 | 상호 배제 | 접근 순서 제어, 리소스 카운팅 |
| 값 | 0 또는 1 | 0 이상의 정수 |

### 활용 예시

**생산자-소비자 문제:**
```java
Semaphore empty = new Semaphore(BUFFER_SIZE); // 빈 공간 수
Semaphore full = new Semaphore(0);             // 채워진 공간 수
Semaphore mutex = new Semaphore(1);            // 버퍼 접근 제어

// 생산자
void producer() {
    empty.acquire();   // 빈 공간 확인
    mutex.acquire();   // 버퍼 접근 잠금
    addToBuffer(item);
    mutex.release();   // 버퍼 접근 해제
    full.release();    // 채워진 공간 증가
}

// 소비자
void consumer() {
    full.acquire();    // 채워진 공간 확인
    mutex.acquire();   // 버퍼 접근 잠금
    removeFromBuffer();
    mutex.release();   // 버퍼 접근 해제
    empty.release();   // 빈 공간 증가
}
```

### 면접 팁
가장 중요한 차이점은 "소유권"입니다. 뮤텍스는 Lock을 획득한 스레드만 해제할 수 있지만, 세마포어는 어떤 스레드든 signal을 보낼 수 있습니다. 또한 세마포어는 순서 제어에도 사용할 수 있다는 점도 기억하세요.

## 꼬리 질문
1. 이진 세마포어와 뮤텍스의 차이는 무엇인가요?
2. 세마포어로 교착 상태(Deadlock)가 발생할 수 있나요?
3. Java의 ReentrantLock과 Semaphore의 차이는?
