---
title: "계수 정렬(Counting Sort)"
category: algorithm
difficulty: INTERMEDIATE
tags: "계수 정렬, 비비교 정렬, O(n+k), 안정 정렬, 카운팅"
---

## 질문
계수 정렬(Counting Sort)의 동작 원리와 특징에 대해 설명해주세요.

## 핵심 키워드
- 비비교 정렬
- 카운팅 배열
- O(n + k)
- 안정 정렬
- 데이터 범위 제한

## 답변
계수 정렬(Counting Sort)은 각 원소의 등장 횟수를 카운팅하여 정렬하는 비비교 정렬 알고리즘입니다. 데이터의 범위가 제한적일 때 매우 효율적입니다.

### 동작 과정

```
입력: [4, 2, 2, 8, 3, 3, 1]  (범위: 1~8)

1. 카운팅: count[1]=1, count[2]=2, count[3]=2, count[4]=1, count[8]=1
2. 누적 합: count[1]=1, count[2]=3, count[3]=5, count[4]=6, count[8]=7
3. 뒤에서부터 배치:
   arr[6]=1 → output[0]=1, count[1]=0
   arr[5]=3 → output[4]=3, count[3]=4
   ...
4. 결과: [1, 2, 2, 3, 3, 4, 8]
```

### 구현

```java
void countingSort(int[] arr) {
    int max = Arrays.stream(arr).max().getAsInt();
    int min = Arrays.stream(arr).min().getAsInt();
    int range = max - min + 1;

    int[] count = new int[range];
    int[] output = new int[arr.length];

    // 1. 각 원소 카운팅
    for (int num : arr) {
        count[num - min]++;
    }

    // 2. 누적 합 계산
    for (int i = 1; i < range; i++) {
        count[i] += count[i - 1];
    }

    // 3. 뒤에서부터 배치 (안정성 보장)
    for (int i = arr.length - 1; i >= 0; i--) {
        output[--count[arr[i] - min]] = arr[i];
    }

    System.arraycopy(output, 0, arr, 0, arr.length);
}
```

### 시간 복잡도
- **O(n + k)**: n = 원소 수, k = 데이터 범위
- k가 n에 비해 매우 크면 비효율적

### 장단점
**장점:** O(n + k)로 매우 빠름, 안정 정렬, 구현이 비교적 간단
**단점:** 데이터 범위(k)가 크면 메모리 낭비, 음수/실수 처리 복잡, 범위가 제한적이어야 함

### 활용
- 성적(0~100), 나이(0~150) 등 범위가 제한된 정수 데이터 정렬
- 기수 정렬의 내부 정렬 알고리즘으로 사용

### 면접 팁
계수 정렬은 데이터 범위가 원소 수에 비해 크지 않을 때 효과적입니다. 누적 합을 이용하여 안정 정렬을 보장하는 원리를 이해하고 설명할 수 있어야 합니다.

## 꼬리 질문
1. 계수 정렬이 안정 정렬을 보장하려면 어떻게 해야 하나요?
2. 데이터 범위가 매우 클 때 계수 정렬의 대안은?
3. 계수 정렬과 기수 정렬의 관계는?
