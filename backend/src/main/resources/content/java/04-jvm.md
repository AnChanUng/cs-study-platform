---
title: "JVM 구조"
category: java
difficulty: INTERMEDIATE
tags: "JVM, 메모리 구조, 힙, 스택, 메소드 영역, 가비지 컬렉션"
---

## 질문
JVM의 구조와 메모리 영역에 대해 설명해주세요.

## 핵심 키워드
- 클래스 로더
- 런타임 데이터 영역
- 힙 (Heap)
- 스택 (Stack)
- 실행 엔진

## 답변
JVM(Java Virtual Machine)은 자바 바이트코드를 실행하는 가상 머신으로, 플랫폼 독립성을 제공합니다.

### JVM 구조

```
┌─────────────────────────────────────┐
│           JVM                       │
│  ┌──────────────────────┐           │
│  │   Class Loader        │           │
│  └──────────┬───────────┘           │
│             ↓                        │
│  ┌──────────────────────────────┐   │
│  │  Runtime Data Area            │   │
│  │  ┌────────┬────────────────┐ │   │
│  │  │ Method │    Heap        │ │   │
│  │  │ Area   │                │ │   │
│  │  ├────────┼────────────────┤ │   │
│  │  │ Stack  │  PC Register   │ │   │
│  │  │        │  Native Stack  │ │   │
│  │  └────────┴────────────────┘ │   │
│  └──────────────────────────────┘   │
│             ↓                        │
│  ┌──────────────────────┐           │
│  │   Execution Engine    │           │
│  │  Interpreter + JIT    │           │
│  │  + GC                 │           │
│  └──────────────────────┘           │
└─────────────────────────────────────┘
```

### 런타임 데이터 영역

**1. 메소드 영역 (Method Area / Metaspace)**
- 클래스 정보, 정적 변수, 상수 풀, 메서드 정보 저장
- 모든 스레드가 공유
- Java 8부터 Metaspace로 변경 (네이티브 메모리 사용)

**2. 힙 (Heap)**
- 객체와 배열이 저장되는 영역
- 모든 스레드가 공유
- GC(Garbage Collection)의 대상

```
Heap 구조:
┌──────────────────────────────┐
│  Young Generation            │
│  ┌──────┬───────┬──────┐    │
│  │ Eden │  S0   │  S1  │    │  ← Minor GC
│  └──────┴───────┴──────┘    │
├──────────────────────────────┤
│  Old Generation              │  ← Major GC (Full GC)
└──────────────────────────────┘
```

**3. 스택 (Stack)**
- 메서드 호출 시 생성되는 스택 프레임 저장
- 지역 변수, 매개변수, 리턴 주소
- 스레드마다 독립적

```
스택 프레임:
┌─────────────────┐
│ Local Variables  │ (지역 변수)
├─────────────────┤
│ Operand Stack   │ (연산용 스택)
├─────────────────┤
│ Frame Data      │ (리턴 주소 등)
└─────────────────┘
```

**4. PC 레지스터**
- 현재 실행 중인 명령어의 주소 저장
- 스레드마다 독립적

**5. 네이티브 메서드 스택**
- JNI를 통해 호출되는 네이티브 코드용 스택

### 메모리 할당 예시

```java
public class Example {
    static int classVar = 10;         // Method Area

    public void method() {
        int localVar = 20;            // Stack
        String str = "hello";         // str은 Stack, "hello"는 String Pool
        Object obj = new Object();    // obj는 Stack, 객체는 Heap
    }
}
```

### 면접 팁
JVM 메모리 구조에서 "스레드 공유 영역"(Heap, Method Area)과 "스레드 독립 영역"(Stack, PC Register)을 구분하세요. Heap의 Young/Old Generation 구조와 GC의 동작 원리도 중요합니다.

## 꼬리 질문
1. Young Generation과 Old Generation의 GC 차이는?
2. StackOverflowError와 OutOfMemoryError의 차이는?
3. Java 8에서 Permanent Generation이 Metaspace로 변경된 이유는?
