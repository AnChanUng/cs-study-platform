---
title: "기수 정렬(Radix Sort)"
category: algorithm
difficulty: INTERMEDIATE
tags: "기수 정렬, 비비교 정렬, O(nk), 안정 정렬, 자릿수"
---

## 질문
기수 정렬(Radix Sort)의 동작 원리와 특징에 대해 설명해주세요.

## 핵심 키워드
- 비비교 정렬
- 자릿수별 정렬
- LSD / MSD
- O(d * (n + k))
- 안정 정렬

## 답변
기수 정렬은 데이터의 각 자릿수를 기준으로 정렬하는 비비교(Non-comparison) 정렬 알고리즘입니다. 원소 간 직접 비교하지 않고 각 자릿수별로 카운팅 정렬을 적용합니다.

### 동작 과정 (LSD - Least Significant Digit)

```
입력: [170, 45, 75, 90, 802, 24, 2, 66]

1의 자리로 정렬: [170, 90, 802, 2, 24, 45, 75, 66]
10의 자리로 정렬: [802, 2, 24, 45, 66, 170, 75, 90]
100의 자리로 정렬: [2, 24, 45, 66, 75, 90, 170, 802]
```

### 구현

```java
void radixSort(int[] arr) {
    int max = Arrays.stream(arr).max().getAsInt();

    // 각 자릿수에 대해 카운팅 정렬 수행
    for (int exp = 1; max / exp > 0; exp *= 10) {
        countingSortByDigit(arr, exp);
    }
}

void countingSortByDigit(int[] arr, int exp) {
    int n = arr.length;
    int[] output = new int[n];
    int[] count = new int[10]; // 0~9

    // 해당 자릿수의 등장 횟수 카운트
    for (int num : arr) {
        int digit = (num / exp) % 10;
        count[digit]++;
    }

    // 누적 합
    for (int i = 1; i < 10; i++) {
        count[i] += count[i - 1];
    }

    // 뒤에서부터 배치 (안정성 유지)
    for (int i = n - 1; i >= 0; i--) {
        int digit = (arr[i] / exp) % 10;
        output[--count[digit]] = arr[i];
    }

    System.arraycopy(output, 0, arr, 0, n);
}
```

### 시간 복잡도
- **O(d * (n + k))**: d = 최대 자릿수, n = 원소 수, k = 기수(진법, 보통 10)
- 자릿수가 상수이면 사실상 O(n)

### LSD vs MSD
- **LSD (Least Significant Digit)**: 가장 낮은 자릿수부터 정렬. 구현이 간단.
- **MSD (Most Significant Digit)**: 가장 높은 자릿수부터 정렬. 중간에 멈출 수 있어 효율적일 수 있음.

### 장단점
**장점:** 비교 정렬의 O(n log n) 하한을 돌파, 안정 정렬, 데이터 범위가 제한적일 때 매우 빠름
**단점:** 추가 메모리 필요, 자릿수가 많으면 비효율적, 음수나 실수 처리가 복잡

### 면접 팁
비교 기반 정렬의 이론적 하한은 O(n log n)이지만, 기수 정렬은 비비교 정렬이므로 이 한계를 넘을 수 있다는 점을 설명하세요. 단, 범용성이 부족하여 특정 조건에서만 효율적입니다.

## 꼬리 질문
1. 비교 기반 정렬의 이론적 하한이 O(n log n)인 이유는?
2. 기수 정렬이 카운팅 정렬보다 유리한 경우는?
3. 문자열을 기수 정렬로 정렬할 수 있나요?
