---
title: "직렬화(Serialization)"
category: java
difficulty: INTERMEDIATE
tags: "직렬화, 역직렬화, Serializable, serialVersionUID, JSON"
---

## 질문
Java의 직렬화(Serialization)란 무엇이며, 어떻게 사용하나요?

## 핵심 키워드
- 직렬화 / 역직렬화
- Serializable 인터페이스
- serialVersionUID
- transient 키워드
- JSON 직렬화

## 답변
직렬화(Serialization)는 객체의 상태를 바이트 스트림으로 변환하는 과정이며, 역직렬화(Deserialization)는 바이트 스트림을 다시 객체로 복원하는 과정입니다.

### Java 직렬화

```java
// Serializable 인터페이스 구현
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private transient String password; // 직렬화에서 제외

    // 생성자, getter, setter
}

// 직렬화 (객체 → 바이트)
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.dat"))) {
    User user = new User("홍길동", 25);
    oos.writeObject(user);
}

// 역직렬화 (바이트 → 객체)
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.dat"))) {
    User user = (User) ois.readObject();
    System.out.println(user.getName()); // "홍길동"
    System.out.println(user.getPassword()); // null (transient)
}
```

### serialVersionUID

클래스의 버전을 관리하는 고유 식별자입니다. 직렬화/역직렬화 시 동일한 클래스인지 확인하는 데 사용됩니다.

```java
private static final long serialVersionUID = 1L;
// 명시하지 않으면 컴파일러가 자동 생성 → 클래스 변경 시 다른 값 생성 → InvalidClassException
```

### transient 키워드

직렬화에서 제외할 필드에 사용합니다.
```java
private transient String password;   // 보안 정보
private transient Connection conn;   // 직렬화 불가능한 객체
```

### JSON 직렬화 (실무에서 주로 사용)

```java
// Jackson
ObjectMapper mapper = new ObjectMapper();

// 직렬화: 객체 → JSON
String json = mapper.writeValueAsString(user);
// {"name":"홍길동","age":25}

// 역직렬화: JSON → 객체
User user = mapper.readValue(json, User.class);

// Spring에서는 @RestController에서 자동 처리
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id); // 자동으로 JSON 직렬화
}
```

### Java 직렬화 vs JSON 직렬화

| 구분 | Java 직렬화 | JSON 직렬화 |
|------|-----------|------------|
| 형식 | 바이너리 | 텍스트 (JSON) |
| 가독성 | 없음 | 높음 |
| 호환성 | Java만 | 언어 무관 |
| 크기 | 큼 | 작음 |
| 보안 | 취약점 많음 | 상대적 안전 |
| 실무 사용 | 줄어드는 추세 | 주류 |

### 면접 팁
Java 기본 직렬화는 보안 취약점이 많아 실무에서는 JSON(Jackson, Gson)이나 Protocol Buffers를 주로 사용합니다. serialVersionUID의 역할과 transient 키워드의 사용 사례를 설명할 수 있으면 좋습니다.

## 꼬리 질문
1. serialVersionUID를 명시하지 않으면 어떤 문제가 발생하나요?
2. Java 직렬화의 보안 취약점은 무엇인가요?
3. Jackson에서 @JsonIgnore와 transient의 차이는?
