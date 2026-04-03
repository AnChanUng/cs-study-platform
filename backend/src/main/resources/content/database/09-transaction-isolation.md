---
title: "트랜잭션 격리 수준"
category: database
difficulty: ADVANCED
tags: "격리수준, Dirty Read, Phantom Read, Non-repeatable Read, MVCC"
---

## 질문
트랜잭션의 격리 수준(Isolation Level)에 대해 설명하고, 각 수준에서 발생할 수 있는 문제점을 말해주세요.

## 핵심 키워드
- READ UNCOMMITTED
- READ COMMITTED
- REPEATABLE READ
- SERIALIZABLE
- Dirty Read / Non-repeatable Read / Phantom Read

## 답변
트랜잭션 격리 수준(Isolation Level)은 동시에 여러 트랜잭션이 실행될 때 각 트랜잭션이 다른 트랜잭션의 변경 사항을 어느 정도까지 볼 수 있는지를 결정하는 정책입니다.

### 동시성 문제

**1. Dirty Read**
커밋되지 않은 다른 트랜잭션의 데이터를 읽는 현상입니다. 해당 트랜잭션이 롤백되면 잘못된 데이터를 읽은 것이 됩니다.

**2. Non-repeatable Read**
같은 트랜잭션 내에서 같은 쿼리를 두 번 실행했을 때, 다른 트랜잭션의 수정으로 인해 다른 결과가 나오는 현상입니다.

**3. Phantom Read**
같은 쿼리를 두 번 실행했을 때, 다른 트랜잭션의 삽입/삭제로 인해 결과 집합의 행 수가 달라지는 현상입니다.

### 격리 수준

**1. READ UNCOMMITTED (레벨 0)**
커밋되지 않은 데이터도 읽을 수 있습니다. 가장 낮은 격리 수준으로, Dirty Read가 발생합니다.

```sql
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
-- 트랜잭션 B가 커밋하지 않은 데이터도 조회 가능
```

**2. READ COMMITTED (레벨 1)**
커밋된 데이터만 읽을 수 있습니다. Dirty Read는 방지하지만 Non-repeatable Read가 발생할 수 있습니다. Oracle, PostgreSQL의 기본 격리 수준입니다.

**3. REPEATABLE READ (레벨 2)**
트랜잭션이 시작된 시점의 데이터를 일관되게 읽습니다. Non-repeatable Read는 방지하지만 Phantom Read가 발생할 수 있습니다. MySQL(InnoDB)의 기본 격리 수준입니다.

**4. SERIALIZABLE (레벨 3)**
가장 엄격한 격리 수준으로, 트랜잭션을 순차적으로 실행하는 것처럼 동작합니다. 모든 동시성 문제를 방지하지만 성능이 크게 저하됩니다.

### 격리 수준별 문제 발생 여부

| 격리 수준 | Dirty Read | Non-repeatable Read | Phantom Read |
|-----------|-----------|-------------------|-------------|
| READ UNCOMMITTED | O | O | O |
| READ COMMITTED | X | O | O |
| REPEATABLE READ | X | X | O (MySQL에서는 X) |
| SERIALIZABLE | X | X | X |

### MVCC (Multi-Version Concurrency Control)
MySQL InnoDB는 MVCC를 사용하여 잠금 없이 일관된 읽기를 제공합니다. 데이터를 변경할 때 이전 버전을 Undo 영역에 보관하고, 각 트랜잭션은 자신의 시작 시점에 맞는 버전을 읽습니다.

```sql
-- MySQL에서 격리 수준 확인
SELECT @@transaction_isolation;

-- 격리 수준 변경
SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
```

### 면접 팁
MySQL의 REPEATABLE READ에서는 Next-Key Lock을 통해 Phantom Read까지 방지한다는 점을 알면 차별화됩니다. 격리 수준은 동시성과 일관성의 트레이드오프이므로, 실무에서 어떤 기준으로 선택하는지도 설명할 수 있으면 좋습니다.

## 꼬리 질문
1. MVCC란 무엇이며, 어떻게 동시성을 보장하나요?
2. MySQL의 REPEATABLE READ에서 Phantom Read가 방지되는 원리는 무엇인가요?
3. 실무에서 격리 수준을 어떤 기준으로 선택하나요?
