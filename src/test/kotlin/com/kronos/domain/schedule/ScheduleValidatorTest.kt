package com.kronos.domain.schedule

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import java.time.LocalDateTime

class ScheduleValidatorTest : FunSpec({
    val future = LocalDateTime.now().plusDays(1)

    fun schedule(
        title: String = "테스트",
        startTime: LocalDateTime = future,
        endTime: LocalDateTime = future.plusHours(1),
    ) = Schedule(title = title, startTime = startTime, endTime = endTime)

    test("정상 일정은 예외 없음") {
        ScheduleValidator.validate(schedule())
    }

    test("제목이 공백이면 예외") {
        val ex = shouldThrow<InvalidScheduleException> {
            ScheduleValidator.validate(schedule(title = "   "))
        }
        ex.message shouldContain "제목"
    }

    test("종료 시간이 시작 시간보다 이르면 예외") {
        val ex = shouldThrow<InvalidScheduleException> {
            ScheduleValidator.validate(schedule(startTime = future.plusHours(2), endTime = future))
        }
        ex.message shouldContain "시작 시간"
    }

    test("시작 시간이 현재 이전이면 예외") {
        val ex = shouldThrow<InvalidScheduleException> {
            ScheduleValidator.validate(schedule(startTime = LocalDateTime.now().minusHours(1), endTime = LocalDateTime.now()))
        }
        ex.message shouldContain "현재 시간"
    }
})
