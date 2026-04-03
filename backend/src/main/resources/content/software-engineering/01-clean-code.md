---
title: "클린코드 & 리팩토링"
category: software-engineering
difficulty: BASIC
tags: "클린코드, 리팩토링, 가독성, 코드 품질, 네이밍"
---

## 질문
클린 코드란 무엇이며, 리팩토링의 목적과 기법에 대해 설명해주세요.

## 핵심 키워드
- 가독성 (Readability)
- 의미 있는 네이밍
- 단일 책임 원칙
- 코드 스멜 (Code Smell)
- 리팩토링 기법

## 답변
클린 코드(Clean Code)는 다른 개발자가 쉽게 이해하고 유지보수할 수 있는 코드를 말합니다. 로버트 마틴(Uncle Bob)의 저서에서 정의한 개념으로, "읽기 쉬운 코드"가 핵심입니다.

### 클린 코드 원칙

**1. 의미 있는 이름**
```java
// Bad
int d; // 경과 시간 (일)

// Good
int elapsedTimeInDays;
```

**2. 함수는 하나의 일만**
```java
// Bad: 여러 일을 하는 함수
void processOrder(Order order) {
    validateOrder(order);
    calculatePrice(order);
    sendEmail(order);
    saveToDatabase(order);
}

// Good: 단일 책임
void validateOrder(Order order) { ... }
void calculatePrice(Order order) { ... }
```

**3. 주석보다 코드로 표현**
```java
// Bad
// 직원에게 복지 혜택을 받을 자격이 있는지 검사
if ((employee.flags & HOURLY_FLAG) && (employee.age > 65))

// Good
if (employee.isEligibleForBenefits())
```

**4. 오류 처리**
- null 반환보다 예외를 사용
- checked exception보다 unchecked exception 선호
- Try-Catch 블록을 별도 함수로 분리

### 리팩토링 (Refactoring)

리팩토링은 외부 동작을 변경하지 않으면서 코드의 내부 구조를 개선하는 것입니다.

**코드 스멜 (리팩토링이 필요한 신호):**
- 중복 코드 (Duplicated Code)
- 긴 메서드 (Long Method)
- 거대한 클래스 (Large Class)
- 긴 매개변수 목록 (Long Parameter List)
- 주석이 많은 코드 (코드가 불명확하다는 신호)

**주요 리팩토링 기법:**
- **메서드 추출 (Extract Method)**: 긴 메서드를 작은 메서드로 분리
- **변수 인라인 (Inline Variable)**: 불필요한 임시 변수 제거
- **조건부 로직 다형성으로 변환**: if-else를 다형성으로 대체
- **매개변수 객체 도입**: 관련 매개변수를 하나의 객체로 묶기

```java
// Before: 매개변수가 많음
void createUser(String name, String email, String phone, String address) { ... }

// After: 매개변수 객체 도입
void createUser(UserCreateRequest request) { ... }
```

### 면접 팁
클린 코드는 "주관적인 좋은 코드"가 아니라 구체적인 원칙이 있습니다. 실무에서 리팩토링한 경험을 예시로 들면 좋습니다. 테스트 코드 없이 리팩토링하면 위험하다는 점도 언급하세요.

## 꼬리 질문
1. 리팩토링 시 테스트 코드가 중요한 이유는?
2. 코드 리뷰에서 주로 어떤 점을 확인하나요?
3. 기술 부채(Technical Debt)란 무엇인가요?
