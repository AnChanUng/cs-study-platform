---
title: "Race Condition"
category: operating-system
difficulty: INTERMEDIATE
tags: "경쟁 상태, 동기화, 임계 구역, 원자적 연산, 동시성"
---

## 질문
Race Condition이란 무엇이며, 어떻게 해결하나요?

## 핵심 키워드
- 경쟁 상태 (Race Condition)
- 임계 구역 (Critical Section)
- 상호 배제 (Mutual Exclusion)
- 원자적 연산 (Atomic Operation)
- 동기화 메커니즘

## 답변
Race Condition(경쟁 상태)은 여러 프로세스나 스레드가 공유 자원에 동시에 접근할 때, 실행 순서에 따라 결과가 달라지는 문제입니다. 데이터의 일관성이 깨질 수 있어 매우 위험합니다.

### Race Condition 예시

```java
// 공유 변수
int count = 0;

// 스레드 1, 2가 동시에 실행
void increment() {
    count++;  // 원자적이지 않은 연산!
}

// count++ 내부 동작:
// 1. count 값을 레지스터에 로드
// 2. 레지스터 값을 1 증가
// 3. 레지스터 값을 count에 저장

// 스레드 1: LOAD count(0) → INC → STORE(1)
// 스레드 2: LOAD count(0) → INC → STORE(1)
// 결과: count = 1 (기대값: 2)
```

### 임계 구역 (Critical Section)
공유 자원에 접근하는 코드 영역을 임계 구역이라 합니다. 한 번에 하나의 스레드만 실행되어야 합니다.

**임계 구역 문제 해결의 3가지 조건:**
1. **상호 배제(Mutual Exclusion)**: 한 스레드가 임계 구역에 있으면 다른 스레드는 진입 불가
2. **진행(Progress)**: 임계 구역이 비어있으면 진입 가능해야 함
3. **한정 대기(Bounded Waiting)**: 무한정 대기하지 않아야 함

### 해결 방법

**1. synchronized (Java)**
```java
public synchronized void increment() {
    count++;
}

// 또는 블록 수준
public void increment() {
    synchronized (this) {
        count++;
    }
}
```

**2. Lock (ReentrantLock)**
```java
private final Lock lock = new ReentrantLock();

public void increment() {
    lock.lock();
    try {
        count++;
    } finally {
        lock.unlock();
    }
}
```

**3. Atomic 클래스**
```java
private AtomicInteger count = new AtomicInteger(0);

public void increment() {
    count.incrementAndGet();  // CAS 연산으로 원자적 증가
}
```

**4. volatile 키워드**
```java
private volatile boolean flag = false;
// 메모리 가시성은 보장하지만, 원자성은 보장하지 않음
```

### Race Condition 발생 시나리오
- **커널 모드**: 여러 프로세스가 커널 데이터를 동시에 수정
- **프로세스 간**: 공유 메모리(IPC)를 동시에 접근
- **스레드 간**: 같은 프로세스의 전역 변수를 동시에 접근

### 면접 팁
Race Condition은 재현이 어렵고 디버깅하기 까다로운 버그를 유발합니다. synchronized와 Lock의 차이, CAS(Compare-And-Swap) 연산의 원리를 이해하면 좋습니다. 실무에서는 가능하면 불변 객체를 사용하거나 스레드 로컬 변수를 활용하여 근본적으로 공유를 피하는 것이 가장 좋은 해결책입니다.

## 꼬리 질문
1. synchronized와 ReentrantLock의 차이점은 무엇인가요?
2. CAS(Compare-And-Swap) 연산의 원리를 설명해주세요.
3. volatile과 synchronized의 차이는 무엇인가요?
