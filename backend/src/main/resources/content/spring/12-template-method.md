---
title: "템플릿 메소드 패턴"
category: spring
difficulty: INTERMEDIATE
tags: "템플릿 메소드, 디자인 패턴, 행위 패턴, 상속, 후크 메소드"
---

## 질문
템플릿 메소드 패턴이란 무엇이며, 어떤 상황에서 사용하나요?

## 핵심 키워드
- 알고리즘 골격 정의
- 일부 단계를 서브클래스에서 구현
- 추상 클래스
- 후크 메소드 (Hook Method)
- 코드 중복 제거

## 답변
템플릿 메소드 패턴(Template Method Pattern)은 알고리즘의 구조(골격)를 상위 클래스에서 정의하고, 일부 단계의 구체적인 구현은 서브클래스에서 제공하도록 하는 패턴입니다.

### 구현 예시: 데이터 처리 파이프라인

```java
// 추상 클래스: 알고리즘 골격 정의
public abstract class DataProcessor {

    // 템플릿 메소드: 알고리즘의 골격 (final로 오버라이드 방지)
    public final void process() {
        readData();
        parseData();
        processData();
        if (shouldSave()) { // 후크 메소드
            saveData();
        }
        logResult();
    }

    // 추상 메소드: 서브클래스에서 반드시 구현
    protected abstract void readData();
    protected abstract void parseData();
    protected abstract void processData();

    // 후크 메소드: 기본 구현 제공, 필요시 오버라이드
    protected boolean shouldSave() {
        return true;
    }

    // 공통 로직
    private void saveData() {
        System.out.println("데이터 저장 완료");
    }

    private void logResult() {
        System.out.println("처리 완료 로그 기록");
    }
}

// 구체적 구현: CSV 처리
public class CsvProcessor extends DataProcessor {
    protected void readData() { System.out.println("CSV 파일 읽기"); }
    protected void parseData() { System.out.println("CSV 파싱"); }
    protected void processData() { System.out.println("CSV 데이터 처리"); }
}

// 구체적 구현: JSON 처리
public class JsonProcessor extends DataProcessor {
    protected void readData() { System.out.println("JSON 파일 읽기"); }
    protected void parseData() { System.out.println("JSON 파싱"); }
    protected void processData() { System.out.println("JSON 데이터 처리"); }

    @Override
    protected boolean shouldSave() {
        return false; // 저장하지 않음
    }
}

// 사용
DataProcessor processor = new CsvProcessor();
processor.process(); // 템플릿 메소드 호출
```

### Spring에서의 활용

```java
// Spring의 JdbcTemplate - 대표적인 템플릿 패턴
jdbcTemplate.query(
    "SELECT * FROM users WHERE id = ?",
    new Object[]{id},
    (rs, rowNum) -> new User(        // 콜백으로 변형된 형태
        rs.getLong("id"),
        rs.getString("name")
    )
);

// AbstractController, HttpServlet의 service() 메소드도 템플릿 패턴
```

### 템플릿 메소드 vs 전략 패턴

| 구분 | 템플릿 메소드 | 전략 패턴 |
|------|-------------|----------|
| 구현 방식 | 상속 | 조합(위임) |
| 변경 단위 | 알고리즘의 일부 | 전체 알고리즘 |
| 결합도 | 높음 (상속) | 낮음 (인터페이스) |
| 유연성 | 컴파일 타임 | 런타임 교체 가능 |

### 장점
- 코드 중복 제거 (공통 로직을 상위 클래스에)
- 알고리즘의 핵심 구조를 한 곳에서 관리
- 확장 포인트가 명확

### 단점
- 상속 기반이므로 결합도가 높음
- 상위 클래스 변경 시 하위 클래스에 영향

### 면접 팁
Spring의 JdbcTemplate, RestTemplate, TransactionTemplate 등 "~Template" 이름의 클래스들이 이 패턴을 사용합니다. 콜백 패턴과 결합된 Spring의 변형도 이해하면 좋습니다.

## 꼬리 질문
1. 후크 메소드(Hook Method)의 역할은 무엇인가요?
2. 템플릿 메소드 패턴의 단점을 보완하는 방법은?
3. Spring의 JdbcTemplate은 어떻게 템플릿 패턴을 활용하나요?
