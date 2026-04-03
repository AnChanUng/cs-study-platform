---
title: "이분 탐색(Binary Search)"
category: algorithm
difficulty: BASIC
tags: "이분 탐색, 이진 탐색, O(log n), 정렬된 배열, 탐색"
---

## 질문
이분 탐색(Binary Search)의 동작 원리와 구현 방법에 대해 설명해주세요.

## 핵심 키워드
- 정렬된 배열 필수
- O(log n) 시간 복잡도
- 중간값 비교
- Lower Bound / Upper Bound
- 매개변수 탐색

## 답변
이분 탐색은 정렬된 배열에서 탐색 범위를 절반씩 줄여가며 원하는 값을 찾는 알고리즘입니다. 매 단계마다 탐색 범위가 반으로 줄어들어 O(log n)의 시간 복잡도를 가집니다.

### 동작 과정

```
정렬된 배열: [1, 3, 5, 7, 9, 11, 13, 15]
찾는 값: 9

Step 1: left=0, right=7, mid=3 → arr[3]=7 < 9 → left=4
Step 2: left=4, right=7, mid=5 → arr[5]=11 > 9 → right=4
Step 3: left=4, right=4, mid=4 → arr[4]=9 = 9 → 찾음!
```

### 구현

```java
// 반복문 방식
int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2; // 오버플로우 방지

        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1; // 찾지 못함
}

// 재귀 방식
int binarySearch(int[] arr, int left, int right, int target) {
    if (left > right) return -1;

    int mid = left + (right - left) / 2;
    if (arr[mid] == target) return mid;
    if (arr[mid] < target) return binarySearch(arr, mid + 1, right, target);
    return binarySearch(arr, left, mid - 1, target);
}
```

### Lower Bound / Upper Bound

```java
// Lower Bound: target 이상인 첫 번째 위치
int lowerBound(int[] arr, int target) {
    int left = 0, right = arr.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] < target) left = mid + 1;
        else right = mid;
    }
    return left;
}

// Upper Bound: target 초과인 첫 번째 위치
int upperBound(int[] arr, int target) {
    int left = 0, right = arr.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] <= target) left = mid + 1;
        else right = mid;
    }
    return left;
}
```

### 매개변수 탐색 (Parametric Search)
"최적화 문제"를 "결정 문제"로 바꾸어 이분 탐색을 적용하는 기법입니다.

```java
// 예: 나무 자르기 - 최대 높이 H를 이분 탐색
int parametricSearch(int[] trees, int target) {
    int left = 0, right = maxHeight;
    int answer = 0;

    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (canGetEnough(trees, mid, target)) {
            answer = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return answer;
}
```

### 주의사항
- **배열이 반드시 정렬되어 있어야 합니다**
- `mid = (left + right) / 2`는 정수 오버플로우가 발생할 수 있으므로 `left + (right - left) / 2` 사용
- `left <= right` vs `left < right` 조건에 따라 동작이 달라짐

### 면접 팁
기본 이분 탐색뿐만 아니라 Lower Bound, Upper Bound, 매개변수 탐색까지 구현할 수 있어야 합니다. 코딩 테스트에서 매우 자주 출제되는 패턴입니다.

## 꼬리 질문
1. 이분 탐색에서 mid 계산 시 오버플로우를 방지하는 방법은?
2. 매개변수 탐색(Parametric Search)의 활용 예시를 설명해주세요.
3. Java에서 Arrays.binarySearch()의 반환값은 어떻게 동작하나요?
