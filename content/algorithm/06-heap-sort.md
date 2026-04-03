---
title: "힙 정렬(Heap Sort)"
category: algorithm
difficulty: INTERMEDIATE
tags: "힙 정렬, 힙, 최대 힙, O(n log n), 불안정 정렬"
---

## 질문
힙 정렬(Heap Sort)의 동작 원리와 특징에 대해 설명해주세요.

## 핵심 키워드
- 최대 힙 (Max Heap)
- heapify
- O(n log n) 보장
- 제자리 정렬
- 불안정 정렬

## 답변
힙 정렬은 최대 힙(Max Heap) 자료구조를 이용한 정렬 알고리즘입니다. 배열을 최대 힙으로 구성한 뒤, 루트(최대값)를 반복적으로 추출하여 정렬합니다.

### 동작 과정

```
1. 배열을 최대 힙으로 구성 (Build Max Heap)
2. 루트(최대값)를 배열 끝과 교환
3. 힙 크기를 줄이고 다시 heapify
4. 2-3을 반복

배열: [4, 10, 3, 5, 1]

Step 1: Build Max Heap → [10, 5, 3, 4, 1]
Step 2: 10과 1 교환   → [1, 5, 3, 4, | 10] → heapify → [5, 4, 3, 1, | 10]
Step 3: 5와 1 교환    → [1, 4, 3, | 5, 10] → heapify → [4, 1, 3, | 5, 10]
Step 4: 4와 3 교환    → [3, 1, | 4, 5, 10] → heapify → [3, 1, | 4, 5, 10]
Step 5: 3과 1 교환    → [1, | 3, 4, 5, 10]
결과: [1, 3, 4, 5, 10]
```

### 구현

```java
void heapSort(int[] arr) {
    int n = arr.length;

    // 1. Build Max Heap: O(n)
    for (int i = n / 2 - 1; i >= 0; i--) {
        heapify(arr, n, i);
    }

    // 2. 루트를 끝으로 보내고 heapify 반복
    for (int i = n - 1; i > 0; i--) {
        swap(arr, 0, i);       // 루트(최대값)를 끝으로
        heapify(arr, i, 0);    // 줄어든 힙에서 heapify
    }
}

void heapify(int[] arr, int n, int i) {
    int largest = i;
    int left = 2 * i + 1;
    int right = 2 * i + 2;

    if (left < n && arr[left] > arr[largest]) largest = left;
    if (right < n && arr[right] > arr[largest]) largest = right;

    if (largest != i) {
        swap(arr, i, largest);
        heapify(arr, n, largest); // 재귀적 heapify
    }
}
```

### 시간 복잡도

| 경우 | 시간 복잡도 |
|------|-----------|
| 최선 | O(n log n) |
| 평균 | O(n log n) |
| 최악 | O(n log n) |

- 공간 복잡도: O(1) (제자리 정렬)
- 불안정 정렬

### 장단점
**장점:** 항상 O(n log n) 보장, 추가 메모리 불필요 (제자리)
**단점:** 불안정 정렬, 캐시 효율이 낮음 (배열 접근 패턴이 불규칙)

### 정렬 알고리즘 비교

| 알고리즘 | 평균 | 최악 | 공간 | 안정 |
|---------|------|------|------|------|
| 퀵 정렬 | O(n log n) | O(n^2) | O(log n) | X |
| 병합 정렬 | O(n log n) | O(n log n) | O(n) | O |
| 힙 정렬 | O(n log n) | O(n log n) | O(1) | X |

### 면접 팁
힙 정렬은 "최악 O(n log n) + 추가 메모리 O(1)"이라는 고유한 장점이 있습니다. 그러나 실무에서는 캐시 효율이 좋은 퀵 정렬이나 안정 정렬인 병합 정렬이 더 많이 사용됩니다.

## 꼬리 질문
1. Build Max Heap의 시간 복잡도가 O(n)인 이유는?
2. 힙 정렬이 실무에서 잘 사용되지 않는 이유는?
3. 힙 정렬과 우선순위 큐의 관계는?
