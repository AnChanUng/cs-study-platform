---
title: "TDD (Test-Driven Development)"
category: software-engineering
difficulty: INTERMEDIATE
tags: "TDD, 테스트 주도 개발, Red-Green-Refactor, 단위 테스트, JUnit"
---

## 질문
TDD(테스트 주도 개발)란 무엇이며, 장단점에 대해 설명해주세요.

## 핵심 키워드
- Red-Green-Refactor 사이클
- 단위 테스트
- 테스트 먼저 작성
- 리팩토링
- 코드 품질

## 답변
TDD(Test-Driven Development)는 테스트 코드를 먼저 작성한 후 그 테스트를 통과하는 구현 코드를 작성하는 개발 방법론입니다.

### Red-Green-Refactor 사이클

```
1. Red   : 실패하는 테스트 작성
2. Green : 테스트를 통과하는 최소한의 코드 작성
3. Refactor : 코드를 깔끔하게 리팩토링 (테스트는 계속 통과)
→ 반복
```

### TDD 예시: 계산기 구현

```java
// Step 1: Red - 실패하는 테스트 작성
@Test
void 두_수를_더한다() {
    Calculator calc = new Calculator();
    assertEquals(5, calc.add(2, 3));
}
// → 컴파일 에러 (Calculator 클래스 없음)

// Step 2: Green - 최소한의 구현
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
// → 테스트 통과

// Step 3: Refactor - 필요한 경우 리팩토링
// (이 경우 리팩토링할 부분 없음)

// Step 4: 다음 테스트
@Test
void 음수를_더한다() {
    Calculator calc = new Calculator();
    assertEquals(-1, calc.add(2, -3));
}
```

### TDD의 장점

1. **높은 코드 품질**: 테스트가 코드의 안전망 역할
2. **설계 개선**: 테스트하기 쉬운 코드 = 잘 설계된 코드 (느슨한 결합, 높은 응집)
3. **리팩토링 안정성**: 테스트가 있으므로 안심하고 리팩토링 가능
4. **문서화 효과**: 테스트 코드가 사용 방법의 문서 역할
5. **디버깅 시간 감소**: 버그를 조기에 발견

### TDD의 단점

1. **초기 개발 속도 저하**: 테스트 작성에 추가 시간 소요
2. **학습 곡선**: 테스트 작성 기술과 마인드셋 전환 필요
3. **설계 변경 시 테스트도 수정**: 변경 비용 증가
4. **모든 상황에 적합하지 않음**: UI, 외부 시스템 연동 등

### 테스트 작성 원칙: F.I.R.S.T

- **Fast**: 빠르게 실행되어야 함
- **Independent**: 테스트 간 독립적이어야 함
- **Repeatable**: 어떤 환경에서도 동일한 결과
- **Self-validating**: 자동으로 성공/실패 판단
- **Timely**: 적시에 작성 (구현 전에)

### 면접 팁
TDD의 핵심은 "테스트를 먼저 작성하는 것"이 아니라 "짧은 사이클로 피드백을 받으며 점진적으로 개발하는 것"입니다. 실무에서 100% TDD가 어려운 이유와 함께, 어떤 부분에 적용하면 효과적인지 설명하세요.

## 꼬리 질문
1. 단위 테스트, 통합 테스트, E2E 테스트의 차이는?
2. Mock 객체란 무엇이며, 언제 사용하나요?
3. 테스트 커버리지가 높으면 코드 품질이 보장되나요?
