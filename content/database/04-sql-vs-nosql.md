---
title: "SQL vs NoSQL"
category: database
difficulty: INTERMEDIATE
tags: "SQL, NoSQL, RDBMS, MongoDB, 관계형 데이터베이스"
---

## 질문
SQL과 NoSQL의 차이점을 설명하고, 각각 어떤 상황에서 사용하는 것이 적합한가요?

## 핵심 키워드
- 관계형 데이터베이스 (RDBMS)
- 비관계형 데이터베이스 (NoSQL)
- 스키마
- 수평적/수직적 확장
- CAP 이론

## 답변
SQL(Structured Query Language) 데이터베이스는 관계형 모델을 기반으로 정형화된 스키마를 사용하며, NoSQL(Not Only SQL)은 비관계형 모델로 유연한 스키마를 제공합니다.

### SQL (관계형 데이터베이스)

**특징:**
- 정해진 스키마에 따라 데이터를 저장합니다
- 테이블 간 관계(Relationship)를 통해 데이터를 관리합니다
- ACID 트랜잭션을 보장합니다
- SQL이라는 표준화된 질의 언어를 사용합니다

**대표 제품:** MySQL, PostgreSQL, Oracle, MSSQL

```sql
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    product_name VARCHAR(100),
    amount DECIMAL(10,2)
);
```

### NoSQL (비관계형 데이터베이스)

**유형:**
- **문서형(Document)**: MongoDB, CouchDB - JSON/BSON 형태로 저장
- **키-값(Key-Value)**: Redis, DynamoDB - 단순한 키-값 쌍으로 저장
- **컬럼형(Column-Family)**: Cassandra, HBase - 컬럼 단위로 저장
- **그래프형(Graph)**: Neo4j - 노드와 엣지로 관계를 표현

```javascript
// MongoDB 문서 예시
{
  "_id": ObjectId("..."),
  "name": "홍길동",
  "orders": [
    { "product": "노트북", "amount": 1500000 },
    { "product": "마우스", "amount": 35000 }
  ]
}
```

### 비교

| 항목 | SQL | NoSQL |
|------|-----|-------|
| 스키마 | 고정 스키마 | 유연한 스키마 |
| 확장성 | 수직적 확장 (Scale-up) | 수평적 확장 (Scale-out) |
| 트랜잭션 | ACID 보장 | BASE (Eventually Consistent) |
| 관계 | JOIN을 통한 관계 표현 | 비정규화, 내장 문서 |
| 일관성 | 강한 일관성 | 최종적 일관성 |

### 선택 기준

**SQL이 적합한 경우:**
- 데이터 구조가 명확하고 변경이 적을 때
- 복잡한 쿼리와 JOIN이 필요할 때
- 트랜잭션의 ACID가 중요할 때 (금융, 결제 시스템)

**NoSQL이 적합한 경우:**
- 데이터 구조가 자주 변경될 때
- 대용량 데이터를 빠르게 처리해야 할 때
- 수평적 확장이 필요할 때 (SNS, IoT, 로그 데이터)

### 면접 팁
CAP 이론(Consistency, Availability, Partition Tolerance)과 연관하여 설명하면 깊이 있는 답변이 됩니다. 실무에서는 하나만 선택하기보다 Polyglot Persistence 전략으로 두 가지를 함께 사용하는 경우가 많습니다.

## 꼬리 질문
1. CAP 이론이란 무엇이며, SQL과 NoSQL은 각각 어떤 특성을 우선시하나요?
2. NoSQL에서 트랜잭션을 보장하려면 어떻게 해야 하나요?
3. MongoDB에서 스키마 설계 시 내장(Embedding)과 참조(Referencing)를 어떤 기준으로 선택하나요?
