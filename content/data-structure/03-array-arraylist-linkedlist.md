---
title: "Array & ArrayList & LinkedList 비교"
category: data-structure
difficulty: BASIC
tags: "배열, ArrayList, LinkedList, 비교, 자료구조 선택"
---

## 질문
Array, ArrayList, LinkedList의 차이점을 비교하고, 각각 언제 사용하는 것이 적합한지 설명해주세요.

## 핵심 키워드
- 정적 배열 vs 동적 배열
- 연속 메모리 vs 비연속 메모리
- 랜덤 접근 vs 순차 접근
- 크기 변경 가능 여부
- 삽입/삭제 성능

## 답변
세 가지 자료구조는 데이터를 선형으로 저장한다는 공통점이 있지만, 내부 구현과 성능 특성에서 큰 차이가 있습니다.

### Array (배열)
```java
int[] arr = new int[5]; // 고정 크기, 기본 타입 저장 가능
arr[0] = 10;
```
- 고정 크기, 크기 변경 불가
- 기본 타입(int, double 등) 저장 가능
- 연속 메모리 공간에 저장

### ArrayList
```java
ArrayList<Integer> list = new ArrayList<>(); // 동적 크기
list.add(10);     // O(1) - 끝에 추가
list.get(0);      // O(1) - 인덱스 접근
list.add(0, 20);  // O(n) - 중간 삽입
list.remove(0);   // O(n) - 중간 삭제
```
- 내부적으로 배열을 사용하는 동적 배열
- 용량이 부족하면 기존 크기의 1.5배로 새 배열을 생성하고 복사
- 객체(참조 타입)만 저장 가능

### LinkedList
```java
LinkedList<Integer> list = new LinkedList<>();
list.addFirst(10);  // O(1) - 맨 앞 추가
list.addLast(20);   // O(1) - 맨 뒤 추가
list.get(5);        // O(n) - 인덱스 접근 (순차 탐색)
```
- 노드 기반, 비연속 메모리
- Java의 LinkedList는 이중 연결 리스트
- Deque 인터페이스도 구현

### 성능 비교

| 연산 | Array | ArrayList | LinkedList |
|------|-------|-----------|------------|
| 인덱스 접근 | O(1) | O(1) | O(n) |
| 검색 | O(n) | O(n) | O(n) |
| 맨 끝 삽입 | - | O(1)* | O(1) |
| 중간 삽입 | - | O(n) | O(1)** |
| 맨 끝 삭제 | - | O(1) | O(1) |
| 중간 삭제 | - | O(n) | O(1)** |
| 메모리 | 가장 적음 | 중간 | 가장 많음 |

*평균 O(1), 리사이즈 시 O(n)
**위치를 알고 있는 경우. 탐색 포함 시 O(n)

### 선택 기준

**Array를 사용하는 경우:**
- 크기가 고정되어 있을 때
- 기본 타입 데이터를 효율적으로 저장할 때
- 다차원 배열이 필요할 때

**ArrayList를 사용하는 경우:**
- 크기가 동적으로 변할 때
- 인덱스를 통한 빈번한 조회가 필요할 때
- 삽입/삭제가 주로 끝에서 이루어질 때
- 대부분의 일반적인 상황에서 기본 선택

**LinkedList를 사용하는 경우:**
- 삽입/삭제가 빈번하고 위치가 이미 알려진 경우
- Stack, Queue, Deque 구현이 필요할 때
- 데이터 크기를 예측할 수 없고 빈번한 구조 변경이 있을 때

### 면접 팁
실무에서는 대부분의 경우 ArrayList가 더 나은 성능을 보입니다. LinkedList는 노드당 포인터 저장에 의한 메모리 오버헤드와 캐시 비효율로 인해, 이론적으로 유리한 상황에서도 ArrayList에 밀리는 경우가 많습니다.

## 꼬리 질문
1. ArrayList의 내부 배열이 꽉 찼을 때 어떻게 동작하나요?
2. Java에서 ArrayList와 Vector의 차이는 무엇인가요?
3. 실무에서 LinkedList보다 ArrayList를 선호하는 이유는 무엇인가요?
