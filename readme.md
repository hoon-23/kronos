# SmartScheduler 사이드 프로젝트

## 프로젝트 개요
자연어로 일정을 등록할 수 있는 AI 연동 일정/예약 관리 API

**핵심 차별점**
- Claude API를 활용한 자연어 일정 파싱
- DDD/헥사고날 아키텍처 적용
- Kotlin + Spring Boot 기반

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin |
| Framework | Spring Boot |
| Architecture | DDD / 헥사고날 아키텍처 |
| DB | PostgreSQL + JPA |
| AI | Claude API (Anthropic) |
| Test | Kotest |
| CI/CD | GitHub Actions |
| Infra | Docker |

---

## 핵심 기능

### 1. 자연어 일정 등록 (Claude API 연동)
- 예: "다음주 화요일 오후 3시에 팀 미팅 잡아줘"
- Claude API가 자연어를 파싱 → 날짜/시간/제목/참여자 추출
- 파싱된 데이터로 일정 자동 등록

### 2. 일정 CRUD API
- 일정 생성/조회/수정/삭제
- 날짜별/참여자별 조회

### 3. 중복 일정 충돌 감지
- 동일 시간대 일정 중복 체크
- 충돌 시 경고 응답

### 4. Kotest 테스트 코드
- 도메인 단위 테스트
- API 통합 테스트

---

## 패키지 구조

```
src/main/kotlin/com/kronos/
│
├── KronosApplication.kt
│
├── domain/
│   └── schedule/
│       ├── Schedule.kt
│       └── ScheduleValidator.kt
│
├── application/
│   └── schedule/
│       ├── port/
│       │   ├── inbound/
│       │   │   ├── CreateScheduleUseCase.kt
│       │   │   ├── GetScheduleUseCase.kt
│       │   │   ├── UpdateScheduleUseCase.kt
│       │   │   └── DeleteScheduleUseCase.kt
│       │   └── outbound/
│       │       ├── SaveSchedulePort.kt
│       │       ├── LoadSchedulePort.kt
│       │       └── DeleteSchedulePort.kt
│       └── service/
│           └── ScheduleService.kt
│
└── infra/
    ├── web/
    │   ├── ScheduleController.kt
    │   └── dto/
    │       ├── CreateScheduleRequest.kt
    │       └── ScheduleResponse.kt
    ├── persistence/
    │   ├── ScheduleEntity.kt
    │   ├── SchedulePersistenceAdapter.kt
    │   ├── repository/
    │   │   └── ScheduleJpaRepository.kt
    │   └── mapper/
    │       └── ScheduleMapper.kt
    └── ai/
        └── ClaudeAdapter.kt
```

---

## API 문서

서버 실행 후 아래 주소에서 확인할 수 있습니다.

| 항목 | 주소 |
|------|------|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

## API 설계

```
POST   /api/v1/schedules/natural    # 자연어로 일정 등록 (Claude AI)
POST   /api/v1/schedules            # 직접 일정 등록
GET    /api/v1/schedules            # 일정 목록 조회
GET    /api/v1/schedules/{id}       # 일정 상세 조회
PUT    /api/v1/schedules/{id}       # 일정 수정
DELETE /api/v1/schedules/{id}       # 일정 삭제
GET    /api/v1/schedules/conflicts  # 충돌 일정 조회
```

