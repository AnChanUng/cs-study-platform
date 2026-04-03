---
title: "옵저버 패턴(Observer Pattern)"
category: spring
difficulty: INTERMEDIATE
tags: "옵저버 패턴, 이벤트, 발행-구독, ApplicationEvent, 느슨한 결합"
---

## 질문
옵저버 패턴이란 무엇이며, Spring에서는 어떻게 활용되나요?

## 핵심 키워드
- 발행/구독 (Publish/Subscribe)
- 상태 변화 알림
- 느슨한 결합
- ApplicationEvent
- @EventListener

## 답변
옵저버 패턴(Observer Pattern)은 객체의 상태 변화가 발생했을 때, 의존하는 다른 객체들에게 자동으로 알림을 보내는 행위 패턴입니다. 일대다(one-to-many) 의존 관계를 정의합니다.

### 기본 구현

```java
// 옵저버 인터페이스
public interface EventListener {
    void update(String event);
}

// 발행자 (Subject)
public class EventPublisher {
    private List<EventListener> listeners = new ArrayList<>();

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    public void notify(String event) {
        for (EventListener listener : listeners) {
            listener.update(event);
        }
    }
}

// 구체적 옵저버
public class EmailAlert implements EventListener {
    public void update(String event) {
        System.out.println("이메일 알림: " + event);
    }
}

public class SlackAlert implements EventListener {
    public void update(String event) {
        System.out.println("슬랙 알림: " + event);
    }
}
```

### Spring의 이벤트 시스템

Spring은 옵저버 패턴을 ApplicationEvent와 @EventListener로 구현합니다.

```java
// 이벤트 정의
public class OrderCreatedEvent {
    private final Long orderId;
    private final Long userId;

    public OrderCreatedEvent(Long orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
    // getters
}

// 이벤트 발행
@Service
@Transactional
public class OrderService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void createOrder(OrderRequest request) {
        Order order = orderRepository.save(new Order(request));

        // 이벤트 발행 - 주문 서비스는 알림 로직을 모름 (느슨한 결합)
        eventPublisher.publishEvent(new OrderCreatedEvent(order.getId(), request.getUserId()));
    }
}

// 이벤트 리스너 (구독자)
@Component
public class OrderEventHandler {

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        // 이메일 발송
        emailService.sendOrderConfirmation(event.getUserId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotification(OrderCreatedEvent event) {
        // 트랜잭션 커밋 후 실행
        notificationService.sendPush(event.getUserId());
    }

    @Async
    @EventListener
    public void logOrder(OrderCreatedEvent event) {
        // 비동기로 로깅
        logService.logOrderCreation(event.getOrderId());
    }
}
```

### 장점
- **느슨한 결합**: 발행자와 구독자가 서로를 알 필요 없음
- **확장성**: 새로운 구독자를 쉽게 추가 가능
- **단일 책임**: 각 구독자가 자신의 로직에만 집중

### 단점
- 실행 순서 제어가 어려움
- 디버깅이 복잡해질 수 있음
- 메모리 누수 가능 (구독 해제를 잊는 경우)

### 면접 팁
옵저버 패턴은 Spring의 이벤트 시스템, 메시지 큐(Kafka), React의 상태 관리 등 다양한 곳에서 사용됩니다. @TransactionalEventListener를 사용한 트랜잭션과 이벤트의 연동을 설명할 수 있으면 좋습니다.

## 꼬리 질문
1. @EventListener와 @TransactionalEventListener의 차이는?
2. 옵저버 패턴과 발행-구독(Pub/Sub) 패턴의 차이는?
3. 비동기 이벤트 처리 시 주의사항은?
