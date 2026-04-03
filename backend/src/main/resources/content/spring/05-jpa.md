---
title: "JPA (Java Persistence API)"
category: spring
difficulty: INTERMEDIATE
tags: "JPA, ORM, 영속성 컨텍스트, 엔티티, Hibernate"
---

## 질문
JPA란 무엇이며, 영속성 컨텍스트에 대해 설명해주세요.

## 핵심 키워드
- ORM (Object-Relational Mapping)
- 영속성 컨텍스트 (Persistence Context)
- 1차 캐시
- 쓰기 지연
- 변경 감지 (Dirty Checking)

## 답변
JPA(Java Persistence API)는 자바 ORM 기술에 대한 표준 명세로, 객체와 관계형 데이터베이스 간의 매핑을 자동화합니다. Hibernate가 대표적인 JPA 구현체입니다.

### 엔티티 매핑

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
}
```

### 영속성 컨텍스트 (Persistence Context)

엔티티를 영구 저장하는 논리적 환경으로, EntityManager를 통해 접근합니다.

**엔티티 상태:**
```
비영속 (new/transient) → 영속 (managed) → 준영속 (detached) → 삭제 (removed)

User user = new User("홍길동");        // 비영속
em.persist(user);                       // 영속
em.detach(user);                        // 준영속
em.remove(user);                        // 삭제
```

### 영속성 컨텍스트의 이점

**1. 1차 캐시**
```java
User user = em.find(User.class, 1L); // DB 조회 후 1차 캐시에 저장
User same = em.find(User.class, 1L); // 1차 캐시에서 반환 (DB 조회 안 함)
// user == same (동일성 보장)
```

**2. 쓰기 지연 (Write-behind)**
```java
em.persist(user1); // SQL 생성만, DB 전송 안 함
em.persist(user2); // SQL 생성만, DB 전송 안 함
tx.commit();       // 이 시점에 SQL 일괄 전송 (flush)
```

**3. 변경 감지 (Dirty Checking)**
```java
User user = em.find(User.class, 1L);
user.setName("김길동"); // 별도의 update 호출 없이
tx.commit();            // flush 시점에 변경 사항 자동 감지 → UPDATE SQL 생성
```

**4. 지연 로딩 (Lazy Loading)**
```java
@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
private List<Order> orders;

User user = em.find(User.class, 1L); // User만 조회
user.getOrders().size();              // 이 시점에 Orders 조회 (프록시)
```

### N+1 문제

```java
List<User> users = userRepository.findAll(); // 1번 쿼리
for (User user : users) {
    user.getOrders().size(); // User마다 1번씩 쿼리 → N번
}
// 총 N+1번 쿼리 실행

// 해결: Fetch Join
@Query("SELECT u FROM User u JOIN FETCH u.orders")
List<User> findAllWithOrders();
```

### 면접 팁
영속성 컨텍스트의 4가지 이점(1차 캐시, 쓰기 지연, 변경 감지, 지연 로딩)을 코드 예시와 함께 설명하세요. N+1 문제의 원인과 해결 방법(Fetch Join, @EntityGraph, BatchSize)은 거의 필수 출제 주제입니다.

## 꼬리 질문
1. N+1 문제를 해결하는 방법들과 각각의 장단점은?
2. 영속성 컨텍스트의 생명주기는 트랜잭션과 어떻게 관련되나요?
3. JPA에서 양방향 연관관계 매핑 시 주의할 점은?
