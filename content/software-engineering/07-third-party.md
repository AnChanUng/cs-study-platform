---
title: "서드 파티(Third Party)"
category: software-engineering
difficulty: BASIC
tags: "서드 파티, 라이브러리, API, SDK, 의존성"
---

## 질문
서드 파티(Third Party)란 무엇이며, 사용 시 고려사항은 무엇인가요?

## 핵심 키워드
- 서드 파티 라이브러리/API
- 의존성 관리
- 라이선스
- 보안 취약점
- 벤더 락인

## 답변
서드 파티(Third Party)란 프로그래밍에서 제3자, 즉 개발자나 플랫폼 제공자가 아닌 외부 업체나 개인이 제공하는 소프트웨어 구성요소를 말합니다.

### 서드 파티의 종류

**1. 서드 파티 라이브러리**
특정 기능을 제공하는 코드 모음입니다.
```gradle
// Java/Gradle 예시
dependencies {
    implementation 'com.google.guava:guava:31.1-jre'
    implementation 'org.apache.commons:commons-lang3:3.12.0'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.14.0'
}
```

**2. 서드 파티 API**
외부 서비스의 기능을 호출할 수 있는 인터페이스입니다.
- 결제 API (Stripe, Toss Payments)
- 지도 API (Google Maps, Kakao Maps)
- 소셜 로그인 (Google, Kakao, Naver)

**3. 서드 파티 SDK**
특정 플랫폼이나 서비스와 연동하기 위한 개발 도구 모음입니다.
- Firebase SDK, AWS SDK

### 서드 파티 사용의 장점
- **개발 시간 단축**: 이미 검증된 기능을 즉시 사용
- **높은 품질**: 전문 팀이 유지보수하는 코드
- **커뮤니티 지원**: 문서, 예제, 질의응답
- **비용 절감**: 직접 개발하는 것보다 경제적

### 서드 파티 사용 시 고려사항

**1. 라이선스 확인**
- MIT, Apache 2.0: 상업적 사용 가능
- GPL: 소스 코드 공개 의무 (주의!)
- 라이선스 위반 시 법적 문제 발생 가능

**2. 보안 취약점**
```
Log4j 취약점(2021년)처럼 서드 파티의 보안 문제가
사용하는 모든 시스템에 영향을 줄 수 있음
→ 정기적인 의존성 업데이트와 취약점 스캔 필요
```

**3. 벤더 종속성 (Vendor Lock-in)**
특정 서드 파티에 과도하게 의존하면 변경이 어려워집니다.
→ 추상화 계층을 두어 교체 가능하게 설계

```java
// Bad: 직접 의존
PayPalClient client = new PayPalClient();
client.pay(amount);

// Good: 추상화를 통한 의존
interface PaymentGateway { void pay(int amount); }
class PayPalGateway implements PaymentGateway { ... }
class StripeGateway implements PaymentGateway { ... }
```

**4. 유지보수 상태**
- 마지막 업데이트 시점
- 이슈 처리 속도
- 커뮤니티 활성도
- 메이저 버전 업데이트 시 호환성

### 의존성 관리
```gradle
// 버전 관리
implementation 'org.springframework.boot:spring-boot-starter:3.0.0'

// 의존성 충돌 확인
./gradlew dependencies
```

### 면접 팁
서드 파티를 "무조건 사용해야 한다" 또는 "사용하면 안 된다"가 아니라, 트레이드오프를 고려하여 결정한다는 관점이 중요합니다. 보안, 라이선스, 유지보수 측면을 고려한 의사결정 과정을 설명하세요.

## 꼬리 질문
1. GPL과 MIT 라이선스의 차이점은 무엇인가요?
2. 서드 파티 라이브러리의 보안 취약점을 관리하는 방법은?
3. 서드 파티에 대한 의존성을 줄이는 설계 패턴은?
