---
title: "Record"
category: java
difficulty: INTERMEDIATE
tags: "Record, 불변 객체, DTO, Java 16, 데이터 클래스"
---

## 질문
Java의 Record란 무엇이며, 어떤 상황에서 사용하나요?

## 핵심 키워드
- 불변 데이터 클래스
- 자동 생성 (equals, hashCode, toString)
- DTO
- compact constructor
- Java 16 정식 도입

## 답변
Record는 Java 16에서 정식 도입된 불변 데이터 클래스입니다. 데이터를 담는 것이 주 목적인 클래스를 간결하게 선언할 수 있습니다.

### 기본 사용

```java
// Record 정의
public record UserDto(Long id, String name, String email) {}

// 위 한 줄이 아래 전체와 동일
public final class UserDto {
    private final Long id;
    private final String name;
    private final String email;

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long id() { return id; }        // getter (get 접두사 없음)
    public String name() { return name; }
    public String email() { return email; }

    @Override public boolean equals(Object o) { ... }
    @Override public int hashCode() { ... }
    @Override public String toString() { ... }
}
```

### Record가 자동 생성하는 것
- **private final 필드**: 모든 컴포넌트
- **생성자**: 모든 필드를 인자로 받는 표준 생성자
- **접근자 메서드**: 필드명과 동일한 메서드 (id(), name())
- **equals() / hashCode()**: 모든 필드 기반
- **toString()**: 모든 필드 포함

### Compact Constructor (검증 로직)

```java
public record UserDto(Long id, String name, String email) {
    // Compact Constructor: 매개변수 목록 없이 선언
    public UserDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        if (email != null) {
            email = email.toLowerCase(); // 값 변환 가능
        }
    }
}
```

### Record의 제약사항
- **불변**: 필드를 변경할 수 없음 (setter 없음)
- **상속 불가**: final 클래스, 다른 클래스를 상속할 수 없음
- **인터페이스 구현**: 가능
- **정적 필드/메서드**: 가능
- **인스턴스 필드 추가**: 불가

```java
// 인터페이스 구현 가능
public record Point(int x, int y) implements Comparable<Point> {
    @Override
    public int compareTo(Point other) {
        return Integer.compare(this.x, other.x);
    }

    // 정적 메서드 가능
    public static Point origin() {
        return new Point(0, 0);
    }

    // 커스텀 메서드 가능
    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }
}
```

### 실무 활용

```java
// DTO로 활용
public record CreateUserRequest(
    @NotBlank String name,
    @Email String email,
    @Min(0) int age
) {}

// Spring Controller에서 사용
@PostMapping("/users")
public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
    User user = userService.create(request);
    return ResponseEntity.ok(UserResponse.from(user));
}

public record UserResponse(Long id, String name, String email) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
```

### 면접 팁
Record는 DTO, 값 객체(Value Object) 등 데이터를 운반하는 목적의 클래스에 적합합니다. JPA 엔티티에는 사용할 수 없습니다(기본 생성자, setter 필요). Lombok의 @Data와 비교하여 설명할 수 있으면 좋습니다.

## 꼬리 질문
1. Record를 JPA 엔티티로 사용할 수 없는 이유는?
2. Record와 Lombok @Value의 차이는?
3. Record에서 불변성을 깨뜨릴 수 있는 경우는?
