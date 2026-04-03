---
title: "팩토리 메소드 패턴"
category: spring
difficulty: INTERMEDIATE
tags: "팩토리 메소드, 디자인 패턴, 생성 패턴, 다형성, 추상 팩토리"
---

## 질문
팩토리 메소드 패턴이란 무엇이며, 어떤 상황에서 사용하나요?

## 핵심 키워드
- 객체 생성을 서브클래스에 위임
- 인터페이스 기반 생성
- 느슨한 결합
- 확장에 열려 있고 변경에 닫혀 있음 (OCP)
- 추상 팩토리와의 차이

## 답변
팩토리 메소드 패턴(Factory Method Pattern)은 객체 생성을 직접 하지 않고, 서브클래스에 위임하는 생성 패턴입니다. 어떤 클래스의 인스턴스를 만들지를 서브클래스에서 결정합니다.

### 구현 예시

```java
// 1. 제품 인터페이스
public interface Notification {
    void send(String message);
}

// 2. 구체적 제품
public class EmailNotification implements Notification {
    public void send(String message) {
        System.out.println("이메일 발송: " + message);
    }
}

public class SmsNotification implements Notification {
    public void send(String message) {
        System.out.println("SMS 발송: " + message);
    }
}

public class PushNotification implements Notification {
    public void send(String message) {
        System.out.println("푸시 알림: " + message);
    }
}

// 3. 팩토리
public class NotificationFactory {
    public static Notification createNotification(String type) {
        return switch (type) {
            case "EMAIL" -> new EmailNotification();
            case "SMS" -> new SmsNotification();
            case "PUSH" -> new PushNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}

// 4. 사용
Notification notification = NotificationFactory.createNotification("EMAIL");
notification.send("안녕하세요");
```

### Spring에서의 활용

```java
// Spring의 BeanFactory가 대표적인 팩토리 패턴
ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
UserService userService = context.getBean(UserService.class);

// 전략 패턴과 결합한 활용
@Component
public class NotificationFactory {
    private final Map<String, Notification> notificationMap;

    public NotificationFactory(List<Notification> notifications) {
        notificationMap = notifications.stream()
            .collect(Collectors.toMap(
                n -> n.getType(),
                Function.identity()
            ));
    }

    public Notification getNotification(String type) {
        return notificationMap.get(type);
    }
}
```

### 팩토리 메소드 vs 추상 팩토리

| 구분 | 팩토리 메소드 | 추상 팩토리 |
|------|-------------|------------|
| 생성 대상 | 하나의 제품 | 관련된 제품군 |
| 확장 방식 | 서브클래스로 확장 | 팩토리 인터페이스로 확장 |
| 복잡도 | 단순 | 복잡 |

### 장점
- 객체 생성과 사용을 분리 (느슨한 결합)
- OCP(개방-폐쇄 원칙) 준수: 새로운 타입 추가 시 팩토리만 수정
- 코드 중복 제거

### 면접 팁
팩토리 패턴은 Spring의 BeanFactory, ApplicationContext에서 핵심적으로 사용됩니다. 단순 팩토리, 팩토리 메소드, 추상 팩토리의 차이를 구분하여 설명할 수 있으면 좋습니다.

## 꼬리 질문
1. 팩토리 패턴과 빌더 패턴의 차이는?
2. Spring의 BeanFactory와 ApplicationContext의 차이는?
3. 추상 팩토리 패턴의 사용 사례는?
