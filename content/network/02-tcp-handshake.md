---
title: "TCP 3-way & 4-way Handshake"
category: network
difficulty: INTERMEDIATE
tags: "TCP, 3-way handshake, 4-way handshake, SYN, ACK, FIN"
---

## 질문
TCP의 3-way handshake와 4-way handshake에 대해 설명해주세요.

## 핵심 키워드
- SYN, ACK, FIN 플래그
- 연결 설정 (3-way)
- 연결 해제 (4-way)
- TIME_WAIT 상태
- 시퀀스 번호

## 답변
TCP는 신뢰성 있는 연결을 보장하기 위해, 연결을 설정할 때 3-way handshake, 해제할 때 4-way handshake를 수행합니다.

### 3-way Handshake (연결 설정)

```
Client                          Server
  |                               |
  |  1. SYN (seq=x)              |
  |  ──────────────────────────→  |  SYN_SENT → SYN_RECEIVED
  |                               |
  |  2. SYN + ACK (seq=y, ack=x+1)|
  |  ←──────────────────────────  |
  |                               |
  |  3. ACK (ack=y+1)            |
  |  ──────────────────────────→  |  ESTABLISHED
  |         ESTABLISHED           |
```

**과정 설명:**
1. **SYN**: 클라이언트가 서버에 연결 요청 (시퀀스 번호 x)
2. **SYN + ACK**: 서버가 요청을 수락하고 응답 (시퀀스 번호 y, 확인 번호 x+1)
3. **ACK**: 클라이언트가 서버의 응답을 확인 (확인 번호 y+1)

**왜 3-way인가?**
양쪽 모두 데이터를 보내고 받을 수 있는 상태임을 확인하기 위해서입니다. 2-way만으로는 서버가 클라이언트의 수신 능력을 확인할 수 없습니다.

### 4-way Handshake (연결 해제)

```
Client                          Server
  |                               |
  |  1. FIN (seq=u)              |
  |  ──────────────────────────→  |  FIN_WAIT_1 → CLOSE_WAIT
  |                               |
  |  2. ACK (ack=u+1)            |
  |  ←──────────────────────────  |  FIN_WAIT_2
  |                               |
  |     (서버가 남은 데이터 전송)    |
  |                               |
  |  3. FIN (seq=w)              |
  |  ←──────────────────────────  |  LAST_ACK
  |                               |
  |  4. ACK (ack=w+1)            |
  |  ──────────────────────────→  |  CLOSED
  |       TIME_WAIT               |
  |     (2MSL 대기 후 CLOSED)      |
```

**과정 설명:**
1. **FIN**: 클라이언트가 연결 종료 요청
2. **ACK**: 서버가 종료 요청 확인 (아직 보낼 데이터가 있을 수 있음)
3. **FIN**: 서버가 남은 데이터를 모두 보낸 후 종료 요청
4. **ACK**: 클라이언트가 종료 확인

**왜 4-way인가?**
서버가 FIN을 받아도 아직 전송 중인 데이터가 있을 수 있으므로, ACK와 FIN을 별도로 보냅니다.

### TIME_WAIT 상태

클라이언트는 마지막 ACK를 보낸 후 바로 CLOSED가 아닌 TIME_WAIT 상태에서 일정 시간(2MSL) 대기합니다.

**이유:**
1. 마지막 ACK가 손실되었을 때 서버의 FIN 재전송을 처리하기 위해
2. 이전 연결의 지연된 패킷이 새로운 연결에 영향을 주지 않도록 보장

### 면접 팁
3-way와 4-way 각각의 단계를 정확히 설명하고, "왜 3번인지/4번인지"의 이유를 논리적으로 설명하세요. TIME_WAIT의 존재 이유도 자주 출제됩니다. SYN Flood 공격에 대해서도 알아두면 좋습니다.

## 꼬리 질문
1. SYN Flood 공격이란 무엇이며, 어떻게 방어하나요?
2. TIME_WAIT 상태가 너무 많으면 어떤 문제가 발생하나요?
3. TCP의 시퀀스 번호는 왜 랜덤으로 시작하나요?
