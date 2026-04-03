---
title: "동적 계획법(Dynamic Programming)"
category: algorithm
difficulty: INTERMEDIATE
tags: "DP, 동적 계획법, 메모이제이션, 탑다운, 바텀업"
---

## 질문
동적 계획법(DP)이란 무엇이며, 어떤 문제에 적용할 수 있나요?

## 핵심 키워드
- 최적 부분 구조
- 중복 부분 문제
- 메모이제이션 (Top-down)
- 타뷸레이션 (Bottom-up)
- 점화식

## 답변
동적 계획법(Dynamic Programming)은 복잡한 문제를 작은 부분 문제로 나누어 해결하고, 그 결과를 저장하여 중복 계산을 방지하는 알고리즘 설계 기법입니다.

### DP 적용 조건
1. **최적 부분 구조 (Optimal Substructure)**: 큰 문제의 최적해가 작은 부분 문제의 최적해로 구성됨
2. **중복 부분 문제 (Overlapping Subproblems)**: 같은 부분 문제가 반복적으로 등장

### Top-down vs Bottom-up

**Top-down (메모이제이션)**
재귀 + 캐싱 방식. 필요한 부분 문제만 계산합니다.

```java
// 피보나치: Top-down
int[] memo = new int[n + 1];
Arrays.fill(memo, -1);

int fib(int n) {
    if (n <= 1) return n;
    if (memo[n] != -1) return memo[n];
    return memo[n] = fib(n - 1) + fib(n - 2);
}
```

**Bottom-up (타뷸레이션)**
작은 문제부터 순차적으로 해결. 반복문 사용.

```java
// 피보나치: Bottom-up
int fib(int n) {
    int[] dp = new int[n + 1];
    dp[0] = 0;
    dp[1] = 1;
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}
```

### 대표적인 DP 문제

**1. 배낭 문제 (0/1 Knapsack)**
```java
// dp[i][w] = i번째 물건까지 고려했을 때 용량 w에서의 최대 가치
for (int i = 1; i <= n; i++) {
    for (int w = 0; w <= W; w++) {
        if (weight[i] <= w) {
            dp[i][w] = Math.max(dp[i-1][w],
                                dp[i-1][w-weight[i]] + value[i]);
        } else {
            dp[i][w] = dp[i-1][w];
        }
    }
}
```

**2. 최장 공통 부분 수열 (LCS)**
```java
// dp[i][j] = s1[0..i-1]과 s2[0..j-1]의 LCS 길이
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (s1.charAt(i-1) == s2.charAt(j-1)) {
            dp[i][j] = dp[i-1][j-1] + 1;
        } else {
            dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
        }
    }
}
```

**3. 동전 교환 (Coin Change)**
```java
// dp[i] = 금액 i를 만드는 최소 동전 수
Arrays.fill(dp, Integer.MAX_VALUE);
dp[0] = 0;
for (int i = 1; i <= amount; i++) {
    for (int coin : coins) {
        if (coin <= i && dp[i - coin] != Integer.MAX_VALUE) {
            dp[i] = Math.min(dp[i], dp[i - coin] + 1);
        }
    }
}
```

### DP 문제 풀이 단계
1. DP 테이블 정의 (dp[i]의 의미)
2. 점화식 도출
3. 초기값 설정
4. 탐색 방향 결정
5. 최적화 (공간 최적화 등)

### 면접 팁
DP의 핵심은 "점화식 도출"입니다. dp[i]가 의미하는 바를 명확히 정의하고, 이전 상태와의 관계를 찾아야 합니다. 분할 정복과의 차이점(중복 부분 문제의 유무)도 설명할 수 있어야 합니다.

## 꼬리 질문
1. DP와 분할 정복의 차이점은 무엇인가요?
2. DP의 공간 최적화 기법은 무엇인가요?
3. Top-down과 Bottom-up의 장단점은?
