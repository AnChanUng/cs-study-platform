---
title: "Composition(조합)"
category: java
difficulty: INTERMEDIATE
tags: "조합, 상속, has-a, is-a, 위임, 느슨한 결합"
---

## 질문
상속(Inheritance)보다 조합(Composition)을 선호하는 이유에 대해 설명해주세요.

## 핵심 키워드
- is-a vs has-a 관계
- 상속의 문제점
- 조합 + 위임
- 느슨한 결합
- 인터페이스 기반 설계

## 답변
"상속보다 조합을 선호하라(Favor composition over inheritance)"는 GoF 디자인 패턴에서 제시한 핵심 원칙입니다. 코드 재사용을 위해 상속을 사용하는 것보다 조합(has-a 관계)을 통한 위임이 더 유연하고 안전합니다.

### 상속의 문제점

**1. 캡슐화 깨짐**
```java
// HashSet을 상속하여 추가된 원소 수를 세는 클래스
public class InstrumentedHashSet<E> extends HashSet<E> {
    private int addCount = 0;

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c); // 내부에서 add()를 호출!
    }
}

InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
s.addAll(Arrays.asList("A", "B", "C"));
// 기대: addCount = 3
// 실제: addCount = 6 (addAll이 내부에서 add를 호출하므로 중복 카운트)
```

**2. 상위 클래스 변경의 영향**
상위 클래스에 새 메서드가 추가되면 하위 클래스에 영향을 줄 수 있습니다.

**3. 단일 상속 제한**
Java는 다중 상속을 지원하지 않으므로, 한 번 상속하면 다른 클래스를 상속할 수 없습니다.

### 조합으로 해결

```java
// 조합 + 위임 방식
public class InstrumentedSet<E> implements Set<E> {
    private final Set<E> set; // has-a 관계 (조합)
    private int addCount = 0;

    public InstrumentedSet(Set<E> set) {
        this.set = set; // 어떤 Set 구현체든 주입 가능
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return set.add(e); // 위임
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return set.addAll(c); // 위임 (내부 구현에 의존하지 않음)
    }

    // 나머지 Set 메서드들도 위임
    @Override public int size() { return set.size(); }
    @Override public boolean contains(Object o) { return set.contains(o); }
    // ...
}

// 사용: 어떤 Set 구현체든 감쌀 수 있음
Set<String> s = new InstrumentedSet<>(new HashSet<>());
Set<String> s2 = new InstrumentedSet<>(new TreeSet<>());
```

### 상속 vs 조합

| 구분 | 상속 (is-a) | 조합 (has-a) |
|------|-----------|-------------|
| 관계 | "~이다" | "~을 가진다" |
| 결합도 | 강한 결합 | 느슨한 결합 |
| 유연성 | 컴파일 타임 결정 | 런타임 변경 가능 |
| 다중 사용 | 단일 상속 제한 | 여러 객체 조합 가능 |
| 캡슐화 | 깨질 수 있음 | 유지됨 |

### 상속이 적합한 경우
- 진정한 "is-a" 관계일 때 (Dog is an Animal)
- 상위 클래스가 확장을 위해 설계되었을 때 (추상 클래스)
- 상위 클래스와 하위 클래스를 같은 팀이 관리할 때

### Spring에서의 조합

```java
@Service
public class OrderService {
    private final OrderRepository orderRepository; // 조합
    private final PaymentService paymentService;   // 조합
    private final NotificationService notificationService; // 조합

    // 생성자 주입 = 조합 패턴
    public OrderService(OrderRepository orderRepository,
                       PaymentService paymentService,
                       NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }
}
```

### 면접 팁
Effective Java의 "상속보다 조합을 사용하라" 항목의 핵심 논거를 이해하세요. HashSet 예시는 대표적인 상속의 문제를 보여줍니다. Spring의 DI 자체가 조합 패턴의 구현이라는 점도 연결하면 좋습니다.

## 꼬리 질문
1. 데코레이터 패턴과 조합의 관계는?
2. 인터페이스 기본 메서드(default method)가 상속 문제를 해결하나요?
3. 실무에서 상속을 사용하는 적절한 사례는?
