---
title: "스택(Stack) & 큐(Queue)"
category: data-structure
difficulty: BASIC
tags: "스택, 큐, LIFO, FIFO, 덱"
---

## 질문
스택과 큐의 차이점과 각각의 활용 사례에 대해 설명해주세요.

## 핵심 키워드
- LIFO (Last In First Out)
- FIFO (First In First Out)
- push / pop
- enqueue / dequeue
- 덱 (Deque)

## 답변

### 스택 (Stack) - LIFO

스택은 "후입선출(Last In First Out)" 구조입니다. 가장 마지막에 넣은 데이터가 가장 먼저 나옵니다.

```java
Stack<Integer> stack = new Stack<>();
stack.push(1);    // [1]
stack.push(2);    // [1, 2]
stack.push(3);    // [1, 2, 3]
stack.pop();      // 3 반환, [1, 2]
stack.peek();     // 2 반환 (제거하지 않음)
```

**활용 사례:**
- **함수 호출 스택**: 함수가 호출되면 스택에 push, 반환되면 pop
- **뒤로 가기 기능**: 브라우저 뒤로 가기, 에디터 Undo
- **괄호 검증**: 올바른 괄호 쌍 확인
- **DFS (깊이 우선 탐색)**: 재귀 또는 명시적 스택 사용
- **후위 표기법 계산**: 계산기 구현

```java
// 괄호 검증 예시
public boolean isValid(String s) {
    Stack<Character> stack = new Stack<>();
    for (char c : s.toCharArray()) {
        if (c == '(' || c == '{' || c == '[') {
            stack.push(c);
        } else {
            if (stack.isEmpty()) return false;
            char top = stack.pop();
            if (c == ')' && top != '(') return false;
            if (c == '}' && top != '{') return false;
            if (c == ']' && top != '[') return false;
        }
    }
    return stack.isEmpty();
}
```

### 큐 (Queue) - FIFO

큐는 "선입선출(First In First Out)" 구조입니다. 가장 먼저 넣은 데이터가 가장 먼저 나옵니다.

```java
Queue<Integer> queue = new LinkedList<>();
queue.offer(1);   // [1]
queue.offer(2);   // [1, 2]
queue.offer(3);   // [1, 2, 3]
queue.poll();     // 1 반환, [2, 3]
queue.peek();     // 2 반환 (제거하지 않음)
```

**활용 사례:**
- **BFS (너비 우선 탐색)**: 그래프/트리 탐색
- **프로세스 스케줄링**: CPU 작업 대기열
- **프린터 대기열**: 먼저 요청된 인쇄 작업 먼저 처리
- **메시지 큐**: 비동기 메시지 처리 (Kafka, RabbitMQ)
- **버퍼**: 데이터 전송 시 버퍼링

### 덱 (Deque - Double-Ended Queue)

양쪽 끝에서 삽입과 삭제가 모두 가능한 자료구조입니다.

```java
Deque<Integer> deque = new ArrayDeque<>();
deque.offerFirst(1);  // 앞에 추가
deque.offerLast(2);   // 뒤에 추가
deque.pollFirst();    // 앞에서 제거
deque.pollLast();     // 뒤에서 제거
```

### 시간 복잡도

| 연산 | Stack | Queue |
|------|-------|-------|
| 삽입 | O(1) | O(1) |
| 삭제 | O(1) | O(1) |
| 조회(top/front) | O(1) | O(1) |
| 검색 | O(n) | O(n) |

### 면접 팁
Java에서 Stack 클래스보다 Deque(ArrayDeque)를 사용하는 것이 권장됩니다. Stack은 Vector를 상속받아 불필요한 동기화 오버헤드가 있기 때문입니다. 또한 두 개의 스택으로 큐를 구현하는 문제가 자주 출제됩니다.

## 꼬리 질문
1. 두 개의 스택으로 큐를 구현하는 방법을 설명해주세요.
2. 우선순위 큐(Priority Queue)란 무엇이며, 어떻게 구현하나요?
3. Java에서 Stack 대신 Deque를 권장하는 이유는 무엇인가요?
