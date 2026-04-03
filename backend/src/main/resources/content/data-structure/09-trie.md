---
title: "트라이(Trie)"
category: data-structure
difficulty: ADVANCED
tags: "트라이, 문자열 검색, 자동완성, 접두사 트리"
---

## 질문
트라이(Trie) 자료구조의 개념과 활용 사례에 대해 설명해주세요.

## 핵심 키워드
- 접두사 트리 (Prefix Tree)
- 문자열 검색
- 자동완성
- 공간 복잡도
- O(m) 탐색 (m: 문자열 길이)

## 답변
트라이(Trie)는 문자열을 저장하고 효율적으로 탐색하기 위한 트리 형태의 자료구조입니다. "Retrieval"에서 유래한 이름으로, 접두사 트리(Prefix Tree)라고도 합니다.

### 구조

각 노드는 문자 하나를 나타내며, 루트에서 특정 노드까지의 경로가 하나의 문자열(접두사)을 나타냅니다.

```
예: "cat", "car", "card", "dog" 저장

        root
       /    \
      c      d
      |      |
      a      o
     / \     |
    t   r    g*
    *   |
        d*

(* = 단어의 끝)
```

### 구현

```java
class TrieNode {
    TrieNode[] children = new TrieNode[26]; // 알파벳 소문자
    boolean isEndOfWord;
}

class Trie {
    TrieNode root = new TrieNode();

    // 삽입: O(m)
    void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEndOfWord = true;
    }

    // 검색: O(m)
    boolean search(String word) {
        TrieNode node = findNode(word);
        return node != null && node.isEndOfWord;
    }

    // 접두사 검색: O(m)
    boolean startsWith(String prefix) {
        return findNode(prefix) != null;
    }

    private TrieNode findNode(String str) {
        TrieNode node = root;
        for (char c : str.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) return null;
            node = node.children[index];
        }
        return node;
    }

    // 자동완성: 접두사로 시작하는 모든 단어 찾기
    List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = findNode(prefix);
        if (node != null) {
            dfs(node, new StringBuilder(prefix), results);
        }
        return results;
    }

    private void dfs(TrieNode node, StringBuilder sb, List<String> results) {
        if (node.isEndOfWord) results.add(sb.toString());
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                sb.append((char) ('a' + i));
                dfs(node.children[i], sb, results);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }
}
```

### 시간 복잡도

| 연산 | 시간 복잡도 |
|------|-----------|
| 삽입 | O(m) |
| 검색 | O(m) |
| 접두사 검색 | O(m) |
| 삭제 | O(m) |

m = 문자열의 길이

### 장점
- 문자열 검색이 O(m)으로 매우 빠름 (해시 테이블의 해시 충돌 문제 없음)
- 접두사 기반 검색에 최적화
- 사전순 정렬이 자연스럽게 유지됨

### 단점
- 메모리 사용량이 큼 (각 노드마다 자식 포인터 배열 필요)
- 문자 종류가 많으면(유니코드) 메모리 낭비가 심함

### 활용 사례
- **자동완성**: 검색 엔진의 검색어 추천
- **맞춤법 검사**: 사전에 단어가 존재하는지 확인
- **IP 라우팅**: Longest Prefix Matching
- **문자열 매칭**: 여러 패턴을 동시에 검색

### 면접 팁
트라이는 공간 효율을 개선한 변형 구조(압축 트라이, 삼진 탐색 트라이)도 있다는 것을 알고 있으면 좋습니다. 해시 테이블과 비교하여 각각의 장단점을 설명할 수 있어야 합니다.

## 꼬리 질문
1. 트라이와 해시 테이블 중 문자열 검색에 어떤 것이 더 적합한가요?
2. 트라이의 메모리 사용량을 줄이는 방법은?
3. 압축 트라이(Radix Tree)란 무엇인가요?
