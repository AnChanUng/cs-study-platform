---
title: "Java 컴파일 과정"
category: java
difficulty: BASIC
tags: "Java 컴파일, javac, 바이트코드, JIT, 클래스 로더"
---

## 질문
Java 소스 코드가 실행되기까지의 과정을 설명해주세요.

## 핵심 키워드
- javac 컴파일러
- 바이트코드 (.class)
- 클래스 로더 (Class Loader)
- JIT 컴파일러
- 인터프리터

## 답변
Java는 "Write Once, Run Anywhere" 철학에 따라, 소스 코드를 플랫폼 독립적인 바이트코드로 컴파일한 후 JVM에서 실행합니다.

### 컴파일 및 실행 과정

```
1. 소스 코드 작성 (.java)
        ↓
2. javac 컴파일러로 컴파일
        ↓
3. 바이트코드 생성 (.class)
        ↓
4. JVM의 클래스 로더가 바이트코드를 메모리에 로드
        ↓
5. 바이트코드 검증 (Bytecode Verifier)
        ↓
6. 실행 엔진 (Execution Engine)
   ├── 인터프리터: 바이트코드를 한 줄씩 해석/실행
   └── JIT 컴파일러: 자주 실행되는 코드를 네이티브 코드로 변환
```

### 각 단계 상세

**1. javac 컴파일러**
```bash
javac Hello.java  → Hello.class (바이트코드)
```
- 소스 코드의 문법 검사, 타입 체크
- 바이트코드(.class) 생성
- 바이트코드는 JVM이 이해할 수 있는 중간 코드

**2. 클래스 로더 (Class Loader)**
```
Bootstrap ClassLoader → Extension ClassLoader → Application ClassLoader
(JDK 핵심 클래스)      (확장 라이브러리)         (사용자 클래스)
```
- 런타임에 필요한 클래스를 동적으로 로드
- 위임 모델(Delegation Model): 상위 클래스 로더에 먼저 위임
- Loading → Linking(Verify, Prepare, Resolve) → Initialization

**3. 실행 엔진**
- **인터프리터**: 바이트코드를 한 줄씩 해석하여 실행. 초기 실행은 빠르지만 반복 실행 시 느림
- **JIT(Just-In-Time) 컴파일러**: 반복 실행되는 코드(핫스팟)를 감지하여 네이티브 기계어로 컴파일. 이후 실행은 매우 빠름
- 인터프리터와 JIT가 함께 동작하여 최적의 성능 달성

### Java vs C/C++ 컴파일 비교

| 구분 | Java | C/C++ |
|------|------|-------|
| 컴파일 결과 | 바이트코드 | 네이티브 코드 |
| 실행 환경 | JVM 필요 | OS에서 직접 실행 |
| 플랫폼 의존성 | 독립적 | 종속적 |
| 성능 | JIT로 준수 | 빠름 |

### 면접 팁
"Java는 컴파일 언어인가 인터프리터 언어인가?" 라는 질문에 대해, Java는 두 가지 모두에 해당한다고 답하세요. 소스 코드를 바이트코드로 컴파일하고(컴파일), 바이트코드를 JVM에서 인터프리터/JIT로 실행합니다(인터프리트).

## 꼬리 질문
1. JIT 컴파일러의 동작 원리를 설명해주세요.
2. 클래스 로더의 위임 모델(Delegation Model)이란?
3. 바이트코드 검증(Bytecode Verification)은 왜 필요한가요?
