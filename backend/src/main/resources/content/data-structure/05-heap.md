---
title: "힙(Heap)"
category: data-structure
difficulty: INTERMEDIATE
tags: "힙, 최대힙, 최소힙, 우선순위 큐, 완전 이진 트리"
---

## 질문
힙(Heap) 자료구조의 특징과 동작 원리에 대해 설명해주세요.

## 핵심 키워드
- 완전 이진 트리
- 최대 힙 / 최소 힙
- heapify
- 우선순위 큐
- O(log n)

## 답변
힙(Heap)은 완전 이진 트리 기반의 자료구조로, 부모 노드와 자식 노드 사이에 대소 관계가 성립합니다. 우선순위 큐를 구현하는 데 주로 사용됩니다.

### 힙의 종류

**최대 힙 (Max Heap)**: 부모 노드가 자식 노드보다 항상 크거나 같습니다.
**최소 힙 (Min Heap)**: 부모 노드가 자식 노드보다 항상 작거나 같습니다.

```
최대 힙 예시:         최소 힙 예시:
      50                  10
     /  \                /  \
    30   40             20   30
   / \                 / \
  10  20              50  40
```

### 배열을 이용한 구현

완전 이진 트리이므로 배열로 효율적으로 구현할 수 있습니다.
- 부모 인덱스: `(i - 1) / 2`
- 왼쪽 자식: `2 * i + 1`
- 오른쪽 자식: `2 * i + 2`

```java
class MinHeap {
    private int[] heap;
    private int size;

    // 삽입: O(log n)
    public void insert(int value) {
        heap[size] = value;
        siftUp(size);
        size++;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[parent] > heap[index]) {
                swap(parent, index);
                index = parent;
            } else break;
        }
    }

    // 삭제(최소값): O(log n)
    public int extractMin() {
        int min = heap[0];
        heap[0] = heap[--size];
        siftDown(0);
        return min;
    }

    private void siftDown(int index) {
        while (2 * index + 1 < size) {
            int child = 2 * index + 1;
            if (child + 1 < size && heap[child + 1] < heap[child]) {
                child++;
            }
            if (heap[index] > heap[child]) {
                swap(index, child);
                index = child;
            } else break;
        }
    }
}
```

### 시간 복잡도

| 연산 | 시간 복잡도 |
|------|-----------|
| 삽입 | O(log n) |
| 삭제 (최대/최소) | O(log n) |
| 최대/최소 조회 | O(1) |
| 힙 구성 (heapify) | O(n) |

### Java에서 우선순위 큐

```java
// 최소 힙 (기본)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.offer(30);
minHeap.offer(10);
minHeap.offer(20);
minHeap.poll(); // 10 (가장 작은 값)

// 최대 힙
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
```

### 활용 사례
- **우선순위 큐**: 작업 스케줄링, 다익스트라 알고리즘
- **힙 정렬**: O(n log n) 정렬 알고리즘
- **Top K 문제**: K개의 최대/최소 원소 추출
- **중앙값 구하기**: 최대 힙과 최소 힙을 함께 사용

### 면접 팁
힙은 정렬된 구조가 아니라는 점을 주의하세요. 부모-자식 간의 대소관계만 보장하며, 형제 노드 간에는 순서가 없습니다. heapify의 시간 복잡도가 O(n log n)이 아닌 O(n)인 이유를 수학적으로 설명할 수 있으면 차별화됩니다.

## 꼬리 질문
1. 힙 정렬의 동작 과정과 시간 복잡도를 설명해주세요.
2. 힙으로 중앙값을 실시간으로 구하는 방법은 무엇인가요?
3. heapify의 시간 복잡도가 O(n)인 이유를 설명해주세요.
