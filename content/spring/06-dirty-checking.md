---
title: "더티 체킹(Dirty Checking)"
category: spring
difficulty: INTERMEDIATE
tags: "더티 체킹, 변경 감지, JPA, 영속성 컨텍스트, 스냅샷"
---

## 질문
JPA의 더티 체킹(Dirty Checking)이란 무엇이며, 어떻게 동작하나요?

## 핵심 키워드
- 변경 감지
- 스냅샷 (Snapshot)
- 영속성 컨텍스트
- flush
- @Transactional

## 답변
더티 체킹(Dirty Checking)은 JPA가 영속 상태의 엔티티 변경 사항을 자동으로 감지하여 데이터베이스에 반영하는 메커니즘입니다. 개발자가 명시적으로 UPDATE 쿼리를 작성하지 않아도 됩니다.

### 동작 원리

```
1. 엔티티를 영속성 컨텍스트에 처음 저장할 때 스냅샷을 생성
2. 트랜잭션 커밋(또는 flush) 시점에
3. 현재 엔티티 상태와 스냅샷을 비교
4. 변경된 필드가 있으면 UPDATE SQL을 생성하여 실행
```

### 코드 예시

```java
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateUserName(Long userId, String newName) {
        // 1. 영속 상태의 엔티티 조회 (스냅샷 저장)
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. 엔티티의 값 변경
        user.setName(newName);

        // 3. 별도의 save() 호출 없이 트랜잭션 커밋 시 자동 UPDATE
        // userRepository.save(user); ← 불필요!
    }
}

// 실행되는 SQL:
// UPDATE users SET name = '새이름' WHERE id = 1
```

### 더티 체킹이 동작하는 조건

1. **엔티티가 영속 상태**여야 합니다 (영속성 컨텍스트에 의해 관리)
2. **트랜잭션 내**에서 변경이 이루어져야 합니다
3. **flush** 시점에 비교가 수행됩니다

```java
// 더티 체킹이 동작하지 않는 경우
public void updateFail(Long userId, String newName) {
    User user = userRepository.findById(userId).orElseThrow();
    user.setName(newName);
    // @Transactional이 없으면 변경 감지가 동작하지 않음!
}

// 준영속 상태에서도 동작하지 않음
User user = userRepository.findById(1L).orElseThrow();
entityManager.detach(user); // 준영속 상태로 변경
user.setName("새이름"); // 변경 감지 안 됨
```

### 전체 필드 업데이트 vs 변경된 필드만 업데이트

기본적으로 JPA는 모든 필드를 포함하는 UPDATE SQL을 생성합니다.

```java
// 기본: 모든 필드 업데이트
UPDATE users SET name=?, email=?, age=? WHERE id=?

// @DynamicUpdate: 변경된 필드만 업데이트
@Entity
@DynamicUpdate
public class User {
    // ...
}
// UPDATE users SET name=? WHERE id=?  (name만 변경됨)
```

### 주의사항

**1. 대량 업데이트에서는 비효율적**
```java
// 비효율: 1000건을 개별 UPDATE
List<User> users = userRepository.findAll();
users.forEach(u -> u.setStatus("ACTIVE"));

// 효율: 벌크 연산 사용
@Modifying
@Query("UPDATE User u SET u.status = :status")
void updateAllStatus(@Param("status") String status);
```

**2. 벌크 연산 후 영속성 컨텍스트 동기화**
```java
@Modifying(clearAutomatically = true) // 벌크 연산 후 영속성 컨텍스트 초기화
@Query("UPDATE User u SET u.status = 'INACTIVE' WHERE u.lastLogin < :date")
void deactivateInactiveUsers(@Param("date") LocalDateTime date);
```

### 면접 팁
더티 체킹은 JPA의 "영속성 컨텍스트" 개념의 핵심입니다. 동작 조건(@Transactional, 영속 상태)과 주의사항(벌크 연산, @DynamicUpdate)을 함께 설명하세요. 실무에서는 변경 메서드를 엔티티 내부에 정의하는 것이 권장됩니다.

## 꼬리 질문
1. @DynamicUpdate를 항상 사용하지 않는 이유는?
2. 벌크 연산과 더티 체킹의 차이점은?
3. flush와 commit의 차이는 무엇인가요?
