---
title: "다익스트라(Dijkstra) 알고리즘"
category: algorithm
difficulty: INTERMEDIATE
tags: "다익스트라, 최단 경로, 그리디, 우선순위 큐, 가중치 그래프"
---

## 질문
다익스트라 알고리즘의 동작 원리와 시간 복잡도에 대해 설명해주세요.

## 핵심 키워드
- 최단 경로
- 우선순위 큐
- 그리디 알고리즘
- 음수 가중치 불가
- O((V+E) log V)

## 답변
다익스트라(Dijkstra) 알고리즘은 하나의 출발 노드에서 다른 모든 노드까지의 최단 경로를 구하는 알고리즘입니다. 음수 가중치가 없는 그래프에서만 동작합니다.

### 동작 원리

1. 출발 노드의 거리를 0, 나머지는 무한대로 초기화
2. 방문하지 않은 노드 중 거리가 가장 짧은 노드를 선택
3. 선택한 노드의 인접 노드들의 거리를 갱신 (Relaxation)
4. 모든 노드를 방문할 때까지 2-3 반복

### 구현 (우선순위 큐)

```java
int[] dijkstra(int start, List<int[]>[] graph, int n) {
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[start] = 0;

    // 우선순위 큐: [거리, 노드]
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{0, start});

    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int currDist = curr[0];
        int currNode = curr[1];

        // 이미 더 짧은 경로로 처리된 경우 스킵
        if (currDist > dist[currNode]) continue;

        // 인접 노드 탐색
        for (int[] edge : graph[currNode]) {
            int next = edge[0];
            int weight = edge[1];
            int newDist = dist[currNode] + weight;

            if (newDist < dist[next]) {
                dist[next] = newDist;
                pq.offer(new int[]{newDist, next});
            }
        }
    }
    return dist;
}
```

### 동작 예시

```
그래프:
A --1-- B --2-- D
|       |       |
4       3       1
|       |       |
C --5-- E --2-- F

출발: A

초기: A=0, B=INF, C=INF, D=INF, E=INF, F=INF

Step 1: A 선택 → B=1, C=4
Step 2: B 선택 → D=3, E=4
Step 3: D 선택 → F=4
Step 4: C 선택 → (갱신 없음)
Step 5: E 선택 → (갱신 없음)
Step 6: F 선택

결과: A=0, B=1, C=4, D=3, E=4, F=4
```

### 시간 복잡도

| 구현 방식 | 시간 복잡도 |
|-----------|-----------|
| 배열 | O(V^2) |
| 우선순위 큐 (이진 힙) | O((V+E) log V) |
| 피보나치 힙 | O(V log V + E) |

### 음수 가중치에서 사용 불가한 이유
다익스트라는 그리디 방식으로 최단 거리가 확정된 노드를 다시 방문하지 않습니다. 음수 가중치가 있으면 이미 확정된 거리가 나중에 더 짧아질 수 있어 올바른 결과를 보장하지 않습니다. 음수 가중치가 있으면 벨만-포드 알고리즘을 사용합니다.

### 경로 역추적

```java
int[] prev = new int[n]; // 이전 노드 기록
Arrays.fill(prev, -1);

// relaxation 시
if (newDist < dist[next]) {
    dist[next] = newDist;
    prev[next] = currNode; // 경로 기록
    pq.offer(new int[]{newDist, next});
}

// 역추적
List<Integer> path = new ArrayList<>();
for (int v = target; v != -1; v = prev[v]) {
    path.add(v);
}
Collections.reverse(path);
```

### 면접 팁
다익스트라 알고리즘이 음수 가중치에서 동작하지 않는 이유와, 그 대안(벨만-포드, SPFA)을 알고 있으면 좋습니다. 우선순위 큐 구현이 표준이며, 이미 처리된 노드를 스킵하는 최적화가 중요합니다.

## 꼬리 질문
1. 다익스트라와 벨만-포드 알고리즘의 차이점은?
2. 음수 사이클이 있을 때 어떻게 감지하나요?
3. 모든 쌍 최단 경로를 구하는 플로이드-워셜 알고리즘은?
