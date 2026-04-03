# CS Study Platform

CS 면접 준비를 위한 웹 플랫폼입니다. 카테고리별 학습, AI 채점, Slack 리마인드 봇을 제공합니다.

## 주요 기능

### 웹 플랫폼
- **카테고리별 학습** - DB, 자료구조, 운영체제, 네트워크, 알고리즘, 소프트웨어공학, Spring, Java 등 11개 카테고리
- **질문 & 답변** - 마크다운 기반 CS 면접 질문과 모범 답안
- **AI 채점** - 직접 답변을 작성하면 키워드 기반으로 채점 및 피드백
- **학습 통계** - 진행률, 카테고리별 학습 현황, 점수 분포 등
- **검색** - 질문 제목/내용 통합 검색
- **북마크** - 중요한 질문 저장

### Slack 리마인드 봇
- **하루 2회 알림** - 오전 8:20 / 오후 6:10 (KST)
- **요일별 카테고리 스케줄**

| 요일 | 카테고리 |
|------|----------|
| 월 | 운영체제 |
| 화 | 네트워크 |
| 수 | 소프트웨어공학 |
| 목 | DB |
| 금 | DB |
| 토 | Java |
| 일 | Java |

- 가장 덜 학습한 질문 3개 + 정답 미리보기 전송
- 매주 월요일 주간 학습 리포트 전송

## 기술 스택

| 구분 | 기술 |
|------|------|
| **Frontend** | React 19, Vite 8, Axios |
| **Backend** | Spring Boot 3.2, Spring Data JPA |
| **Database** | PostgreSQL |
| **배포** | Vercel (Frontend), Render (Backend + DB) |
| **알림** | Slack Incoming Webhooks |

## 프로젝트 구조

```
cs-study-platform/
├── frontend/          # React + Vite 프론트엔드
│   └── src/
│       ├── pages/     # Home, Category, Question, Stats
│       ├── components/# Layout
│       └── api.js     # Axios 설정
├── backend/           # Spring Boot 백엔드
│   └── src/main/java/com/csstudy/backend/
│       ├── config/    # CORS, 캐시, 데이터 초기화
│       ├── controller/# REST API 컨트롤러
│       ├── service/   # 비즈니스 로직, Slack 서비스
│       ├── entity/    # JPA 엔티티
│       ├── repository/# Spring Data JPA
│       └── scheduler/ # Slack 스케줄러
├── content/           # 마크다운 CS 질문 콘텐츠
├── review/            # 복습용 개념 정리 노트
└── render.yaml        # Render 배포 설정
```

## 배포 URL

- **Frontend**: https://cs-study-platform.vercel.app
- **Backend API**: https://cs-study-backend.onrender.com/api/v1

## 로컬 실행

### Backend
```bash
cd backend
./gradlew bootRun
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

환경변수 설정:
```
VITE_API_URL=http://localhost:8080/api/v1
```
