---
title: "DFS & BFS"
category: algorithm
difficulty: INTERMEDIATE
tags: "DFS, BFS, 깊이 우선 탐색, 너비 우선 탐색, 그래프 탐색"
---

## 질문
DFS와 BFS의 동작 원리와 차이점에 대해 설명해주세요.

## 핵심 키워드
- 깊이 우선 탐색 (DFS)
- 너비 우선 탐색 (BFS)
- 스택 / 큐
- 최단 경로
- 그래프 탐색

## 답변

### DFS (Depth-First Search, 깊이 우선 탐색)
한 경로를 끝까지 탐색한 후 되돌아와서 다른 경로를 탐색합니다. 스택 또는 재귀로 구현합니다.

```java
// 재귀 DFS
void dfs(int node, boolean[] visited, List<List<Integer>> graph) {
    visited[node] = true;
    System.out.print(node + " ");

    for (int next : graph.get(node)) {
        if (!visited[next]) {
            dfs(next, visited, graph);
        }
    }
}

// 스택 DFS
void dfsIterative(int start, List<List<Integer>> graph) {
    boolean[] visited = new boolean[graph.size()];
    Stack<Integer> stack = new Stack<>();
    stack.push(start);

    while (!stack.isEmpty()) {
        int node = stack.pop();
        if (visited[node]) continue;
        visited[node] = true;
        System.out.print(node + " ");

        for (int next : graph.get(node)) {
            if (!visited[next]) stack.push(next);
        }
    }
}
```

### BFS (Breadth-First Search, 너비 우선 탐색)
시작 노드에서 가까운 노드부터 탐색합니다. 큐로 구현합니다.

```java
void bfs(int start, List<List<Integer>> graph) {
    boolean[] visited = new boolean[graph.size()];
    Queue<Integer> queue = new LinkedList<>();

    visited[start] = true;
    queue.offer(start);

    while (!queue.isEmpty()) {
        int node = queue.poll();
        System.out.print(node + " ");

        for (int next : graph.get(node)) {
            if (!visited[next]) {
                visited[next] = true;
                queue.offer(next);
            }
        }
    }
}
```

### 탐색 순서 비교

```
그래프:
    1
   / \
  2   3
 / \   \
4   5   6

DFS: 1 → 2 → 4 → 5 → 3 → 6 (깊이 우선)
BFS: 1 → 2 → 3 → 4 → 5 → 6 (너비 우선, 레벨별)
```

### DFS vs BFS 비교

| 특성 | DFS | BFS |
|------|-----|-----|
| 자료구조 | 스택/재귀 | 큐 |
| 메모리 | 적음 (경로 길이) | 많음 (같은 레벨 노드) |
| 최단 경로 | 보장 안 됨 | 보장 (가중치 없을 때) |
| 완전성 | 무한 그래프에서 위험 | 항상 찾음 |

### 활용 사례

**DFS가 적합한 경우:**
- 경로 탐색, 사이클 감지
- 위상 정렬
- 미로 찾기 (모든 경로 탐색)
- 백트래킹 문제
- 연결 요소 탐색

**BFS가 적합한 경우:**
- 최단 경로 (가중치가 동일할 때)
- 레벨별 탐색
- 최소 이동 횟수
- SNS 친구 추천 (n촌 관계)

### 면접 팁
"최단 경로를 구해야 하면 BFS, 모든 경로를 탐색해야 하면 DFS"라는 기본 원칙을 기억하세요. 코딩 테스트에서는 2차원 배열 그래프 탐색(상하좌우 이동)이 자주 출제됩니다.

## 꼬리 질문
1. DFS로 사이클을 감지하는 방법은?
2. BFS에서 최단 경로를 역추적하는 방법은?
3. 가중치가 있는 그래프에서 최단 경로를 구하려면?
