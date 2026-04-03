---
title: "키(Key) 정리"
category: database
difficulty: BASIC
tags: "기본키, 외래키, 후보키, 슈퍼키, 대체키"
---

## 질문
데이터베이스에서 사용되는 키(Key)의 종류와 각각의 역할에 대해 설명해주세요.

## 핵심 키워드
- 슈퍼키 (Super Key)
- 후보키 (Candidate Key)
- 기본키 (Primary Key)
- 외래키 (Foreign Key)
- 대체키 (Alternate Key)

## 답변
데이터베이스에서 키(Key)는 테이블의 각 행(튜플)을 유일하게 식별하거나, 테이블 간의 관계를 설정하기 위해 사용되는 속성 또는 속성의 집합입니다.

### 키의 종류

**1. 슈퍼키 (Super Key)**
테이블에서 튜플을 유일하게 식별할 수 있는 속성의 집합입니다. 유일성은 만족하지만 최소성은 만족하지 않을 수 있습니다. 예를 들어, 학생 테이블에서 (학번, 이름) 조합은 슈퍼키가 될 수 있습니다.

**2. 후보키 (Candidate Key)**
슈퍼키 중에서 최소성을 만족하는 키입니다. 즉, 튜플을 유일하게 식별하는 데 필요한 최소한의 속성으로 구성됩니다. 하나의 테이블에 여러 후보키가 존재할 수 있습니다.

**3. 기본키 (Primary Key)**
후보키 중에서 테이블의 대표 키로 선택된 키입니다. 기본키의 특징은 다음과 같습니다:
- NULL 값을 허용하지 않습니다
- 중복 값을 허용하지 않습니다
- 테이블당 하나만 존재합니다

```sql
CREATE TABLE student (
    student_id INT PRIMARY KEY,  -- 기본키
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE     -- 대체키가 될 수 있음
);
```

**4. 대체키 (Alternate Key)**
후보키 중에서 기본키로 선택되지 않은 나머지 키입니다. 위 예시에서 email이 후보키였다면, 기본키로 student_id를 선택한 후 email은 대체키가 됩니다.

**5. 외래키 (Foreign Key)**
다른 테이블의 기본키를 참조하는 속성으로, 테이블 간의 관계를 설정합니다. 참조 무결성을 유지하는 데 핵심적인 역할을 합니다.

```sql
CREATE TABLE enrollment (
    enrollment_id INT PRIMARY KEY,
    student_id INT,
    course_id INT,
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);
```

**6. 복합키 (Composite Key)**
두 개 이상의 속성을 조합하여 만든 키입니다. 단일 속성만으로는 유일성을 보장할 수 없을 때 사용합니다.

### 키 선택 시 고려사항
- **유일성**: 모든 튜플을 구별할 수 있어야 합니다
- **최소성**: 꼭 필요한 속성만으로 구성해야 합니다
- **불변성**: 자주 변경되지 않는 속성을 선택해야 합니다
- **NOT NULL**: NULL 값이 없어야 합니다

### 면접 팁
키의 계층 구조를 명확히 이해하세요: 슈퍼키 ⊃ 후보키 ⊃ 기본키. 실무에서는 자연키(Natural Key)보다 인조키(Surrogate Key, Auto Increment)를 기본키로 사용하는 경우가 많은데, 그 이유도 함께 설명할 수 있으면 좋습니다.

## 꼬리 질문
1. 자연키와 인조키(대리키)의 차이점과 각각의 장단점은 무엇인가요?
2. 복합키를 기본키로 사용할 때의 장단점은 무엇인가요?
3. 외래키 제약조건에서 ON DELETE CASCADE와 ON DELETE SET NULL의 차이는 무엇인가요?
