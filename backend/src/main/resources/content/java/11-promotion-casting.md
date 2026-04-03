---
title: "Promotion & Casting"
category: java
difficulty: BASIC
tags: "자동 형변환, 강제 형변환, 프로모션, 데이터 손실"
---

## 질문
Java에서 자동 타입 변환(Promotion)과 강제 타입 변환(Casting)에 대해 설명해주세요.

## 핵심 키워드
- 자동 타입 변환 (Widening)
- 강제 타입 변환 (Narrowing)
- 데이터 손실
- 형변환 규칙
- 연산 시 자동 변환

## 답변

### 자동 타입 변환 (Promotion / Widening)
작은 크기의 타입이 큰 크기의 타입에 저장될 때 자동으로 변환됩니다. 데이터 손실이 없습니다.

```java
byte → short → int → long → float → double
         char ↗

byte b = 10;
int i = b;       // byte → int (자동)
long l = i;      // int → long (자동)
float f = l;     // long → float (자동, 정밀도 손실 가능)
double d = f;    // float → double (자동)
```

### 강제 타입 변환 (Casting / Narrowing)
큰 크기의 타입을 작은 크기의 타입으로 변환할 때 명시적으로 캐스팅해야 합니다. 데이터 손실이 발생할 수 있습니다.

```java
int i = 300;
byte b = (byte) i;  // int → byte (강제), 300은 byte 범위 초과
System.out.println(b); // 44 (데이터 손실!)

double d = 3.14;
int n = (int) d;     // double → int (강제), 소수점 이하 버림
System.out.println(n); // 3

long l = 100L;
int m = (int) l;     // long → int (강제), 범위 내면 안전
```

### 연산 시 자동 타입 변환

```java
// 피연산자 중 큰 타입으로 자동 변환 후 연산
int a = 10;
double b = 5.5;
double result = a + b; // int가 double로 자동 변환: 15.5

// 정수 나눗셈 주의
int x = 7;
int y = 2;
int z = x / y;       // 3 (정수 나눗셈)
double w = x / y;    // 3.0 (이미 3으로 계산된 후 double로 변환)
double v = (double) x / y; // 3.5 (x를 먼저 double로 변환)

// byte, short 연산 결과는 int
byte b1 = 10;
byte b2 = 20;
// byte b3 = b1 + b2; // 컴파일 에러! 결과가 int
int b3 = b1 + b2;     // OK
byte b4 = (byte)(b1 + b2); // OK (강제 캐스팅)
```

### char와 int의 변환

```java
char c = 'A';
int num = c;           // 65 (자동 변환)
char c2 = (char) 66;  // 'B' (강제 변환)

// char 연산
char ch = 'A';
// char result = ch + 1; // 컴파일 에러 (결과가 int)
char result = (char)(ch + 1); // 'B'
```

### 면접 팁
연산 시 자동 형변환 규칙과 정수 나눗셈의 주의점을 기억하세요. `byte + byte = int`인 이유는 JVM이 연산을 int 단위로 수행하기 때문입니다. long에서 float으로의 자동 변환 시 정밀도 손실이 발생할 수 있다는 점도 알아두세요.

## 꼬리 질문
1. long을 float으로 변환할 때 정밀도가 손실되는 이유는?
2. 정수 나눗셈에서 소수점을 보존하려면 어떻게 해야 하나요?
3. byte + byte의 결과가 int인 이유는?
