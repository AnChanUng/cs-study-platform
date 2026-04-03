---
title: "이진탐색트리(BST)"
category: data-structure
difficulty: INTERMEDIATE
tags: "이진탐색트리, BST, 탐색, 삽입, 삭제"
---

## 질문
이진 탐색 트리(BST)의 특징과 연산의 시간 복잡도에 대해 설명해주세요.

## 핵심 키워드
- 왼쪽 < 부모 < 오른쪽
- O(log n) 평균 탐색
- 편향 트리 문제
- 중위 순회 = 정렬
- AVL, Red-Black Tree

## 답변
이진 탐색 트리(Binary Search Tree, BST)는 이진 트리의 일종으로, 각 노드에 대해 "왼쪽 서브트리의 모든 노드 값 < 현재 노드 값 < 오른쪽 서브트리의 모든 노드 값" 규칙을 만족하는 자료구조입니다.

### 주요 연산 구현

```java
class BST {
    TreeNode root;

    // 탐색: 평균 O(log n)
    TreeNode search(TreeNode node, int key) {
        if (node == null || node.data == key) return node;
        if (key < node.data) return search(node.left, key);
        return search(node.right, key);
    }

    // 삽입: 평균 O(log n)
    TreeNode insert(TreeNode node, int key) {
        if (node == null) return new TreeNode(key);
        if (key < node.data) node.left = insert(node.left, key);
        else if (key > node.data) node.right = insert(node.right, key);
        return node;
    }

    // 삭제: 평균 O(log n)
    TreeNode delete(TreeNode node, int key) {
        if (node == null) return null;

        if (key < node.data) {
            node.left = delete(node.left, key);
        } else if (key > node.data) {
            node.right = delete(node.right, key);
        } else {
            // Case 1: 리프 노드 또는 자식이 하나
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Case 2: 자식이 둘 - 중위 후속자로 대체
            TreeNode successor = findMin(node.right);
            node.data = successor.data;
            node.right = delete(node.right, successor.data);
        }
        return node;
    }

    TreeNode findMin(TreeNode node) {
        while (node.left != null) node = node.left;
        return node;
    }
}
```

### 삭제 시 세 가지 경우
1. **리프 노드 삭제**: 단순히 제거
2. **자식이 하나인 노드 삭제**: 자식을 부모에 연결
3. **자식이 둘인 노드 삭제**: 중위 후속자(오른쪽 서브트리의 최소값) 또는 중위 선행자(왼쪽 서브트리의 최대값)로 대체

### 시간 복잡도

| 연산 | 평균 | 최악 (편향) |
|------|------|-----------|
| 탐색 | O(log n) | O(n) |
| 삽입 | O(log n) | O(n) |
| 삭제 | O(log n) | O(n) |

### 편향 트리 문제

정렬된 데이터를 순서대로 삽입하면 한쪽으로 치우친 편향 트리가 됩니다.
```
1 → 2 → 3 → 4 → 5  (사실상 연결 리스트)
```
이 경우 모든 연산이 O(n)으로 퇴화합니다.

### 해결: 균형 이진 탐색 트리
- **AVL 트리**: 모든 노드에서 좌우 서브트리 높이 차이가 1 이하
- **Red-Black 트리**: 색상 규칙을 통해 대략적인 균형 유지 (Java의 TreeMap, TreeSet)
- **Splay 트리**: 최근 접근한 노드를 루트로 올림

### 면접 팁
BST의 중위 순회가 정렬된 결과를 반환한다는 점은 자주 출제됩니다. 또한 BST의 최악 케이스와 이를 해결하는 균형 트리(특히 Red-Black Tree)에 대해서도 설명할 수 있어야 합니다.

## 꼬리 질문
1. AVL 트리와 Red-Black 트리의 차이점은 무엇인가요?
2. BST에서 k번째 작은 원소를 O(log n)에 찾는 방법은?
3. Java의 TreeMap이 Red-Black Tree를 사용하는 이유는?
