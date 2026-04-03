---
title: "싱글톤 패턴(Singleton Pattern)"
category: spring
difficulty: BASIC
tags: "싱글톤, 디자인 패턴, 인스턴스 하나, Spring Bean, 스레드 안전"
---

## 질문
싱글톤 패턴이란 무엇이며, 구현 방법과 주의사항에 대해 설명해주세요.

## 핵심 키워드
- 인스턴스 하나만 생성
- private 생성자
- 전역 접근점
- 스레드 안전
- Spring 싱글톤

## 답변
싱글톤 패턴(Singleton Pattern)은 클래스의 인스턴스가 오직 하나만 생성되도록 보장하고, 전역적으로 접근할 수 있는 접근점을 제공하는 디자인 패턴입니다.

### 구현 방법

**1. Eager Initialization (즉시 초기화)**
```java
public class Singleton {
    private static final Singleton INSTANCE = new Singleton();
    private Singleton() {}
    public static Singleton getInstance() { return INSTANCE; }
}
```

**2. Lazy Initialization + synchronized**
```java
public class Singleton {
    private static Singleton instance;
    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
// 단점: 매번 동기화 오버헤드
```

**3. Double-Checked Locking (DCL)**
```java
public class Singleton {
    private static volatile Singleton instance;
    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {             // 1차 체크
            synchronized (Singleton.class) {
                if (instance == null) {     // 2차 체크
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

**4. Bill Pugh 방식 (정적 내부 클래스) - 권장**
```java
public class Singleton {
    private Singleton() {}

    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
// Lazy + Thread-safe + 간결
```

**5. Enum (가장 안전) - 권장**
```java
public enum Singleton {
    INSTANCE;

    public void doSomething() { ... }
}
// 직렬화, 리플렉션 공격에도 안전
```

### 싱글톤의 문제점
- 전역 상태를 만들어 테스트가 어려움
- 의존성을 감추어 코드의 결합도 증가
- 멀티스레드 환경에서 동시성 이슈
- SOLID의 단일 책임 원칙 위반 가능
- 상태를 가지면 동시성 문제 발생

### Spring 싱글톤과의 차이

| 구분 | 싱글톤 패턴 | Spring 싱글톤 |
|------|-----------|--------------|
| 관리 | 클래스 자체 | Spring 컨테이너 |
| 범위 | JVM | 스프링 컨텍스트 |
| 테스트 | 어려움 | DI로 쉬움 |
| 구현 | private 생성자 | 일반 클래스 |

Spring은 싱글톤 패턴의 단점(테스트 어려움, 결합도)을 DI 컨테이너로 해결합니다.

### 면접 팁
구현 방법보다 싱글톤의 문제점과 Spring이 이를 어떻게 해결하는지에 초점을 맞추세요. 실무에서는 직접 싱글톤 패턴을 구현하기보다 Spring Bean을 사용합니다.

## 꼬리 질문
1. DCL에서 volatile 키워드가 필요한 이유는?
2. 리플렉션으로 싱글톤을 깨뜨릴 수 있는데, 방지법은?
3. Spring에서 싱글톤 Bean의 상태 관리를 어떻게 하나요?
