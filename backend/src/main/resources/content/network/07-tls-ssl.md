---
title: "TLS/SSL Handshake"
category: network
difficulty: ADVANCED
tags: "TLS, SSL, handshake, 인증서, 암호화 스위트"
---

## 질문
TLS/SSL Handshake의 동작 과정에 대해 설명해주세요.

## 핵심 키워드
- TLS (Transport Layer Security)
- SSL 인증서
- CA (Certificate Authority)
- 암호화 스위트 (Cipher Suite)
- 세션 키

## 답변
TLS(Transport Layer Security)는 SSL(Secure Sockets Layer)의 후속 프로토콜로, 네트워크 통신의 기밀성과 무결성을 보장합니다. TLS Handshake는 암호화된 통신을 시작하기 위한 초기 협상 과정입니다.

### TLS 1.2 Handshake 과정

```
Client                                Server
  |                                     |
  | 1. ClientHello                      |
  |  (TLS 버전, 암호화 스위트 목록,       |
  |   클라이언트 랜덤값)                  |
  | ──────────────────────────────────→ |
  |                                     |
  | 2. ServerHello                      |
  |  (선택된 암호화 스위트, 서버 랜덤값)   |
  | 3. Certificate (SSL 인증서)          |
  | 4. ServerHelloDone                  |
  | ←────────────────────────────────── |
  |                                     |
  | 5. ClientKeyExchange                |
  |  (Pre-Master Secret을 공개키로 암호화)|
  | 6. ChangeCipherSpec                 |
  | 7. Finished                         |
  | ──────────────────────────────────→ |
  |                                     |
  | 8. ChangeCipherSpec                 |
  | 9. Finished                         |
  | ←────────────────────────────────── |
  |                                     |
  |    암호화된 데이터 통신 시작           |
```

### 상세 과정

**1. ClientHello**
클라이언트가 지원하는 TLS 버전, 암호화 스위트 목록, 클라이언트 랜덤값을 서버에 전송합니다.

**2-4. ServerHello ~ ServerHelloDone**
서버가 사용할 암호화 스위트를 선택하고, SSL 인증서(공개키 포함)를 전송합니다.

**5. 인증서 검증 및 키 교환**
- 클라이언트가 인증서의 유효성을 CA(인증 기관)를 통해 검증
- Pre-Master Secret을 생성하고 서버의 공개키로 암호화하여 전송
- 양쪽 모두 클라이언트 랜덤 + 서버 랜덤 + Pre-Master Secret으로 세션 키(대칭키) 생성

**6-9. 암호화 전환**
양쪽 모두 세션 키를 사용한 암호화 통신으로 전환합니다.

### SSL 인증서 구조

```
인증서 내용:
- 서버 도메인 이름
- 서버 공개키
- 인증기관(CA) 정보
- 유효 기간
- 디지털 서명 (CA의 개인키로 서명)
```

### 인증서 검증 체인 (Certificate Chain)

```
루트 CA (자체 서명) → 중간 CA → 서버 인증서
브라우저에 루트 CA 인증서가 내장되어 있어 신뢰 가능
```

### TLS 1.3 개선사항

TLS 1.3은 핸드셰이크를 1-RTT로 줄여 속도를 개선했습니다.

```
TLS 1.2: 2-RTT (왕복 2번)
TLS 1.3: 1-RTT (왕복 1번)
TLS 1.3 + 0-RTT: 이전 연결 정보 재사용
```

- 안전하지 않은 알고리즘 제거 (RC4, DES, MD5 등)
- 키 교환과 암호화 스위트 간소화
- Forward Secrecy 필수 (Diffie-Hellman 기반)

### 암호화 스위트 예시

```
TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
│     │     │        │    │     │
│     │     │        │    │     └─ 해시 함수
│     │     │        │    └─ 운영 모드
│     │     │        └─ 대칭키 알고리즘/키 크기
│     │     └─ 인증 알고리즘
│     └─ 키 교환 알고리즘
└─ 프로토콜
```

### 면접 팁
TLS Handshake에서 비대칭키가 대칭키를 교환하는 데 사용되고, 이후 대칭키로 실제 통신이 이루어진다는 하이브리드 방식을 이해하세요. Forward Secrecy의 개념과 TLS 1.3의 개선점도 알아두면 좋습니다.

## 꼬리 질문
1. Forward Secrecy(완전 순방향 비밀성)란 무엇인가요?
2. 자체 서명 인증서(Self-signed Certificate)의 문제점은?
3. TLS 1.2와 TLS 1.3의 주요 차이점은?
