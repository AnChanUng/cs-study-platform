---
title: "Bean Scope"
category: spring
difficulty: BASIC
tags: "Spring Bean, 스코프, 싱글톤, 프로토타입, 라이프사이클"
---

## 질문
Spring Bean의 스코프(Scope)에 대해 설명해주세요.

## 핵심 키워드
- Singleton Scope
- Prototype Scope
- Request/Session/Application Scope
- Bean 라이프사이클
- @Scope 어노테이션

## 답변
Spring Bean의 스코프는 Bean이 존재할 수 있는 범위(생성되고 소멸되는 범위)를 의미합니다. Spring은 기본적으로 싱글톤 스코프를 사용합니다.

### Bean 스코프 종류

**1. Singleton (기본값)**
Spring 컨테이너에서 단 하나의 인스턴스만 생성됩니다. 모든 요청에 같은 객체를 반환합니다.

```java
@Component
// @Scope("singleton") - 기본값이므로 생략 가능
public class SingletonBean {
    // 컨테이너 전체에서 하나의 인스턴스
}
```

**2. Prototype**
요청할 때마다 새로운 인스턴스를 생성합니다. Spring 컨테이너는 생성과 의존성 주입까지만 관리하고, 이후의 생명주기는 관리하지 않습니다.

```java
@Component
@Scope("prototype")
public class PrototypeBean {
    // 요청할 때마다 새 인스턴스
}
```

**3. Web 스코프 (웹 환경에서만)**

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestBean {
    // HTTP 요청마다 새 인스턴스
}

@Scope("session")    // HTTP 세션마다 하나
@Scope("application") // 서블릿 컨텍스트마다 하나
```

### Singleton에서 Prototype 주입 문제

싱글톤 Bean에 프로토타입 Bean을 주입하면, 프로토타입이 의도대로 동작하지 않습니다.

```java
@Component
public class SingletonBean {
    @Autowired
    private PrototypeBean prototypeBean; // 항상 같은 인스턴스!

    // 해결 방법 1: ObjectProvider
    @Autowired
    private ObjectProvider<PrototypeBean> prototypeBeanProvider;

    public void logic() {
        PrototypeBean bean = prototypeBeanProvider.getObject(); // 매번 새 인스턴스
    }

    // 해결 방법 2: @Lookup
    @Lookup
    public PrototypeBean getPrototypeBean() { return null; }
}
```

### Bean 라이프사이클

```
1. 스프링 컨테이너 생성
2. 스프링 빈 생성 (생성자 호출)
3. 의존성 주입 (@Autowired)
4. 초기화 콜백 (@PostConstruct)
5. 사용
6. 소멸 콜백 (@PreDestroy)
7. 스프링 컨테이너 종료
```

```java
@Component
public class MyBean {
    @PostConstruct
    public void init() {
        // 초기화 로직 (DB 연결, 파일 로드 등)
    }

    @PreDestroy
    public void destroy() {
        // 정리 로직 (연결 해제, 리소스 반환 등)
    }
}
```

### 면접 팁
대부분의 Bean은 싱글톤으로 사용합니다. 싱글톤 Bean에서는 상태를 가지면 안 되며(Stateless), 공유 변수를 사용하면 동시성 문제가 발생합니다. 프로토타입 스코프는 잘 사용되지 않지만, 싱글톤과의 혼용 시 발생하는 문제는 자주 출제됩니다.

## 꼬리 질문
1. 싱글톤 Bean에서 상태를 가지면 왜 문제가 되나요?
2. @PostConstruct와 @PreDestroy의 사용 사례는?
3. 싱글톤 패턴과 Spring 싱글톤의 차이는?
