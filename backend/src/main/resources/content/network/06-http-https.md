---
title: "HTTP & HTTPS"
category: network
difficulty: BASIC
tags: "HTTP, HTTPS, SSL, TLS, 상태코드, REST"
---

## 질문
HTTP와 HTTPS의 차이점에 대해 설명해주세요.

## 핵심 키워드
- HTTP (HyperText Transfer Protocol)
- HTTPS (HTTP Secure)
- SSL/TLS
- 상태 코드
- Stateless

## 답변
HTTP(HyperText Transfer Protocol)는 웹에서 클라이언트와 서버 간에 데이터를 주고받기 위한 프로토콜이며, HTTPS는 HTTP에 보안(SSL/TLS)을 추가한 프로토콜입니다.

### HTTP 특징

**1. Stateless (무상태)**
각 요청은 독립적이며 이전 요청의 정보를 기억하지 않습니다. 상태 유지가 필요하면 쿠키, 세션, 토큰을 사용합니다.

**2. 비연결형 (Connectionless)**
요청-응답 후 연결을 끊습니다. (HTTP/1.1부터는 Keep-Alive로 연결 유지 가능)

**3. 요청 메서드**
```
GET    - 리소스 조회
POST   - 리소스 생성
PUT    - 리소스 전체 수정
PATCH  - 리소스 부분 수정
DELETE - 리소스 삭제
```

**4. 주요 상태 코드**
```
1xx - 정보 (처리 중)
2xx - 성공
  200 OK, 201 Created, 204 No Content
3xx - 리다이렉션
  301 Moved Permanently, 302 Found, 304 Not Modified
4xx - 클라이언트 오류
  400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found
5xx - 서버 오류
  500 Internal Server Error, 502 Bad Gateway, 503 Service Unavailable
```

### HTTP vs HTTPS

| 구분 | HTTP | HTTPS |
|------|------|-------|
| 포트 | 80 | 443 |
| 보안 | 없음 (평문 전송) | SSL/TLS 암호화 |
| 속도 | 약간 빠름 | 약간 느림 (암호화 오버헤드) |
| 인증서 | 불필요 | SSL 인증서 필요 |
| URL | http:// | https:// |

### HTTPS 동작 원리

```
1. 클라이언트가 서버에 HTTPS 요청
2. 서버가 SSL 인증서(공개키 포함)를 전송
3. 클라이언트가 인증서의 유효성을 CA를 통해 검증
4. 클라이언트가 대칭키를 생성하여 서버의 공개키로 암호화하여 전송
5. 서버가 개인키로 복호화하여 대칭키를 획득
6. 대칭키를 사용하여 암호화된 통신 시작
```

### HTTPS가 필요한 이유
- **기밀성**: 제3자가 데이터를 도청할 수 없음
- **무결성**: 데이터가 전송 중 변조되지 않음을 보장
- **인증**: 서버의 신원을 인증서로 확인

### HTTP 버전별 발전

```
HTTP/1.0 - 요청마다 연결 생성/종료
HTTP/1.1 - Keep-Alive, 파이프라이닝
HTTP/2   - 멀티플렉싱, 헤더 압축, 서버 푸시
HTTP/3   - QUIC(UDP 기반), 더 빠른 연결 설정
```

### 면접 팁
HTTPS의 동작 원리(SSL/TLS handshake)를 순서대로 설명할 수 있어야 합니다. HTTP/2와 HTTP/3의 개선점도 알아두면 좋습니다. REST API와 연관하여 HTTP 메서드와 상태 코드를 설명할 수 있으면 좋습니다.

## 꼬리 질문
1. HTTP/2의 멀티플렉싱이란 무엇인가요?
2. SSL 인증서는 어떻게 신뢰할 수 있나요? (CA의 역할)
3. 쿠키와 세션의 차이점은 무엇인가요?
