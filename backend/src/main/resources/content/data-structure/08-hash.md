---
title: "해시(Hash)"
category: data-structure
difficulty: INTERMEDIATE
tags: "해시, 해시 함수, 해시 충돌, 체이닝, 개방 주소법"
---

## 질문
해시 테이블의 동작 원리와 충돌 해결 방법에 대해 설명해주세요.

## 핵심 키워드
- 해시 함수
- 해시 충돌
- 체이닝 (Chaining)
- 개방 주소법 (Open Addressing)
- 로드 팩터

## 답변
해시 테이블(Hash Table)은 키(Key)를 해시 함수(Hash Function)에 넣어 나온 해시 값을 인덱스로 사용하여 데이터를 저장하고 검색하는 자료구조입니다. 평균 O(1)의 시간 복잡도로 데이터에 접근할 수 있습니다.

### 동작 원리

```
키(Key) → 해시 함수 → 해시 값(인덱스) → 버킷(Bucket)에 저장
"apple"  → hash()  → 3            → table[3] = "apple"
```

```java
// 간단한 해시 함수 예시
int hash(String key) {
    int hashValue = 0;
    for (char c : key.toCharArray()) {
        hashValue = (hashValue * 31 + c) % tableSize;
    }
    return hashValue;
}
```

### 해시 충돌 (Hash Collision)
서로 다른 키가 동일한 해시 값을 가지는 현상입니다. 해시 함수의 출력 범위가 유한하므로 충돌은 불가피합니다.

### 충돌 해결 방법

**1. 체이닝 (Separate Chaining)**
같은 해시 값을 가진 데이터를 연결 리스트로 연결합니다.

```java
class HashTable {
    LinkedList<Entry>[] table;

    void put(String key, String value) {
        int index = hash(key);
        for (Entry entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value; // 키가 이미 존재하면 갱신
                return;
            }
        }
        table[index].add(new Entry(key, value));
    }
}
```

장점: 삭제가 간단, 테이블 확장이 유연
단점: 메모리 추가 사용, 캐시 효율 낮음

**2. 개방 주소법 (Open Addressing)**
충돌 시 다른 빈 버킷을 탐색하여 저장합니다.

- **선형 탐사(Linear Probing)**: 다음 인덱스를 순차적으로 탐색
  ```
  index = (hash(key) + i) % tableSize
  ```
- **이차 탐사(Quadratic Probing)**: 제곱 간격으로 탐색
  ```
  index = (hash(key) + i²) % tableSize
  ```
- **이중 해싱(Double Hashing)**: 두 번째 해시 함수로 탐사 간격 결정
  ```
  index = (hash1(key) + i * hash2(key)) % tableSize
  ```

### 로드 팩터 (Load Factor)
```
Load Factor = 저장된 항목 수 / 테이블 크기
```
로드 팩터가 높아지면 충돌이 빈번해져 성능이 저하됩니다. Java의 HashMap은 로드 팩터가 0.75를 초과하면 테이블 크기를 2배로 확장(rehashing)합니다.

### 시간 복잡도

| 연산 | 평균 | 최악 |
|------|------|------|
| 삽입 | O(1) | O(n) |
| 삭제 | O(1) | O(n) |
| 탐색 | O(1) | O(n) |

### Java의 HashMap
Java 8부터 HashMap의 체이닝에서 연결 리스트의 길이가 8을 초과하면 Red-Black Tree로 변환하여 최악의 경우에도 O(log n)을 보장합니다.

### 면접 팁
좋은 해시 함수의 조건(균일 분포, 빠른 계산)과 Java HashMap의 내부 구현(초기 용량, 로드 팩터, 트리화)을 함께 설명할 수 있으면 좋습니다.

## 꼬리 질문
1. Java HashMap에서 해시 충돌이 발생하면 내부적으로 어떻게 처리하나요?
2. hashCode()와 equals()를 함께 오버라이드해야 하는 이유는?
3. ConcurrentHashMap과 HashMap의 차이는?
