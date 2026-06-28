package com.kronos.domain.schedule

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ScheduleTest : FunSpec({
    val base = LocalDateTime.of(2030, 1, 1, 10, 0)

    fun schedule(start: LocalDateTime, end: LocalDateTime) = Schedule(
        title = "테스트",
        startTime = start,
        endTime = end,
    )

    test("시간이 완전히 겹치면 충돌") {
        val a = schedule(base, base.plusHours(2))
        val b = schedule(base.plusHours(1), base.plusHours(3))
        a.conflictsWith(b) shouldBe true
    }

    test("한 일정이 다른 일정을 완전히 포함하면 충돌") {
        val a = schedule(base, base.plusHours(4))
        val b = schedule(base.plusHours(1), base.plusHours(2))
        a.conflictsWith(b) shouldBe true
    }

    test("연속 일정은 충돌하지 않음") {
        val a = schedule(base, base.plusHours(1))
        val b = schedule(base.plusHours(1), base.plusHours(2))
        a.conflictsWith(b) shouldBe false
    }

    test("완전히 분리된 일정은 충돌하지 않음") {
        val a = schedule(base, base.plusHours(1))
        val b = schedule(base.plusHours(2), base.plusHours(3))
        a.conflictsWith(b) shouldBe false
    }
})
