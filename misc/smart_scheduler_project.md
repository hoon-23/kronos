# SmartScheduler 사이드 프로젝트 진행 방향

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
| Cache | Redis |
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

### 4. Redis 캐싱
- 자주 조회되는 일정 캐싱
- TTL 기반 캐시 만료 처리

### 5. Kotest 테스트 코드
- 도메인 단위 테스트
- API 통합 테스트

---

## DDD 도메인 구조

```
domain/
├── schedule/
│   ├── Schedule.kt          # 일정 엔티티
│   ├── ScheduleRepository.kt
│   └── ScheduleService.kt
├── participant/
│   ├── Participant.kt       # 참여자 엔티티
│   └── ParticipantRepository.kt
└── notification/
    └── Notification.kt      # 알림 엔티티
```

### 헥사고날 아키텍처 레이어

```
application/
├── port/
│   ├── in/                  # UseCase 인터페이스
│   └── out/                 # Repository 인터페이스
└── service/                 # UseCase 구현체

adapter/
├── in/
│   └── web/                 # REST Controller
└── out/
    ├── persistence/         # JPA Repository 구현체
    ├── cache/               # Redis 구현체
    └── ai/                  # Claude API 구현체
```

---

## API 설계

```
POST /api/v1/schedules/natural    # 자연어로 일정 등록
POST /api/v1/schedules            # 직접 일정 등록
GET  /api/v1/schedules            # 일정 목록 조회
GET  /api/v1/schedules/{id}       # 일정 상세 조회
PUT  /api/v1/schedules/{id}       # 일정 수정
DELETE /api/v1/schedules/{id}     # 일정 삭제
GET  /api/v1/schedules/conflicts  # 충돌 일정 조회
```

---

## 3주 완성 플랜

### 1주차: 기본 구조 설계 및 CRUD
- [ ] 프로젝트 세팅 (Spring Boot + Kotlin + Docker)
- [ ] DDD/헥사고날 패키지 구조 설계
- [ ] 일정 기본 CRUD 구현
- [ ] PostgreSQL 연동 및 JPA 엔티티 설계
- [ ] GitHub 레포지토리 생성 및 GitHub Actions 설정

### 2주차: Claude API 연동 + Redis 캐싱
- [ ] Claude API 연동 (자연어 파싱)
- [ ] 자연어 → 일정 변환 로직 구현
- [ ] 중복 일정 충돌 감지 로직
- [ ] Redis 캐싱 적용
- [ ] 예외 처리 및 에러 응답 설계

### 3주차: 테스트 + 문서화 + 배포
- [ ] Kotest 단위 테스트 작성
- [ ] API 통합 테스트 작성
- [ ] README.md 작성 (설계 의도, 실행 방법, API 문서)
- [ ] Docker Compose 구성
- [ ] AWS 배포 (선택사항)

---

## GitHub README 필수 항목
- 프로젝트 소개 및 동기
- 기술 스택 및 선택 이유
- 아키텍처 다이어그램
- 주요 기능 설명
- 실행 방법
- API 문서
- 트러블슈팅 경험

---

## 면접 어필 포인트
- "이직 준비 중 AI 연동 서비스를 직접 경험해보고 싶어 만들었습니다"
- DDD/헥사고날 설계 의도 설명 가능
- Claude API 실제 활용 경험 어필
- Kotest 테스트 코드로 코드 품질 입증
