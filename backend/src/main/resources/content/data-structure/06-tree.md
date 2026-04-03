---
title: "트리(Tree)"
category: data-structure
difficulty: INTERMEDIATE
tags: "트리, 이진 트리, 완전 이진 트리, 트리 순회, 노드"
---

## 질문
트리(Tree) 자료구조의 개념과 종류, 순회 방법에 대해 설명해주세요.

## 핵심 키워드
- 루트 노드 / 리프 노드
- 이진 트리
- 완전 이진 트리 / 포화 이진 트리
- 전위/중위/후위/레벨 순회
- 높이 / 깊이

## 답변
트리(Tree)는 노드와 간선으로 이루어진 비선형 계층적 자료구조입니다. 하나의 루트 노드에서 시작하여 자식 노드로 확장되며, 사이클이 없는 연결 그래프입니다.

### 트리 용어
- **루트(Root)**: 최상위 노드
- **리프(Leaf)**: 자식이 없는 말단 노드
- **부모(Parent)**: 상위 노드
- **자식(Child)**: 하위 노드
- **깊이(Depth)**: 루트에서 특정 노드까지의 거리
- **높이(Height)**: 트리의 최대 깊이
- **차수(Degree)**: 노드의 자식 수

### 트리의 종류

**이진 트리 (Binary Tree)**: 각 노드가 최대 2개의 자식을 가짐

**완전 이진 트리 (Complete Binary Tree)**: 마지막 레벨을 제외한 모든 레벨이 채워져 있고, 마지막 레벨은 왼쪽부터 채워짐

**포화 이진 트리 (Full Binary Tree)**: 모든 레벨이 완전히 채워진 이진 트리

**이진 탐색 트리 (BST)**: 왼쪽 자식 < 부모 < 오른쪽 자식 규칙을 만족

### 트리 구현

```java
class TreeNode {
    int data;
    TreeNode left;
    TreeNode right;

    TreeNode(int data) {
        this.data = data;
    }
}
```

### 트리 순회

```java
class TreeTraversal {
    // 전위 순회 (Pre-order): 루트 → 왼쪽 → 오른쪽
    void preorder(TreeNode node) {
        if (node == null) return;
        System.out.print(node.data + " ");
        preorder(node.left);
        preorder(node.right);
    }

    // 중위 순회 (In-order): 왼쪽 → 루트 → 오른쪽
    void inorder(TreeNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.data + " ");
        inorder(node.right);
    }

    // 후위 순회 (Post-order): 왼쪽 → 오른쪽 → 루트
    void postorder(TreeNode node) {
        if (node == null) return;
        postorder(node.left);
        postorder(node.right);
        System.out.print(node.data + " ");
    }

    // 레벨 순회 (Level-order): BFS 방식
    void levelorder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.data + " ");
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }
}
```

### 순회 결과 예시
```
        1
       / \
      2   3
     / \
    4   5

전위: 1 2 4 5 3
중위: 4 2 5 1 3
후위: 4 5 2 3 1
레벨: 1 2 3 4 5
```

### 면접 팁
트리 순회는 재귀적 방법과 반복적(스택 사용) 방법을 모두 구현할 수 있어야 합니다. BST에서 중위 순회를 하면 오름차순으로 정렬된 결과를 얻는다는 점도 자주 출제됩니다.

## 꼬리 질문
1. 이진 트리의 높이가 h일 때 최대 노드 수는 몇 개인가요?
2. 트리 순회를 재귀 없이 반복문으로 구현하는 방법은?
3. 이진 탐색 트리가 편향되면 어떤 문제가 발생하나요?
