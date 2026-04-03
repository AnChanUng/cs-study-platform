---
title: "최장 증가 수열(LIS)"
category: algorithm
difficulty: ADVANCED
tags: "LIS, 최장 증가 수열, 동적 계획법, 이분 탐색, O(n log n)"
---

## 질문
최장 증가 수열(LIS) 알고리즘에 대해 설명해주세요.

## 핵심 키워드
- Longest Increasing Subsequence
- DP 풀이: O(n^2)
- 이분 탐색 풀이: O(n log n)
- 부분 수열
- Lower Bound

## 답변
최장 증가 수열(LIS, Longest Increasing Subsequence)은 주어진 수열에서 원소들의 순서를 유지하면서 원소의 값이 순증가하는 가장 긴 부분 수열을 찾는 문제입니다.

### 예시
```
수열: [10, 20, 10, 30, 20, 50]
LIS: [10, 20, 30, 50] → 길이 = 4
```

### 방법 1: DP - O(n^2)

```java
int lisDP(int[] arr) {
    int n = arr.length;
    int[] dp = new int[n];
    Arrays.fill(dp, 1); // 자기 자신만 포함

    for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
            if (arr[j] < arr[i]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
    }
    return Arrays.stream(dp).max().getAsInt();
}
```

`dp[i]` = arr[i]를 마지막으로 하는 LIS의 길이

### 방법 2: 이분 탐색 - O(n log n)

```java
int lisBinarySearch(int[] arr) {
    List<Integer> tails = new ArrayList<>();

    for (int num : arr) {
        int pos = lowerBound(tails, num);
        if (pos == tails.size()) {
            tails.add(num);     // 끝에 추가 (LIS 길이 증가)
        } else {
            tails.set(pos, num); // 기존 값 교체 (더 작은 값으로)
        }
    }
    return tails.size();
}

int lowerBound(List<Integer> list, int target) {
    int lo = 0, hi = list.size();
    while (lo < hi) {
        int mid = (lo + hi) / 2;
        if (list.get(mid) < target) lo = mid + 1;
        else hi = mid;
    }
    return lo;
}
```

### 이분 탐색 풀이 동작 과정

```
수열: [10, 20, 10, 30, 20, 50]

num=10: tails=[] → [10]
num=20: tails=[10] → [10, 20]
num=10: tails=[10, 20] → [10, 20] (10은 이미 있으므로 교체 없음)
num=30: tails=[10, 20] → [10, 20, 30]
num=20: tails=[10, 20, 30] → [10, 20, 30] (20 교체)
num=50: tails=[10, 20, 30] → [10, 20, 30, 50]

LIS 길이 = 4
```

**주의**: tails 배열은 실제 LIS가 아니라 LIS의 길이만 정확합니다.

### 실제 LIS 역추적

```java
int[] lisWithPath(int[] arr) {
    int n = arr.length;
    List<Integer> tails = new ArrayList<>();
    int[] parent = new int[n]; // 역추적용
    int[] indices = new int[n]; // tails에서의 위치

    for (int i = 0; i < n; i++) {
        int pos = lowerBound(tails, arr[i]);
        if (pos == tails.size()) tails.add(arr[i]);
        else tails.set(pos, arr[i]);

        indices[i] = pos;
        parent[i] = pos > 0 ? findLastIndex(indices, pos - 1, i) : -1;
    }

    // 역추적하여 실제 LIS 구성
    // ...
}
```

### 면접 팁
O(n^2) DP 풀이는 기본이고, O(n log n) 이분 탐색 풀이까지 구현할 수 있어야 합니다. tails 배열이 실제 LIS가 아님을 주의하세요. 실제 LIS를 구해야 할 때는 역추적이 필요합니다.

## 꼬리 질문
1. O(n log n) 풀이에서 tails 배열이 실제 LIS가 아닌 이유는?
2. LIS에서 감소 수열(LDS)을 구하는 방법은?
3. 2차원 LIS 문제는 어떻게 풀 수 있나요?
