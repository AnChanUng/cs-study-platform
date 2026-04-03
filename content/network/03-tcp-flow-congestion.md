---
title: "TCP 흐름제어 & 혼잡제어"
category: network
difficulty: ADVANCED
tags: "흐름제어, 혼잡제어, 슬라이딩 윈도우, AIMD, Slow Start"
---

## 질문
TCP의 흐름제어와 혼잡제어에 대해 설명해주세요.

## 핵심 키워드
- 흐름제어 (Flow Control)
- 혼잡제어 (Congestion Control)
- 슬라이딩 윈도우
- Slow Start / AIMD
- 혼잡 윈도우 (cwnd)

## 답변

### 흐름제어 (Flow Control)

송신자가 수신자의 처리 능력을 초과하여 데이터를 보내지 않도록 조절하는 메커니즘입니다. 수신자가 감당할 수 있는 속도로 데이터를 전송합니다.

**슬라이딩 윈도우 (Sliding Window)**
수신자가 자신의 수신 버퍼 크기(Window Size)를 송신자에게 알려주면, 송신자는 윈도우 크기만큼만 데이터를 전송합니다.

```
송신자 윈도우:
[1][2][3][4][5][6][7][8][9][10]...
         ├──────────┤
         윈도우 크기 = 4
         ACK 2 수신 → 윈도우 이동
[1][2][3][4][5][6][7][8][9][10]...
            ├──────────┤
```

- ACK를 받으면 윈도우가 오른쪽으로 이동
- 수신자가 Window Size = 0을 보내면 전송 중단
- 수신자의 버퍼가 비면 Window Update로 재개

### 혼잡제어 (Congestion Control)

네트워크의 혼잡 상태를 감지하고, 송신 속도를 조절하여 네트워크 붕괴를 방지하는 메커니즘입니다.

**혼잡 윈도우 (cwnd - Congestion Window)**: 송신자가 관리하는 윈도우 크기
**실제 전송량** = min(수신자 윈도우, 혼잡 윈도우)

### 혼잡제어 알고리즘

**1. Slow Start (느린 시작)**
- 초기 cwnd = 1 MSS(Maximum Segment Size)
- ACK를 받을 때마다 cwnd를 2배로 증가 (지수적 증가)
- ssthresh(Slow Start Threshold)에 도달하면 혼잡 회피로 전환

```
RTT 1: cwnd = 1  → 1개 세그먼트 전송
RTT 2: cwnd = 2  → 2개 세그먼트 전송
RTT 3: cwnd = 4  → 4개 세그먼트 전송
RTT 4: cwnd = 8  → ssthresh 도달 → 혼잡 회피
```

**2. 혼잡 회피 (Congestion Avoidance) - AIMD**
- cwnd를 RTT마다 1 MSS씩 선형적으로 증가 (Additive Increase)
- 패킷 손실 발생 시 cwnd를 절반으로 감소 (Multiplicative Decrease)

```
cwnd 변화:
     ^
cwnd |    /\    /\
     |   /  \  /  \
     |  /    \/    \
     | /             \
     |/________________\___→ time
     ssthresh
```

**3. 빠른 재전송 (Fast Retransmit)**
- 3개의 중복 ACK를 받으면 타임아웃을 기다리지 않고 즉시 재전송

**4. 빠른 회복 (Fast Recovery)**
- 3개의 중복 ACK 시 cwnd를 절반으로 줄이고 혼잡 회피 단계로 진입
- 타임아웃 발생 시에만 Slow Start로 돌아감

### TCP Tahoe vs TCP Reno

| 이벤트 | Tahoe | Reno |
|--------|-------|------|
| 3 중복 ACK | cwnd=1, Slow Start | cwnd 절반, Fast Recovery |
| Timeout | cwnd=1, Slow Start | cwnd=1, Slow Start |

### 면접 팁
흐름제어와 혼잡제어를 혼동하지 마세요. 흐름제어는 "수신자 보호"(수신자가 처리할 수 있는 속도로), 혼잡제어는 "네트워크 보호"(네트워크가 감당할 수 있는 속도로)입니다.

## 꼬리 질문
1. 흐름제어와 혼잡제어의 차이점을 요약해주세요.
2. TCP Tahoe와 TCP Reno의 차이는 무엇인가요?
3. CUBIC TCP란 무엇이며, 기존 방식과 어떻게 다른가요?
