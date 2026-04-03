---
title: "LinkedList"
category: data-structure
difficulty: BASIC
tags: "연결리스트, 노드, 포인터, 단일 연결 리스트, 이중 연결 리스트"
---

## 질문
LinkedList의 구조와 동작 원리에 대해 설명해주세요.

## 핵심 키워드
- 노드 (Node)
- 포인터
- 단일/이중/원형 연결 리스트
- 동적 크기
- 순차 접근

## 답변
LinkedList(연결 리스트)는 각 요소(노드)가 데이터와 다음 노드를 가리키는 포인터로 구성된 자료구조입니다. 배열과 달리 메모리의 비연속적인 공간에 데이터를 저장합니다.

### 노드 구조

```java
class Node {
    int data;       // 데이터
    Node next;      // 다음 노드를 가리키는 포인터

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}
```

### LinkedList 종류

**1. 단일 연결 리스트 (Singly Linked List)**
각 노드가 다음 노드만 가리킵니다. 한 방향으로만 탐색 가능합니다.
```
[Data|Next] → [Data|Next] → [Data|null]
```

**2. 이중 연결 리스트 (Doubly Linked List)**
각 노드가 이전 노드와 다음 노드를 모두 가리킵니다.
```
null ← [Prev|Data|Next] ⇄ [Prev|Data|Next] ⇄ [Prev|Data|Next] → null
```

**3. 원형 연결 리스트 (Circular Linked List)**
마지막 노드가 첫 번째 노드를 가리켜 순환 구조를 이룹니다.

### 주요 연산 구현

```java
class LinkedList {
    Node head;

    // 맨 앞에 삽입: O(1)
    void insertFirst(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    // 특정 위치 삽입: O(n)
    void insertAt(int index, int data) {
        Node newNode = new Node(data);
        Node current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
    }

    // 삭제: O(n)
    void delete(int data) {
        if (head.data == data) {
            head = head.next;
            return;
        }
        Node current = head;
        while (current.next != null && current.next.data != data) {
            current = current.next;
        }
        if (current.next != null) {
            current.next = current.next.next;
        }
    }
}
```

### 시간 복잡도

| 연산 | 시간 복잡도 |
|------|-----------|
| 접근 (Access) | O(n) |
| 검색 (Search) | O(n) |
| 맨 앞 삽입/삭제 | O(1) |
| 중간 삽입/삭제 | O(n) (탐색) + O(1) (삽입/삭제) |

### 장점
- **동적 크기**: 크기가 고정되지 않아 유연합니다
- **삽입/삭제 효율**: 위치를 알고 있다면 O(1)에 수행 가능
- **메모리 효율**: 필요한 만큼만 메모리를 사용

### 단점
- **랜덤 접근 불가**: 인덱스 접근이 불가능하여 순차 탐색 필요
- **추가 메모리**: 포인터를 저장하기 위한 추가 메모리 필요
- **캐시 비효율**: 메모리가 비연속적이라 캐시 지역성이 낮음

### 면접 팁
LinkedList의 삽입/삭제가 O(1)이라고 할 때, 이는 "위치를 이미 알고 있는 경우"에 해당합니다. 위치를 찾는 데 O(n)이 걸리므로, 실질적으로는 O(n)입니다. Java의 LinkedList는 이중 연결 리스트로 구현되어 있습니다.

## 꼬리 질문
1. Java에서 LinkedList와 ArrayList의 내부 구현 차이는 무엇인가요?
2. LinkedList에서 순환(Cycle)을 감지하는 방법은 무엇인가요?
3. 이중 연결 리스트가 단일 연결 리스트보다 유리한 경우는 언제인가요?
