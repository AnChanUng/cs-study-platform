---
title: "해시 테이블 구현"
category: algorithm
difficulty: INTERMEDIATE
tags: "해시 테이블, 해시 함수, 충돌 해결, 체이닝, 개방 주소법"
---

## 질문
해시 테이블을 직접 구현한다면 어떻게 설계하시겠습니까?

## 핵심 키워드
- 해시 함수 설계
- 충돌 해결 (체이닝, 개방 주소법)
- 리사이징 (Resizing)
- 로드 팩터
- 시간 복잡도 O(1)

## 답변

### 해시 테이블 구현

```java
public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private LinkedList<Entry<K, V>>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    // 해시 함수
    private int hash(K key) {
        int h = key.hashCode();
        // 상위 비트를 하위 비트에 분산 (Java HashMap과 동일)
        h ^= (h >>> 16);
        return h & (table.length - 1);
    }

    // 삽입: O(1) 평균
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = hash(key);
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        // 기존 키가 있으면 갱신
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        table[index].add(new Entry<>(key, value));
        size++;
    }

    // 조회: O(1) 평균
    public V get(K key) {
        int index = hash(key);
        if (table[index] == null) return null;

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    // 삭제: O(1) 평균
    public V remove(K key) {
        int index = hash(key);
        if (table[index] == null) return null;

        Iterator<Entry<K, V>> it = table[index].iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            if (entry.key.equals(key)) {
                V value = entry.value;
                it.remove();
                size--;
                return value;
            }
        }
        return null;
    }

    // 리사이징: O(n)
    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedList<Entry<K, V>>[] oldTable = table;
        table = new LinkedList[oldTable.length * 2];
        size = 0;

        for (LinkedList<Entry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }

    static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
```

### 좋은 해시 함수의 조건
1. **균일 분포**: 해시 값이 고르게 분포되어야 합니다
2. **빠른 계산**: O(1)에 계산 가능해야 합니다
3. **결정적**: 같은 입력에 항상 같은 출력

### 설계 시 고려사항

**테이블 크기**: 2의 거듭제곱으로 설정하면 비트 연산으로 모듈러 계산 가능
```java
index = hash & (capacity - 1); // capacity가 2의 거듭제곱일 때
```

**로드 팩터**: 0.75가 일반적. 높으면 메모리 절약 but 충돌 증가, 낮으면 반대

**리사이징**: 로드 팩터 초과 시 2배로 확장하고 모든 엔트리를 재해싱

### 면접 팁
Java HashMap의 내부 구현을 알고 있으면 좋습니다: 초기 용량 16, 로드 팩터 0.75, 체이닝 길이가 8을 넘으면 Red-Black Tree로 변환, 6 이하로 줄면 다시 연결 리스트로 변환됩니다.

## 꼬리 질문
1. Java HashMap의 리사이징 과정에서 성능 저하를 최소화하는 방법은?
2. thread-safe한 해시 테이블은 어떻게 구현하나요?
3. HashMap에서 key로 사용되는 객체의 조건은?
