---
title: "퀵 정렬(Quick Sort)"
category: algorithm
difficulty: INTERMEDIATE
tags: "퀵 정렬, 분할 정복, 피벗, 파티션, 불안정 정렬"
---

## 질문
퀵 정렬(Quick Sort)의 동작 원리와 시간 복잡도에 대해 설명해주세요.

## 핵심 키워드
- 분할 정복 (Divide and Conquer)
- 피벗 (Pivot)
- 파티션 (Partition)
- O(n log n) 평균
- 불안정 정렬

## 답변
퀵 정렬은 피벗(Pivot)을 기준으로 배열을 두 부분으로 분할한 후, 각 부분을 재귀적으로 정렬하는 분할 정복 알고리즘입니다. 평균적으로 가장 빠른 정렬 알고리즘 중 하나입니다.

### 동작 과정

```
[5, 3, 8, 4, 2, 7, 1, 6]  피벗 = 5

1. 파티션: 피벗보다 작은 값 | 피벗 | 피벗보다 큰 값
   [3, 4, 2, 1] [5] [8, 7, 6]

2. 왼쪽 재귀: [3, 4, 2, 1] 피벗 = 3
   [2, 1] [3] [4]

3. 오른쪽 재귀: [8, 7, 6] 피벗 = 8
   [7, 6] [8]

4. 계속 재귀... → [1, 2, 3, 4, 5, 6, 7, 8]
```

### 구현

```java
void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pivotIndex = partition(arr, low, high);
        quickSort(arr, low, pivotIndex - 1);  // 왼쪽 부분
        quickSort(arr, pivotIndex + 1, high);  // 오른쪽 부분
    }
}

int partition(int[] arr, int low, int high) {
    int pivot = arr[high]; // 마지막 요소를 피벗으로
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (arr[j] <= pivot) {
            i++;
            swap(arr, i, j);
        }
    }
    swap(arr, i + 1, high);
    return i + 1;
}
```

### 시간 복잡도

| 경우 | 시간 복잡도 | 설명 |
|------|-----------|------|
| 최선 | O(n log n) | 균등 분할 |
| 평균 | O(n log n) | |
| 최악 | O(n^2) | 이미 정렬된 배열에서 첫/마지막 피벗 |

- 공간 복잡도: O(log n) (재귀 스택)
- 불안정 정렬

### 최악의 경우 방지

**1. 랜덤 피벗 선택**
```java
int randomIndex = low + random.nextInt(high - low + 1);
swap(arr, randomIndex, high);
```

**2. Median of Three**
첫 번째, 중간, 마지막 요소의 중앙값을 피벗으로 선택합니다.

**3. 작은 배열에서 삽입 정렬로 전환**
요소가 10~20개 이하일 때 삽입 정렬이 더 효율적입니다.

### 퀵 정렬의 장단점
**장점:** 평균 O(n log n), 캐시 효율이 좋음 (제자리 정렬), 실무에서 가장 빠른 정렬 중 하나
**단점:** 최악 O(n^2), 불안정 정렬

### 면접 팁
퀵 정렬이 병합 정렬보다 실무에서 빠른 이유(캐시 지역성, 상수 계수가 작음)를 설명할 수 있으면 좋습니다. 최악의 경우를 방지하는 방법(랜덤 피벗, Median of Three)도 중요합니다.

## 꼬리 질문
1. 퀵 정렬이 병합 정렬보다 실무에서 빠른 이유는?
2. 퀵 정렬의 최악의 경우를 방지하는 방법은?
3. Java의 Arrays.sort()는 어떤 정렬 알고리즘을 사용하나요?
