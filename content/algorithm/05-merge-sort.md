---
title: "병합 정렬(Merge Sort)"
category: algorithm
difficulty: INTERMEDIATE
tags: "병합 정렬, 분할 정복, 안정 정렬, O(n log n), 외부 정렬"
---

## 질문
병합 정렬(Merge Sort)의 동작 원리와 특징에 대해 설명해주세요.

## 핵심 키워드
- 분할 정복
- 병합 (Merge)
- O(n log n) 보장
- 안정 정렬
- 추가 메모리 O(n)

## 답변
병합 정렬은 배열을 반으로 나누고, 각각을 재귀적으로 정렬한 뒤, 두 정렬된 배열을 병합하는 분할 정복 알고리즘입니다.

### 동작 과정

```
[38, 27, 43, 3, 9, 82, 10]

분할:
[38, 27, 43, 3] | [9, 82, 10]
[38, 27] | [43, 3] | [9, 82] | [10]
[38] [27] [43] [3] [9] [82] [10]

병합:
[27, 38] [3, 43] [9, 82] [10]
[3, 27, 38, 43] [9, 10, 82]
[3, 9, 10, 27, 38, 43, 82]
```

### 구현

```java
void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid);       // 왼쪽 정렬
        mergeSort(arr, mid + 1, right);  // 오른쪽 정렬
        merge(arr, left, mid, right);    // 병합
    }
}

void merge(int[] arr, int left, int mid, int right) {
    int[] temp = new int[right - left + 1];
    int i = left, j = mid + 1, k = 0;

    while (i <= mid && j <= right) {
        if (arr[i] <= arr[j]) {
            temp[k++] = arr[i++];
        } else {
            temp[k++] = arr[j++];
        }
    }

    while (i <= mid) temp[k++] = arr[i++];
    while (j <= right) temp[k++] = arr[j++];

    System.arraycopy(temp, 0, arr, left, temp.length);
}
```

### 시간 복잡도

| 경우 | 시간 복잡도 |
|------|-----------|
| 최선 | O(n log n) |
| 평균 | O(n log n) |
| 최악 | O(n log n) |

- 공간 복잡도: O(n) (임시 배열 필요)
- 안정 정렬
- 항상 O(n log n) 보장

### 장단점
**장점:**
- 항상 O(n log n) 보장 (최악의 경우가 없음)
- 안정 정렬
- 연결 리스트 정렬에 효율적 (추가 메모리 불필요)
- 외부 정렬(External Sort)에 적합

**단점:**
- O(n) 추가 메모리 필요
- 퀵 정렬보다 캐시 효율이 낮음

### 활용
- Java의 `Collections.sort()`, `Arrays.sort()`(객체 배열)는 Tim Sort(병합 정렬 + 삽입 정렬)를 사용
- 외부 정렬: 메모리에 다 올릴 수 없는 대용량 데이터 정렬

### 면접 팁
병합 정렬은 "항상 O(n log n)을 보장하는 안정 정렬"이라는 점이 핵심입니다. 퀵 정렬과의 비교에서 각각의 장단점을 논리적으로 설명하세요.

## 꼬리 질문
1. 병합 정렬과 퀵 정렬의 차이점은?
2. 연결 리스트에서 병합 정렬이 유리한 이유는?
3. 외부 정렬(External Sort)이란 무엇인가요?
