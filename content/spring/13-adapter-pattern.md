---
title: "어댑터 패턴(Adapter Pattern)"
category: spring
difficulty: INTERMEDIATE
tags: "어댑터 패턴, 디자인 패턴, 구조 패턴, 인터페이스 변환, 래퍼"
---

## 질문
어댑터 패턴이란 무엇이며, 실무에서 어떻게 활용되나요?

## 핵심 키워드
- 인터페이스 호환성
- 기존 코드 재사용
- 클래스 어댑터 / 객체 어댑터
- 래퍼 (Wrapper)
- Spring HandlerAdapter

## 답변
어댑터 패턴(Adapter Pattern)은 호환되지 않는 인터페이스를 가진 클래스들을 함께 동작할 수 있도록 변환해주는 구조 패턴입니다. 콘센트 어댑터(110V → 220V 변환)와 같은 역할을 합니다.

### 구현 예시

```java
// 기존 시스템: XML 형식으로 데이터 제공
public class LegacyXmlService {
    public String getXmlData() {
        return "<user><name>홍길동</name></user>";
    }
}

// 새 시스템이 요구하는 인터페이스
public interface JsonDataProvider {
    String getJsonData();
}

// 어댑터: XML → JSON 변환
public class XmlToJsonAdapter implements JsonDataProvider {
    private final LegacyXmlService legacyService;

    public XmlToJsonAdapter(LegacyXmlService legacyService) {
        this.legacyService = legacyService;
    }

    @Override
    public String getJsonData() {
        String xml = legacyService.getXmlData();
        // XML을 JSON으로 변환하는 로직
        return convertXmlToJson(xml);
    }

    private String convertXmlToJson(String xml) {
        // 변환 로직
        return "{\"name\": \"홍길동\"}";
    }
}

// 사용
JsonDataProvider provider = new XmlToJsonAdapter(new LegacyXmlService());
String json = provider.getJsonData();
```

### 실무 활용: 외부 라이브러리 래핑

```java
// 외부 결제 라이브러리 (우리가 수정할 수 없음)
public class ExternalPaymentSDK {
    public PaymentResult charge(String cardNumber, double amount) { ... }
}

// 우리 시스템의 인터페이스
public interface PaymentGateway {
    PaymentResponse pay(PaymentRequest request);
}

// 어댑터
@Component
public class ExternalPaymentAdapter implements PaymentGateway {
    private final ExternalPaymentSDK sdk;

    public ExternalPaymentAdapter(ExternalPaymentSDK sdk) {
        this.sdk = sdk;
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        PaymentResult result = sdk.charge(
            request.getCardNumber(),
            request.getAmount()
        );
        return new PaymentResponse(result.isSuccess(), result.getTransactionId());
    }
}
```

### Spring에서의 어댑터 패턴

**HandlerAdapter**: 다양한 형태의 Controller를 통일된 방식으로 실행

```java
// Spring MVC의 HandlerAdapter
public interface HandlerAdapter {
    boolean supports(Object handler);
    ModelAndView handle(HttpServletRequest request,
                       HttpServletResponse response,
                       Object handler);
}

// 다양한 어댑터 구현체
// - RequestMappingHandlerAdapter (@RequestMapping)
// - HttpRequestHandlerAdapter
// - SimpleControllerHandlerAdapter
```

### 클래스 어댑터 vs 객체 어댑터

| 구분 | 클래스 어댑터 | 객체 어댑터 |
|------|-------------|------------|
| 방식 | 상속 | 조합(위임) |
| 유연성 | 낮음 | 높음 |
| 다중 적용 | 불가 (단일 상속) | 가능 |
| 권장 | X | O |

### 장점
- 기존 코드 수정 없이 새 인터페이스에 적응
- 외부 라이브러리와의 결합도 감소
- 테스트 용이 (어댑터를 Mock으로 교체)

### 면접 팁
어댑터 패턴은 레거시 시스템 연동, 외부 라이브러리 래핑 등 실무에서 매우 자주 사용됩니다. Spring MVC의 HandlerAdapter와 연결하여 설명하면 좋습니다.

## 꼬리 질문
1. 어댑터 패턴과 파사드 패턴의 차이는?
2. 어댑터 패턴과 데코레이터 패턴의 차이는?
3. 실무에서 어댑터 패턴을 적용한 사례를 들어주세요.
