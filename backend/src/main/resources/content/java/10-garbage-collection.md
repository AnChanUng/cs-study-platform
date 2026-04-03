---
title: "Garbage Collection"
category: java
difficulty: INTERMEDIATE
tags: "가비지 컬렉션, GC, 힙, Young Generation, Old Generation"
---

## 질문
Java의 가비지 컬렉션(GC)의 동작 원리에 대해 설명해주세요.

## 핵심 키워드
- Mark and Sweep
- Young / Old Generation
- Minor GC / Major GC
- Stop-the-World
- G1 GC / ZGC

## 답변
가비지 컬렉션(Garbage Collection)은 JVM이 사용하지 않는 객체의 메모리를 자동으로 회수하는 메커니즘입니다.

### GC 기본 동작: Mark and Sweep

```
1. Mark  : GC Root에서 시작하여 참조를 따라가며 살아있는 객체를 표시
2. Sweep : 표시되지 않은 객체(가비지)의 메모리를 회수
3. Compact: (선택) 살아남은 객체를 한쪽으로 모아 단편화 해결
```

**GC Root:**
- 스택의 지역 변수
- 정적 변수
- JNI 참조
- 실행 중인 스레드

### 세대별 GC (Generational GC)

대부분의 객체는 짧은 시간만 살아있다는 가설(Weak Generational Hypothesis)에 기반합니다.

```
Heap 구조:
┌──────────────────────────────┐
│  Young Generation            │
│  ┌──────┬───────┬──────┐    │
│  │ Eden │  S0   │  S1  │    │
│  └──────┴───────┴──────┘    │
├──────────────────────────────┤
│  Old Generation              │
└──────────────────────────────┘
```

**Minor GC (Young Generation)**
1. 새 객체는 Eden에 생성
2. Eden이 가득 차면 Minor GC 발생
3. 살아남은 객체는 Survivor(S0 또는 S1)로 이동
4. Survivor에서 일정 횟수(age) 이상 살아남으면 Old로 이동(Promotion)

**Major GC / Full GC (Old Generation)**
- Old 영역이 가득 차면 발생
- Minor GC보다 시간이 오래 걸림
- Stop-the-World 발생

### Stop-the-World
GC를 수행하기 위해 모든 애플리케이션 스레드를 일시 정지하는 것입니다. GC 튜닝의 목표는 이 시간을 최소화하는 것입니다.

### GC 종류

**1. Serial GC**
단일 스레드로 GC 수행. 소규모 애플리케이션용.

**2. Parallel GC**
여러 스레드로 GC 수행. Java 8 기본 GC.

**3. G1 GC (Garbage First)**
힙을 균등한 Region으로 나누어 관리. Java 9+ 기본 GC.
```
가비지가 가장 많은 Region을 우선 수집 → 이름의 유래
```

**4. ZGC**
매우 낮은 지연 시간(10ms 이하). 대용량 힙(TB 규모)에 적합.

### GC 튜닝

```bash
# JVM 옵션
-Xms512m -Xmx2g          # 힙 크기 설정
-XX:+UseG1GC              # G1 GC 사용
-XX:MaxGCPauseMillis=200  # 목표 STW 시간
-verbose:gc               # GC 로그 출력
-XX:+PrintGCDetails       # 상세 GC 로그
```

### 면접 팁
GC의 기본 원리(Mark-Sweep-Compact)와 세대별 GC의 동작 과정을 설명할 수 있어야 합니다. G1 GC의 특징과 Stop-the-World를 최소화하는 방법도 알아두세요.

## 꼬리 질문
1. G1 GC의 동작 원리를 설명해주세요.
2. 메모리 누수가 발생하는 대표적인 사례는?
3. GC 튜닝을 해야 하는 상황과 방법은?
