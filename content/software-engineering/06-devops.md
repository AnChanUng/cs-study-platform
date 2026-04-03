---
title: "데브옵스(DevOps)"
category: software-engineering
difficulty: INTERMEDIATE
tags: "DevOps, CI/CD, 자동화, 인프라, 문화"
---

## 질문
DevOps란 무엇이며, 왜 중요한가요?

## 핵심 키워드
- 개발(Dev) + 운영(Ops) 통합
- CI/CD
- 자동화
- 인프라 as 코드
- 지속적 모니터링

## 답변
DevOps는 소프트웨어 개발(Development)과 IT 운영(Operations)을 통합하여, 소프트웨어의 개발, 배포, 운영 전 과정을 자동화하고 효율화하는 문화, 방법론, 도구의 집합입니다.

### DevOps의 핵심 원칙

**1. 지속적 통합 (CI - Continuous Integration)**
개발자가 코드를 자주 메인 브랜치에 병합하고, 자동으로 빌드와 테스트를 수행합니다.

```yaml
# GitHub Actions CI 예시
name: CI
on: push
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Build
        run: ./gradlew build
      - name: Test
        run: ./gradlew test
```

**2. 지속적 배포 (CD - Continuous Delivery/Deployment)**
- Continuous Delivery: 배포 가능한 상태를 항상 유지
- Continuous Deployment: 자동으로 프로덕션 배포

**3. 인프라 as 코드 (IaC - Infrastructure as Code)**
인프라를 코드로 관리하여 버전 관리, 재현성, 자동화를 달성합니다.
```
Terraform, Ansible, CloudFormation 등
```

**4. 마이크로서비스 아키텍처**
작은 독립적인 서비스로 분리하여 독립적인 배포와 확장을 가능하게 합니다.

**5. 모니터링 & 로깅**
시스템의 상태를 실시간으로 모니터링하고 문제를 빠르게 감지합니다.
```
Prometheus, Grafana, ELK Stack 등
```

### DevOps 파이프라인

```
Plan → Code → Build → Test → Release → Deploy → Operate → Monitor
  ↑                                                            ↓
  └────────────────── 피드백 루프 ─────────────────────────────┘
```

### DevOps 도구 체인

| 단계 | 도구 |
|------|------|
| 소스 관리 | Git, GitHub, GitLab |
| CI/CD | Jenkins, GitHub Actions, GitLab CI |
| 컨테이너 | Docker, Kubernetes |
| IaC | Terraform, Ansible |
| 모니터링 | Prometheus, Grafana, Datadog |
| 로깅 | ELK Stack (Elasticsearch, Logstash, Kibana) |

### DevOps의 장점
- 배포 빈도 증가, 배포 시간 단축
- 장애 복구 시간 단축 (MTTR)
- 변경 실패율 감소
- 개발과 운영 간의 협업 향상

### 면접 팁
DevOps는 단순히 도구가 아니라 "문화"라는 점을 강조하세요. 실무에서 CI/CD 파이프라인을 구축한 경험이나, Docker/Kubernetes를 사용한 경험을 구체적으로 설명하면 좋습니다.

## 꼬리 질문
1. CI와 CD의 차이점은 무엇인가요?
2. Blue-Green 배포와 Canary 배포의 차이는?
3. 컨테이너와 가상 머신(VM)의 차이는 무엇인가요?
