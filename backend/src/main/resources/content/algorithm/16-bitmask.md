---
title: "비트마스크(Bitmask)"
category: algorithm
difficulty: INTERMEDIATE
tags: "비트마스크, 비트 연산, 부분집합, 상태 압축, DP"
---

## 질문
비트마스크(Bitmask)란 무엇이며, 알고리즘에서 어떻게 활용되나요?

## 핵심 키워드
- 비트 연산 (AND, OR, XOR, NOT, SHIFT)
- 부분집합 표현
- 상태 압축 DP
- 비트마스킹 기법
- 집합 연산

## 답변
비트마스크는 정수의 이진 표현을 이용하여 집합을 표현하고 조작하는 기법입니다. 각 비트가 원소의 포함 여부를 나타내어, 부분집합을 효율적으로 표현하고 연산할 수 있습니다.

### 기본 비트 연산

```java
// AND (&): 교집합
0b1010 & 0b1100 = 0b1000

// OR (|): 합집합
0b1010 | 0b1100 = 0b1110

// XOR (^): 대칭 차집합
0b1010 ^ 0b1100 = 0b0110

// NOT (~): 여집합
~0b1010 = 0b...0101

// LEFT SHIFT (<<): 2^n 곱셈
1 << 3 = 8  (2^3)

// RIGHT SHIFT (>>): 2^n 나눗셈
8 >> 2 = 2  (8 / 2^2)
```

### 집합 연산

```java
int set = 0; // 공집합

// i번째 원소 추가
set |= (1 << i);

// i번째 원소 제거
set &= ~(1 << i);

// i번째 원소 포함 여부 확인
boolean has = (set & (1 << i)) != 0;

// i번째 원소 토글
set ^= (1 << i);

// 집합의 크기 (1의 개수)
int size = Integer.bitCount(set);

// 모든 부분집합 순회
for (int subset = set; subset > 0; subset = (subset - 1) & set) {
    // subset 처리
}
```

### 활용 1: 모든 부분집합 열거

```java
// n개 원소의 모든 부분집합: 2^n개
int n = 4;
for (int mask = 0; mask < (1 << n); mask++) {
    System.out.print("{ ");
    for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
            System.out.print(i + " ");
        }
    }
    System.out.println("}");
}
// {}, {0}, {1}, {0,1}, {2}, {0,2}, ...
```

### 활용 2: 상태 압축 DP (외판원 문제 - TSP)

```java
// dp[visited][current] = visited 집합의 도시를 방문하고
// current에 있을 때의 최소 비용
int[][] dp = new int[1 << n][n];

int tsp(int visited, int current, int[][] dist) {
    if (visited == (1 << n) - 1) { // 모든 도시 방문
        return dist[current][0]; // 출발지로 복귀
    }
    if (dp[visited][current] != -1) return dp[visited][current];

    int result = Integer.MAX_VALUE;
    for (int next = 0; next < n; next++) {
        if ((visited & (1 << next)) == 0) { // 미방문
            int cost = dist[current][next] + tsp(visited | (1 << next), next, dist);
            result = Math.min(result, cost);
        }
    }
    return dp[visited][current] = result;
}
```

### 활용 3: 권한 관리

```java
// 권한을 비트마스크로 관리
final int READ = 1;     // 001
final int WRITE = 2;    // 010
final int EXECUTE = 4;  // 100

int permission = READ | WRITE; // 011

// 권한 확인
if ((permission & EXECUTE) != 0) {
    // 실행 권한 있음
}

// 권한 추가
permission |= EXECUTE; // 111
```

### 장점
- 메모리 효율적 (하나의 정수로 집합 표현)
- 비트 연산은 매우 빠름 (CPU 한 클럭)
- 부분집합 연산이 간결

### 면접 팁
비트마스크는 코딩 테스트에서 상태 압축 DP, 부분집합 문제에 자주 등장합니다. 기본 비트 연산과 집합 표현 방법을 확실히 이해하세요.

## 꼬리 질문
1. 비트마스크 DP에서 상태 공간의 크기는?
2. 외판원 문제(TSP)를 비트마스크 DP로 푸는 시간 복잡도는?
3. n이 20을 초과하면 비트마스크 DP를 사용할 수 없는 이유는?
