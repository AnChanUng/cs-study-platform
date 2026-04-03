---
title: "Call by Value vs Call by Reference"
category: java
difficulty: BASIC
tags: "Call by Value, Call by Reference, 참조, 값 복사, Java"
---

## 질문
Java는 Call by Value인가요, Call by Reference인가요?

## 핵심 키워드
- Call by Value
- 값 복사
- 참조값 복사
- 원본 변경 불가
- 객체 필드 변경 가능

## 답변
Java는 항상 **Call by Value(값에 의한 호출)**입니다. 메서드에 인자를 전달할 때 값을 복사하여 전달합니다. 객체를 전달할 때도 객체 자체가 아닌 참조값(주소값)을 복사하여 전달합니다.

### 기본 타입 전달

```java
void changeValue(int num) {
    num = 100; // 복사된 값을 변경 → 원본에 영향 없음
}

int x = 10;
changeValue(x);
System.out.println(x); // 10 (변경 안 됨)
```

### 객체(참조 타입) 전달

```java
void changeObject(User user) {
    user.setName("김길동"); // 같은 객체를 참조하므로 원본 변경 가능
}

User user = new User("홍길동");
changeObject(user);
System.out.println(user.getName()); // "김길동" (변경됨!)
```

**이것은 Call by Reference가 아닙니다.** 참조값(주소)이 복사된 것입니다.

```java
void replaceObject(User user) {
    user = new User("완전 새 객체"); // 지역 변수가 새 객체를 가리킴
    // 원본 참조에는 영향 없음
}

User user = new User("홍길동");
replaceObject(user);
System.out.println(user.getName()); // "홍길동" (변경 안 됨!)
```

### 핵심 차이

```
Call by Value (Java):
  원본 변수의 값(또는 참조값)을 복사하여 전달
  메서드 내에서 매개변수 자체를 변경해도 원본에 영향 없음

Call by Reference (C++ 등):
  원본 변수 자체의 참조(별명)를 전달
  메서드 내에서 매개변수를 변경하면 원본도 변경됨
```

```
Java 메모리:
Main:   user ──→ [User: "홍길동"]  (Heap)
                      ↑
Method: user ──→──────┘  (복사된 참조값이 같은 객체를 가리킴)

replaceObject 호출 후:
Main:   user ──→ [User: "홍길동"]  (원본 그대로)
Method: user ──→ [User: "완전 새 객체"]  (지역 변수가 새 객체 가리킴)
```

### 면접 팁
Java는 "참조를 값으로 전달(pass reference by value)"합니다. 객체의 필드를 변경할 수 있는 것은 같은 객체를 참조하기 때문이며, 참조 변수 자체를 교체할 수는 없습니다. 이 차이를 코드 예시로 명확하게 설명하세요.

## 꼬리 질문
1. Java에서 메서드로 전달된 객체를 완전히 교체할 수 없는 이유는?
2. String이 불변인 것과 Call by Value의 관계는?
3. swap 메서드를 Java에서 구현하는 방법은?
