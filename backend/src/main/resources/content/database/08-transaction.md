---
title: "트랜잭션(Transaction)"
category: database
difficulty: INTERMEDIATE
tags: "트랜잭션, ACID, 커밋, 롤백, 원자성"
---

## 질문
트랜잭션이란 무엇이며, ACID 속성에 대해 설명해주세요.

## 핵심 키워드
- 트랜잭션
- 원자성 (Atomicity)
- 일관성 (Consistency)
- 격리성 (Isolation)
- 지속성 (Durability)

## 답변
트랜잭션(Transaction)은 데이터베이스의 상태를 변환시키는 하나의 논리적 작업 단위입니다. 여러 개의 SQL 연산이 하나의 작업으로 묶여, 모두 성공하거나 모두 실패해야 합니다.

### 트랜잭션 예시: 계좌 이체

```sql
BEGIN TRANSACTION;

-- A 계좌에서 10만원 출금
UPDATE account SET balance = balance - 100000 WHERE id = 'A';

-- B 계좌에 10만원 입금
UPDATE account SET balance = balance + 100000 WHERE id = 'B';

-- 모두 성공하면 커밋
COMMIT;

-- 하나라도 실패하면 롤백
-- ROLLBACK;
```

### ACID 속성

**1. 원자성 (Atomicity)**
트랜잭션의 모든 연산이 완전히 수행되거나, 전혀 수행되지 않아야 합니다. "All or Nothing" 원칙입니다. 중간에 실패하면 이전 상태로 롤백됩니다.

**2. 일관성 (Consistency)**
트랜잭션 수행 전후에 데이터베이스가 일관된 상태를 유지해야 합니다. 예를 들어, 계좌 이체 후 두 계좌의 합은 이전과 동일해야 합니다.

**3. 격리성 (Isolation)**
동시에 실행되는 트랜잭션들이 서로 영향을 미치지 않아야 합니다. 각 트랜잭션은 독립적으로 실행되는 것처럼 보여야 합니다.

**4. 지속성 (Durability)**
성공적으로 완료(COMMIT)된 트랜잭션의 결과는 영구적으로 데이터베이스에 반영되어야 합니다. 시스템 장애가 발생해도 유지됩니다.

### 트랜잭션 상태

```
Active → Partially Committed → Committed
  ↓                ↓
Failed         Failed
  ↓                ↓
Aborted        Aborted
```

- **Active**: 트랜잭션이 실행 중인 상태
- **Partially Committed**: 마지막 연산까지 실행했지만 아직 커밋하지 않은 상태
- **Committed**: 트랜잭션이 성공적으로 완료된 상태
- **Failed**: 오류가 발생하여 중단된 상태
- **Aborted**: 트랜잭션이 취소되고 롤백된 상태

### 트랜잭션 관리 (Spring)

```java
@Transactional
public void transfer(String from, String to, int amount) {
    accountRepository.withdraw(from, amount);
    accountRepository.deposit(to, amount);
}
// 메서드 내에서 예외 발생 시 자동 롤백
```

### 면접 팁
ACID 각 속성의 정의뿐만 아니라, 각 속성이 어떤 메커니즘으로 보장되는지 설명할 수 있으면 좋습니다. 예를 들어 원자성은 Undo Log, 지속성은 Redo Log를 통해 보장됩니다.

## 꼬리 질문
1. 트랜잭션의 격리 수준에는 어떤 것들이 있나요?
2. Spring의 @Transactional에서 propagation 옵션은 어떤 것이 있나요?
3. 분산 트랜잭션이란 무엇이며, 어떻게 처리하나요?
