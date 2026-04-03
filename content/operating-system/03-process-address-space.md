---
title: "프로세스 주소 공간"
category: operating-system
difficulty: INTERMEDIATE
tags: "프로세스 주소 공간, 코드 영역, 데이터 영역, 스택, 힙"
---

## 질문
프로세스의 주소 공간 구조에 대해 설명해주세요.

## 핵심 키워드
- Code(Text) 영역
- Data 영역 (BSS 포함)
- Heap 영역
- Stack 영역
- 가상 메모리

## 답변
프로세스가 생성되면 운영체제로부터 독립적인 주소 공간을 할당받습니다. 이 주소 공간은 크게 Code, Data, Heap, Stack 네 가지 영역으로 나뉩니다.

### 프로세스 주소 공간 구조

```
높은 주소  ┌─────────────────┐
           │   Stack 영역    │  ↓ 아래로 성장
           │                 │
           ├─────────────────┤
           │    (빈 공간)     │  ← Stack과 Heap 사이
           ├─────────────────┤
           │   Heap 영역     │  ↑ 위로 성장
           │                 │
           ├─────────────────┤
           │   BSS 영역      │  초기화되지 않은 전역 변수
           ├─────────────────┤
           │   Data 영역     │  초기화된 전역/정적 변수
           ├─────────────────┤
낮은 주소  │   Code(Text)    │  실행 코드
           └─────────────────┘
```

### 각 영역의 역할

**1. Code(Text) 영역**
- 실행할 프로그램의 기계어 코드가 저장됩니다
- 읽기 전용(Read-Only)으로 프로그램 실행 중 변경되지 않습니다
- 여러 프로세스가 같은 프로그램을 실행할 때 Code 영역을 공유할 수 있습니다

**2. Data 영역**
- 전역 변수와 정적(static) 변수가 저장됩니다
- **Data**: 초기화된 전역/정적 변수
- **BSS(Block Started by Symbol)**: 초기화되지 않은 전역/정적 변수 (0으로 초기화)
- 프로그램 시작 시 할당되고 종료 시 해제됩니다

```java
static int initialized = 10;   // Data 영역
static int uninitialized;      // BSS 영역
```

**3. Heap 영역**
- 동적으로 할당되는 메모리 공간입니다
- 프로그래머가 직접 관리합니다 (Java에서는 GC가 관리)
- 낮은 주소에서 높은 주소 방향으로 성장합니다
- 런타임에 크기가 결정됩니다

```java
// Heap에 할당
Object obj = new Object();        // Heap
int[] arr = new int[100];         // Heap
String str = new String("hello"); // Heap
```

**4. Stack 영역**
- 함수 호출 시 지역 변수, 매개변수, 리턴 주소가 저장됩니다
- 함수 호출 시 push, 반환 시 pop 됩니다
- 높은 주소에서 낮은 주소 방향으로 성장합니다
- 컴파일 시 크기가 결정됩니다
- 재귀가 너무 깊으면 Stack Overflow가 발생합니다

```java
void function() {
    int localVar = 10;      // Stack
    int[] ref = new int[5]; // ref는 Stack, 배열 자체는 Heap
}
```

### 영역을 분리하는 이유

1. **메모리 효율성**: Code 영역은 같은 프로그램 실행 시 공유 가능
2. **보안**: Code 영역을 읽기 전용으로 보호
3. **관리 용이성**: 각 영역의 특성에 맞는 메모리 관리 전략 적용

### 면접 팁
Stack과 Heap의 차이를 명확히 구분하세요. Java에서 기본 타입은 Stack에, 객체는 Heap에 할당된다는 점, 그리고 Stack Overflow와 Out of Memory의 차이를 설명할 수 있어야 합니다.

## 꼬리 질문
1. Stack Overflow와 Heap Overflow의 차이는 무엇인가요?
2. Java에서 메모리 누수(Memory Leak)가 발생하는 경우는?
3. 가상 메모리란 무엇이며, 왜 필요한가요?
