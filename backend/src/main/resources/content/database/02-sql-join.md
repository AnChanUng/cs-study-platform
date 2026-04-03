---
title: "SQL JOIN"
category: database
difficulty: BASIC
tags: "INNER JOIN, OUTER JOIN, CROSS JOIN, SELF JOIN"
---

## 질문
SQL에서 JOIN의 종류와 각각의 차이점에 대해 설명해주세요.

## 핵심 키워드
- INNER JOIN
- LEFT/RIGHT OUTER JOIN
- FULL OUTER JOIN
- CROSS JOIN
- SELF JOIN

## 답변
JOIN은 두 개 이상의 테이블을 연결하여 데이터를 조회하는 SQL 연산입니다. 관계형 데이터베이스에서 정규화로 분리된 테이블들을 다시 결합할 때 사용합니다.

### JOIN의 종류

**1. INNER JOIN**
두 테이블에서 조건이 일치하는 행만 반환합니다. 가장 많이 사용되는 JOIN입니다.

```sql
SELECT e.name, d.dept_name
FROM employee e
INNER JOIN department d ON e.dept_id = d.dept_id;
```

**2. LEFT OUTER JOIN**
왼쪽 테이블의 모든 행과, 오른쪽 테이블에서 조건이 일치하는 행을 반환합니다. 일치하지 않는 경우 NULL로 채워집니다.

```sql
SELECT e.name, d.dept_name
FROM employee e
LEFT JOIN department d ON e.dept_id = d.dept_id;
-- 부서가 없는 직원도 조회됨
```

**3. RIGHT OUTER JOIN**
오른쪽 테이블의 모든 행과, 왼쪽 테이블에서 조건이 일치하는 행을 반환합니다.

```sql
SELECT e.name, d.dept_name
FROM employee e
RIGHT JOIN department d ON e.dept_id = d.dept_id;
-- 직원이 없는 부서도 조회됨
```

**4. FULL OUTER JOIN**
두 테이블의 모든 행을 반환합니다. 일치하지 않는 행은 NULL로 채워집니다.

```sql
SELECT e.name, d.dept_name
FROM employee e
FULL OUTER JOIN department d ON e.dept_id = d.dept_id;
```

**5. CROSS JOIN**
두 테이블의 모든 행을 조합하는 카르테시안 곱(Cartesian Product)을 반환합니다.

```sql
SELECT e.name, d.dept_name
FROM employee e
CROSS JOIN department d;
-- 직원 수 × 부서 수만큼의 행이 반환됨
```

**6. SELF JOIN**
같은 테이블을 자기 자신과 JOIN하는 것입니다. 계층 구조를 표현할 때 유용합니다.

```sql
SELECT e.name AS 직원, m.name AS 관리자
FROM employee e
LEFT JOIN employee m ON e.manager_id = m.emp_id;
```

### JOIN 성능 최적화
- JOIN할 컬럼에 인덱스를 생성하면 성능이 크게 향상됩니다
- 필요한 컬럼만 SELECT하고, WHERE 조건을 적절히 사용합니다
- 작은 테이블을 기준으로 JOIN하는 것이 일반적으로 유리합니다

### 면접 팁
각 JOIN의 벤 다이어그램을 머릿속에 그려보세요. INNER JOIN은 교집합, LEFT JOIN은 왼쪽 원 전체, FULL OUTER JOIN은 합집합입니다. 실무에서는 INNER JOIN과 LEFT JOIN을 가장 많이 사용합니다.

## 꼬리 질문
1. JOIN과 서브쿼리의 성능 차이는 어떻게 되나요?
2. INNER JOIN에서 ON과 WHERE의 차이점은 무엇인가요?
3. N+1 문제란 무엇이며, JOIN으로 어떻게 해결할 수 있나요?
