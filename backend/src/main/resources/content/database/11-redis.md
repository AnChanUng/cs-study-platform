---
title: "레디스(Redis)"
category: database
difficulty: INTERMEDIATE
tags: "Redis, 인메모리, 캐시, NoSQL, 자료구조"
---

## 질문
Redis란 무엇이며, 어떤 상황에서 사용하나요?

## 핵심 키워드
- 인메모리 데이터 저장소
- Key-Value 구조
- 캐시
- 다양한 자료구조
- 싱글 스레드

## 답변
Redis(Remote Dictionary Server)는 오픈소스 인메모리 Key-Value 데이터 구조 저장소입니다. 데이터를 메모리에 저장하여 매우 빠른 읽기/쓰기 성능을 제공합니다.

### 주요 특징

**1. 인메모리 저장소**
모든 데이터를 RAM에 저장하여 디스크 기반 DB보다 훨씬 빠릅니다. 평균 읽기/쓰기 속도가 1ms 이하입니다.

**2. 다양한 자료구조 지원**

```
- String: 가장 기본적인 자료형
  SET user:1:name "홍길동"
  GET user:1:name

- List: 연결 리스트
  LPUSH queue "task1"
  RPOP queue

- Set: 중복 없는 집합
  SADD tags "java" "spring" "redis"
  SMEMBERS tags

- Sorted Set: 점수(score) 기반 정렬 집합
  ZADD ranking 100 "player1"
  ZRANGE ranking 0 -1 WITHSCORES

- Hash: 필드-값 쌍의 집합
  HSET user:1 name "홍길동" age 25
  HGETALL user:1
```

**3. 싱글 스레드**
이벤트 루프 기반의 싱글 스레드로 동작합니다. Race Condition이 발생하지 않아 원자적 연산이 보장됩니다. 단, 오래 걸리는 명령(KEYS * 등)은 전체 서버를 블로킹하므로 주의해야 합니다.

**4. 영속성 (Persistence)**
인메모리이지만 데이터를 디스크에 저장하는 방법을 제공합니다:
- **RDB (Snapshot)**: 특정 시점의 메모리 스냅샷을 저장
- **AOF (Append Only File)**: 모든 쓰기 명령을 로그에 기록

### 주요 사용 사례

**1. 캐시 (Cache)**
자주 조회되는 데이터를 Redis에 캐싱하여 DB 부하를 줄입니다.

```java
// Spring에서 Redis 캐시 사용
@Cacheable(value = "users", key = "#userId")
public User getUser(Long userId) {
    return userRepository.findById(userId).orElseThrow();
}
```

**2. 세션 저장소**
분산 서버 환경에서 세션을 Redis에 저장하여 세션 클러스터링을 구현합니다.

**3. 메시지 큐**
Pub/Sub 기능을 활용하여 메시지 브로커로 사용합니다.

**4. 실시간 랭킹**
Sorted Set을 활용하여 실시간 리더보드를 구현합니다.

**5. 분산 락 (Distributed Lock)**
여러 서버에서 공유 자원에 대한 동기화를 구현합니다.

### 주의사항
- 메모리 용량이 한정적이므로 TTL(Time To Live) 설정이 중요합니다
- 캐시 전략(Cache Aside, Write Through 등)을 적절히 선택해야 합니다
- 캐시 침투(Cache Penetration), 캐시 눈사태(Cache Avalanche) 등의 문제를 고려해야 합니다

### 면접 팁
Redis를 단순히 "빠른 캐시"로만 설명하지 말고, 다양한 자료구조와 구체적인 사용 사례를 함께 설명하세요. 캐시 전략과 관련된 문제(Cache Aside 패턴, TTL 전략 등)도 함께 준비하면 좋습니다.

## 꼬리 질문
1. Redis의 캐시 전략(Cache Aside, Write Through, Write Behind)에 대해 설명해주세요.
2. Redis의 싱글 스레드 모델에서 높은 성능을 낼 수 있는 이유는 무엇인가요?
3. Redis Cluster와 Redis Sentinel의 차이는 무엇인가요?
