---
title: "SQL Injection"
category: database
difficulty: INTERMEDIATE
tags: "SQL Injection, 보안, PreparedStatement, 파라미터 바인딩"
---

## 질문
SQL Injection이란 무엇이며, 어떻게 방어할 수 있나요?

## 핵심 키워드
- SQL Injection 공격
- PreparedStatement
- 입력값 검증
- ORM
- 파라미터 바인딩

## 답변
SQL Injection은 악의적인 사용자가 입력 필드를 통해 SQL 쿼리를 조작하여 데이터베이스를 비정상적으로 조작하는 공격 기법입니다. OWASP Top 10에 항상 포함되는 대표적인 웹 보안 취약점입니다.

### 공격 원리

사용자의 입력값이 SQL 쿼리에 직접 삽입될 때 발생합니다.

```java
// 취약한 코드
String query = "SELECT * FROM users WHERE id = '" + userInput + "'";
```

공격자가 `' OR '1'='1`을 입력하면:
```sql
SELECT * FROM users WHERE id = '' OR '1'='1'
-- 모든 사용자 정보가 노출됨
```

### 공격 유형

**1. 인증 우회**
로그인 폼에서 `admin' --`를 입력하여 비밀번호 검증을 무력화합니다.

**2. 데이터 추출 (Union-based)**
```sql
' UNION SELECT username, password FROM users --
```
UNION을 사용하여 다른 테이블의 데이터를 추출합니다.

**3. Blind SQL Injection**
참/거짓 반응을 통해 데이터를 한 글자씩 추측합니다.
```sql
' AND SUBSTRING(password,1,1)='a' --
```

**4. Time-based Blind SQL Injection**
서버 응답 시간 차이를 이용하여 데이터를 추출합니다.

### 방어 방법

**1. PreparedStatement 사용 (가장 효과적)**
```java
String query = "SELECT * FROM users WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(query);
pstmt.setString(1, userInput);
ResultSet rs = pstmt.executeQuery();
```

**2. ORM 사용**
```java
// JPA 사용 예시
@Query("SELECT u FROM User u WHERE u.id = :userId")
User findByUserId(@Param("userId") String userId);
```

**3. 입력값 검증 및 이스케이프 처리**
- 화이트리스트 방식으로 허용된 문자만 입력 가능하도록 제한
- 특수문자(', ", --, ;) 이스케이프 처리

**4. 최소 권한 원칙**
데이터베이스 접속 계정에 최소한의 권한만 부여합니다.

**5. 웹 방화벽(WAF) 사용**
SQL Injection 패턴을 탐지하고 차단하는 웹 애플리케이션 방화벽을 도입합니다.

### 면접 팁
단순히 "PreparedStatement를 쓰면 됩니다"보다는, 왜 PreparedStatement가 SQL Injection을 방어하는지(쿼리 구조와 데이터를 분리하여 컴파일하기 때문) 원리까지 설명하면 좋습니다.

## 꼬리 질문
1. PreparedStatement가 SQL Injection을 방어하는 내부 원리는 무엇인가요?
2. ORM을 사용하면 SQL Injection으로부터 완전히 안전한가요?
3. Stored Procedure를 사용하면 SQL Injection을 방어할 수 있나요?
