---
title: "저장 프로시저(Stored Procedure)"
category: database
difficulty: INTERMEDIATE
tags: "저장 프로시저, 함수, 트리거, 성능, SQL"
---

## 질문
저장 프로시저(Stored Procedure)란 무엇이며, 장단점에 대해 설명해주세요.

## 핵심 키워드
- 저장 프로시저
- 프리컴파일
- 네트워크 트래픽 감소
- 보안
- 유지보수

## 답변
저장 프로시저(Stored Procedure)는 일련의 SQL 문을 하나의 함수처럼 실행하기 위해 데이터베이스에 미리 저장해 둔 SQL 집합입니다. 자주 사용하는 복잡한 쿼리를 재사용 가능한 단위로 만들어 호출합니다.

### 기본 문법

```sql
-- 저장 프로시저 생성
DELIMITER //
CREATE PROCEDURE GetEmployeeByDept(IN dept_id INT)
BEGIN
    SELECT employee_id, name, salary
    FROM employee
    WHERE department_id = dept_id
    ORDER BY salary DESC;
END //
DELIMITER ;

-- 저장 프로시저 호출
CALL GetEmployeeByDept(10);
```

### 매개변수 유형

```sql
CREATE PROCEDURE TransferMoney(
    IN from_account INT,     -- 입력 매개변수
    IN to_account INT,
    IN amount DECIMAL(10,2),
    OUT result VARCHAR(50)   -- 출력 매개변수
)
BEGIN
    DECLARE from_balance DECIMAL(10,2);

    SELECT balance INTO from_balance
    FROM accounts WHERE id = from_account;

    IF from_balance >= amount THEN
        UPDATE accounts SET balance = balance - amount WHERE id = from_account;
        UPDATE accounts SET balance = balance + amount WHERE id = to_account;
        SET result = 'SUCCESS';
    ELSE
        SET result = 'INSUFFICIENT_BALANCE';
    END IF;
END;
```

### 장점

**1. 성능 향상**
- 최초 실행 시 컴파일되어 캐시에 저장되므로, 이후 호출 시 컴파일 과정 없이 빠르게 실행됩니다
- 여러 SQL을 하나의 호출로 처리하여 네트워크 트래픽이 감소합니다

**2. 보안 강화**
- 테이블에 직접 접근하지 않고 프로시저를 통해서만 데이터에 접근하도록 제어할 수 있습니다
- SQL Injection 공격을 방지하는 데 도움이 됩니다

**3. 유지보수 편의성**
- 비즈니스 로직 변경 시 애플리케이션을 재배포하지 않고 프로시저만 수정하면 됩니다

### 단점

**1. 디버깅 어려움**
- IDE에서 제공하는 디버깅 도구가 부족합니다
- 오류 추적이 어렵습니다

**2. 데이터베이스 종속성**
- DBMS마다 문법이 다르므로 이식성이 낮습니다
- 데이터베이스 변경 시 프로시저를 모두 다시 작성해야 합니다

**3. 버전 관리 어려움**
- Git 등으로 관리하기 어렵습니다
- 애플리케이션 코드와 분리되어 있어 전체 로직 파악이 어렵습니다

**4. 서버 부하 집중**
- 비즈니스 로직이 DB 서버에 집중되어 스케일아웃이 어렵습니다

### 저장 프로시저 vs 저장 함수

| 구분 | 프로시저 | 함수 |
|------|---------|------|
| 반환값 | 없거나 OUT 파라미터 | 반드시 하나의 값 반환 |
| SQL에서 호출 | CALL로 호출 | SELECT에서 사용 가능 |
| 트랜잭션 | 사용 가능 | 제한적 |

### 면접 팁
최근에는 JPA 등 ORM의 사용이 보편화되면서 저장 프로시저 사용이 줄어드는 추세입니다. 그러나 대량 데이터 처리나 레거시 시스템에서는 여전히 사용됩니다. 장단점을 균형 있게 설명하세요.

## 꼬리 질문
1. 저장 프로시저와 ORM 중 어떤 상황에서 어떤 것을 선택하나요?
2. 트리거(Trigger)란 무엇이며, 저장 프로시저와의 차이점은 무엇인가요?
3. 저장 프로시저의 성능을 최적화하는 방법은 무엇인가요?
