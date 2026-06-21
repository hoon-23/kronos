# ADR-001: Application Service와 Domain Service의 레이어 분리

- **상태**: 채택
- **날짜**: 2026-06-21

## 컨텍스트

DDD에서 Service 클래스는 크게 **Domain Service**와 **Application Service** 두 가지로 나뉜다. 
클래식 DDD에서는 서비스가 도메인 레이어에 위치하는 것이 일반적이지만, 헥사고날 아키텍처에서는 이 둘의 책임이 명확히 구분된다.

## 결정

`ScheduleService`는 `application/` 레이어에 배치한다.

### Domain Service (도메인 레이어)

- 단일 엔티티에 속하지 않는 **순수 비즈니스 로직**을 담당
- 인프라 의존성(DB, 외부 API, 캐시 등)이 없음
- 현재 프로젝트에서의 예시: `ScheduleValidator`, `Schedule.conflictsWith()`

### Application Service (애플리케이션 레이어)

- 유스케이스 흐름을 **오케스트레이션**하는 역할
- 도메인 객체를 호출하고, 트랜잭션을 관리하고, 포트를 통해 외부 시스템과 연결
- 그 자체가 비즈니스 로직이 아니라 **조율자**
- 현재 프로젝트에서의 예시: `ScheduleService`

## 근거

`ScheduleService`가 수행하는 책임을 분석하면:

| 책임 | 유형 |
|------|------|
| 포트(DB, Claude API, Redis) 조합 | 오케스트레이션 |
| `@Transactional` 트랜잭션 관리 | 인프라 관심사 |
| `ScheduleValidator.validate()` 호출 | 도메인 로직에 **위임** |
| `Schedule.conflictsWith()` 호출 | 도메인 로직에 **위임** |

이 모든 것이 **흐름 제어**이지 도메인 지식 자체가 아니므로, `application/` 레이어에 위치하는 것이 적절하다.

## 패키지 구조

```
domain/schedule/
├── Schedule.kt              ← 도메인 모델 + 비즈니스 로직 (conflictsWith)
├── ScheduleValidator.kt     ← 도메인 서비스 (순수 검증 규칙)
└── ScheduleException.kt     ← 도메인 예외

application/schedule/
├── port/inbound/             ← 유스케이스 계약 (인바운드 포트)
├── port/outbound/            ← 인프라 계약 (아웃바운드 포트)
└── service/
    └── ScheduleService.kt    ← 애플리케이션 서비스 (오케스트레이션)
```

## 향후 고려사항

여러 애그리거트에 걸치는 순수 비즈니스 규칙이 생기면, `domain/` 아래에 Domain Service로 별도 생성한다.
