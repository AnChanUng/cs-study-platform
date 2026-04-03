---
title: "Error & Exception"
category: java
difficulty: INTERMEDIATE
tags: "Error, Exception, Checked, Unchecked, 예외 처리"
---

## 질문
Java의 Error와 Exception의 차이, Checked Exception과 Unchecked Exception의 차이에 대해 설명해주세요.

## 핵심 키워드
- Throwable
- Error vs Exception
- Checked vs Unchecked Exception
- try-catch-finally
- 커스텀 예외

## 답변

### 예외 계층 구조

```
Throwable
├── Error (시스템 레벨, 복구 불가)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── ...
└── Exception
    ├── Checked Exception (컴파일 시 확인)
    │   ├── IOException
    │   ├── SQLException
    │   └── ...
    └── RuntimeException (Unchecked Exception)
        ├── NullPointerException
        ├── ArrayIndexOutOfBoundsException
        ├── IllegalArgumentException
        └── ...
```

### Error vs Exception

| 구분 | Error | Exception |
|------|-------|-----------|
| 원인 | JVM 시스템 레벨 문제 | 애플리케이션 로직 문제 |
| 복구 | 불가 | 가능 |
| 처리 | 처리하면 안 됨 | 적절히 처리해야 함 |
| 예시 | OOM, SOF | NPE, IOException |

### Checked vs Unchecked Exception

| 구분 | Checked | Unchecked |
|------|---------|-----------|
| 확인 시점 | 컴파일 타임 | 런타임 |
| 처리 의무 | 반드시 처리 (try-catch or throws) | 선택적 |
| 상위 클래스 | Exception | RuntimeException |
| 예시 | IOException, SQLException | NPE, IAE |

```java
// Checked Exception: 반드시 처리해야 함
public void readFile() throws IOException { // throws 선언 필수
    FileReader reader = new FileReader("file.txt");
}

// Unchecked Exception: 처리 선택
public void divide(int a, int b) {
    int result = a / b; // ArithmeticException 발생 가능하지만 선언 불필요
}
```

### 예외 처리 방법

```java
// try-catch-finally
try {
    riskyOperation();
} catch (SpecificException e) {
    // 구체적 예외 처리
    log.error("Error: {}", e.getMessage());
} catch (Exception e) {
    // 일반 예외 처리
} finally {
    // 항상 실행 (자원 해제)
}

// try-with-resources (Java 7+)
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line = br.readLine();
} // AutoCloseable 구현 객체 자동 close()
```

### 커스텀 예외

```java
// Unchecked 커스텀 예외 (권장)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
    }
}

// 사용
public User findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
}
```

### 예외 처리 Best Practice

1. **구체적인 예외를 잡기**: catch(Exception e)보다 구체적인 예외
2. **예외 삼키지 않기**: 빈 catch 블록 금지
3. **의미 있는 메시지 포함**: 디버깅에 도움되는 정보
4. **Checked보다 Unchecked 선호**: 불필요한 throws 전파 방지
5. **예외를 제어 흐름에 사용하지 않기**: 예외는 예외적 상황에만

### 면접 팁
Checked와 Unchecked의 차이를 확실히 이해하고, 실무에서 커스텀 예외를 RuntimeException으로 만드는 이유(Checked의 불편함)를 설명할 수 있어야 합니다. Spring의 @ExceptionHandler를 이용한 전역 예외 처리도 알아두세요.

## 꼬리 질문
1. Spring에서 전역 예외 처리를 하는 방법은?
2. Checked Exception을 Unchecked로 감싸는 이유는?
3. try-with-resources가 필요한 이유는?
