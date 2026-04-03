---
title: "최소 공통 조상(LCA)"
category: algorithm
difficulty: ADVANCED
tags: "LCA, 최소 공통 조상, 희소 배열, 트리, 이분 탐색"
---

## 질문
최소 공통 조상(LCA) 알고리즘에 대해 설명해주세요.

## 핵심 키워드
- Lowest Common Ancestor
- 나이브: O(N)
- 희소 배열(Sparse Table): O(log N)
- 깊이(depth)
- 2^k 조상

## 답변
최소 공통 조상(LCA, Lowest Common Ancestor)은 트리에서 두 노드의 공통 조상 중 가장 깊은(가장 가까운) 노드를 찾는 알고리즘입니다.

### 예시
```
        1
       / \
      2   3
     / \   \
    4   5   6
   /
  7

LCA(7, 5) = 2
LCA(4, 6) = 1
LCA(7, 2) = 2
```

### 방법 1: 나이브 - O(N)

두 노드의 깊이를 맞춘 후 동시에 올라갑니다.

```java
int lcaNaive(int u, int v, int[] parent, int[] depth) {
    // 1. 깊이 맞추기
    while (depth[u] > depth[v]) u = parent[u];
    while (depth[v] > depth[u]) v = parent[v];

    // 2. 같은 노드가 될 때까지 동시에 올라가기
    while (u != v) {
        u = parent[u];
        v = parent[v];
    }
    return u;
}
```

### 방법 2: 희소 배열 (Sparse Table) - O(log N)

전처리로 각 노드의 2^k번째 조상을 미리 계산합니다.

```java
class LCA {
    int[][] ancestor; // ancestor[k][v] = v의 2^k번째 조상
    int[] depth;
    int LOG;

    // 전처리: O(N log N)
    void preprocess(int root, List<List<Integer>> tree) {
        int n = tree.size();
        LOG = (int) (Math.log(n) / Math.log(2)) + 1;
        ancestor = new int[LOG][n];
        depth = new int[n];

        // BFS로 깊이와 부모 계산
        bfs(root, tree);

        // 희소 배열 구성
        for (int k = 1; k < LOG; k++) {
            for (int v = 0; v < n; v++) {
                if (ancestor[k-1][v] != -1) {
                    ancestor[k][v] = ancestor[k-1][ancestor[k-1][v]];
                } else {
                    ancestor[k][v] = -1;
                }
            }
        }
    }

    // LCA 쿼리: O(log N)
    int query(int u, int v) {
        // 1. 깊이 맞추기
        if (depth[u] < depth[v]) { int tmp = u; u = v; v = tmp; }
        int diff = depth[u] - depth[v];

        for (int k = 0; k < LOG; k++) {
            if (((diff >> k) & 1) == 1) {
                u = ancestor[k][u];
            }
        }

        // 2. 같으면 LCA
        if (u == v) return u;

        // 3. 동시에 올라가기 (이분 탐색 방식)
        for (int k = LOG - 1; k >= 0; k--) {
            if (ancestor[k][u] != ancestor[k][v]) {
                u = ancestor[k][u];
                v = ancestor[k][v];
            }
        }

        return ancestor[0][u]; // 부모가 LCA
    }
}
```

### 시간 복잡도

| 방법 | 전처리 | 쿼리 |
|------|--------|------|
| 나이브 | O(N) | O(N) |
| 희소 배열 | O(N log N) | O(log N) |
| 오일러 투어 + RMQ | O(N log N) | O(1) |

### 활용 사례
- 트리에서 두 노드 사이의 거리 계산
- 트리에서 경로의 가중치 합 계산
- 파일 시스템에서 공통 디렉토리 찾기

### 면접 팁
LCA 알고리즘의 핵심은 "2^k 점프"입니다. 이진수 표현을 이용하여 임의의 거리를 O(log N)에 이동할 수 있다는 원리를 이해하세요. 쿼리가 많은 경우 O(N log N) 전처리 + O(log N) 쿼리가 효율적입니다.

## 꼬리 질문
1. LCA를 이용하여 트리에서 두 노드 간 거리를 구하는 방법은?
2. 오일러 투어와 RMQ를 이용한 O(1) LCA 방법은?
3. 희소 배열에서 ancestor[k][v]의 의미는?
