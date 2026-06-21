# Kronos 프로젝트 가이드

## 프로젝트 개요
- 자연어로 일정을 등록할 수 있는 AI 연동 일정/예약 관리 REST API
- Kotlin + Spring Boot, DDD/헥사고날 아키텍처
- Claude API, PostgreSQL, Redis

## 레이어 구조
- `domain/` — 순수 비즈니스 로직. 외부 의존성 없음 (엔티티, 도메인 서비스, 도메인 예외)
- `application/` — 유스케이스 오케스트레이션. 포트 정의 + 애플리케이션 서비스
- `infra/` — 기술 구현. 웹(컨트롤러/DTO), 영속성(JPA), 외부 서비스(Claude API, Redis)

## 작업 규칙
- 아키텍처 관련 의사결정/논의는 `docs/adr/`에 ADR로 기록
- 한국어 주석/에러 메시지 사용
- DDD/헥사고날 개념 설명은 간결하게 (최대 2줄)

## 빌드 & 실행
```bash
./gradlew build          # 빌드
./gradlew bootRun        # 실행 (PostgreSQL, Redis 필요)
docker compose -f docker/postgre/docker-compose.yml up -d  # PostgreSQL
docker compose -f docker/redis/docker-compose.yml up -d    # Redis
```
