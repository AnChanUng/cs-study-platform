---
title: "SpringApplication"
category: spring
difficulty: INTERMEDIATE
tags: "Spring Boot, 자동 설정, 내장 서버, 스타터, SpringApplication"
---

## 질문
Spring Boot란 무엇이며, Spring Framework와의 차이점을 설명해주세요.

## 핵심 키워드
- 자동 설정 (Auto Configuration)
- 내장 서버 (Embedded Server)
- 스타터 의존성 (Starter)
- SpringApplication
- application.yml

## 답변
Spring Boot는 Spring Framework를 기반으로 하여, 복잡한 설정 없이 빠르게 Spring 애플리케이션을 개발할 수 있게 해주는 프레임워크입니다. "Convention over Configuration(설정보다 관례)" 원칙을 따릅니다.

### Spring Boot의 핵심 특징

**1. 자동 설정 (Auto Configuration)**
클래스패스에 있는 라이브러리를 감지하여 자동으로 적절한 설정을 적용합니다.

```java
@SpringBootApplication  // 아래 3개를 포함
// @SpringBootConfiguration
// @EnableAutoConfiguration  ← 자동 설정 핵심
// @ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

예: spring-boot-starter-data-jpa 의존성 추가 시 DataSource, EntityManagerFactory, TransactionManager가 자동 설정됩니다.

**2. 내장 서버 (Embedded Server)**
Tomcat, Jetty, Undertow를 내장하여 별도의 서버 설치 없이 독립 실행 가능합니다.

```bash
# JAR로 패키징 후 바로 실행
java -jar my-app.jar
```

**3. 스타터 의존성 (Starter Dependencies)**
관련 라이브러리들을 묶어 의존성 관리를 간소화합니다.

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    // spring-webmvc, jackson, tomcat 등이 포함됨

    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    // spring-data-jpa, hibernate 등이 포함됨
}
```

**4. 외부 설정 (Externalized Configuration)**
```yaml
# application.yml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
```

### SpringApplication 실행 과정

```
1. SpringApplication 인스턴스 생성
2. 환경 설정 (프로파일, 프로퍼티 소스)
3. ApplicationContext 생성
4. Bean 정의 로드
5. Auto Configuration 적용
6. Bean 인스턴스화 및 의존성 주입
7. ApplicationRunner / CommandLineRunner 실행
8. 내장 서버 시작 (웹 애플리케이션인 경우)
```

### Spring vs Spring Boot

| 구분 | Spring | Spring Boot |
|------|--------|-------------|
| 설정 | XML/Java Config 수동 설정 | 자동 설정 |
| 서버 | 외부 서버 필요 | 내장 서버 |
| 의존성 | 개별 관리 | 스타터로 일괄 관리 |
| 배포 | WAR | JAR (독립 실행) |
| 시작 코드 | 많음 | 최소화 |

### 면접 팁
@SpringBootApplication이 포함하는 세 가지 어노테이션의 역할을 정확히 설명하세요. 특히 @EnableAutoConfiguration의 동작 원리(spring.factories/AutoConfiguration.imports)를 이해하면 좋습니다. 자동 설정을 커스터마이징하는 방법(@Conditional, @ConditionalOnClass 등)도 알아두세요.

## 꼬리 질문
1. @EnableAutoConfiguration은 어떻게 동작하나요?
2. Spring Boot에서 프로파일(Profile)은 어떻게 관리하나요?
3. 자동 설정을 비활성화하거나 커스터마이징하는 방법은?
