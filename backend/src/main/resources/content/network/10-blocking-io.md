---
title: "Blocking & Non-Blocking I/O"
category: network
difficulty: ADVANCED
tags: "I/O 모델, select, epoll, 이벤트 드리븐, 리액터 패턴"
---

## 질문
운영체제의 I/O 모델(Blocking I/O, Non-blocking I/O, I/O Multiplexing 등)에 대해 설명해주세요.

## 핵심 키워드
- Blocking I/O
- Non-blocking I/O
- I/O Multiplexing (select, poll, epoll)
- Async I/O
- 이벤트 드리븐

## 답변
운영체제에서 I/O 작업은 크게 두 단계로 나뉩니다:
1. **데이터 준비**: 커널이 데이터를 준비하는 단계
2. **데이터 복사**: 커널 버퍼에서 사용자 버퍼로 복사하는 단계

각 I/O 모델은 이 두 단계에서의 동작이 다릅니다.

### I/O 모델 종류

**1. Blocking I/O**
```
Application          Kernel
    |  read() 호출    |
    |───────────────→|
    |   (블로킹...)    | 데이터 준비 중...
    |                 | 데이터 준비 완료
    |                 | 커널→유저 복사
    |←───────────────|
    | 데이터 반환       |
```
- 가장 단순한 모델
- 스레드가 I/O 완료까지 차단됨
- 동시 처리를 위해 스레드를 늘려야 함 → 자원 낭비

**2. Non-blocking I/O**
```
Application          Kernel
    |  read() 호출    |
    |───────────────→|
    |←── EAGAIN ─────| 데이터 없음
    |  read() 재호출   |
    |───────────────→|
    |←── EAGAIN ─────| 아직 없음
    |  read() 재호출   |
    |───────────────→|
    |                 | 데이터 준비 완료
    |                 | 커널→유저 복사 (블로킹)
    |←───────────────|
```
- 호출 즉시 반환, 데이터가 없으면 에러 반환
- 반복적인 시스템 콜이 필요 (busy-waiting) → CPU 낭비

**3. I/O Multiplexing (select, poll, epoll)**
```
Application          Kernel
    |  select() 호출  |
    |───────────────→|
    |   (블로킹...)    | 여러 fd 모니터링
    |←───────────────| fd 준비됨
    |  read() 호출    |
    |───────────────→|
    |←───────────────| 데이터 반환
```
- 하나의 스레드로 여러 I/O를 동시에 감시
- 준비된 I/O만 처리하여 효율적

```c
// select 예시 (최대 FD_SETSIZE개 감시)
fd_set readfds;
FD_ZERO(&readfds);
FD_SET(sockfd, &readfds);
select(sockfd + 1, &readfds, NULL, NULL, &timeout);

// epoll 예시 (Linux, 대규모에 효율적)
int epfd = epoll_create1(0);
struct epoll_event ev;
ev.events = EPOLLIN;
ev.data.fd = sockfd;
epoll_ctl(epfd, EPOLL_CTL_ADD, sockfd, &ev);
int nfds = epoll_wait(epfd, events, MAX_EVENTS, timeout);
```

**select vs epoll:**

| 특성 | select | epoll |
|------|--------|-------|
| fd 제한 | 1024개 | 없음 |
| 동작 | 전체 fd 스캔 O(n) | 이벤트 기반 O(1) |
| 커널-유저 복사 | 매번 전체 | 필요한 것만 |
| 적합한 상황 | 소규모 연결 | 대규모 연결 (C10K) |

**4. Asynchronous I/O (AIO)**
```
Application          Kernel
    |  aio_read()     |
    |───────────────→|
    |←── 즉시 반환 ───|
    | (다른 작업 수행)  | 데이터 준비 + 복사
    |                 |
    |←── 시그널/콜백 ──| 완료 통지
```
- 두 단계 모두 비동기로 처리
- 가장 효율적이지만 구현이 복잡

### 실무 적용

```
Nginx: epoll 기반 이벤트 드리븐 → 수만 동시 연결 처리
Node.js: libuv의 이벤트 루프 → Non-blocking I/O
Netty: Java NIO 기반 → 높은 동시성
Spring WebFlux: Reactor 패턴 → 리액티브 프로그래밍
```

### 면접 팁
C10K 문제(동시 1만 연결 처리)를 해결하기 위해 I/O Multiplexing과 이벤트 드리븐 아키텍처가 등장했다는 맥락을 설명하세요. 실무에서 사용하는 Nginx, Node.js가 어떤 I/O 모델을 사용하는지 연결하면 좋습니다.

## 꼬리 질문
1. C10K 문제란 무엇이며, 어떻게 해결하나요?
2. epoll의 Edge Trigger와 Level Trigger의 차이는?
3. Java NIO의 Selector는 어떤 I/O 모델에 해당하나요?
