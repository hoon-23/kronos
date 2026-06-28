package com.kronos.application.schedule.service

import com.kronos.application.schedule.port.inbound.CreateScheduleCommand
import com.kronos.application.schedule.port.inbound.UpdateScheduleCommand
import com.kronos.application.schedule.port.outbound.DeleteSchedulePort
import com.kronos.application.schedule.port.outbound.LoadSchedulePort
import com.kronos.application.schedule.port.outbound.NaturalLanguageParsePort
import com.kronos.application.schedule.port.outbound.SaveSchedulePort
import com.kronos.domain.schedule.Schedule
import com.kronos.domain.schedule.ScheduleConflictException
import com.kronos.domain.schedule.ScheduleNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import java.util.UUID

class ScheduleServiceTest : FunSpec({
    val savePort = mockk<SaveSchedulePort>()
    val loadPort = mockk<LoadSchedulePort>()
    val deletePort = mockk<DeleteSchedulePort>()
    val nlpPort = mockk<NaturalLanguageParsePort>()
    val service = ScheduleService(savePort, loadPort, deletePort, nlpPort)

    val future = LocalDateTime.now().plusDays(1)
    val scheduleId = UUID.randomUUID()

    fun sampleSchedule(id: UUID = scheduleId) = Schedule(
        id = id,
        title = "팀 회의",
        startTime = future,
        endTime = future.plusHours(1),
    )

    fun createCommand() = CreateScheduleCommand(
        title = "팀 회의",
        startTime = future,
        endTime = future.plusHours(1),
    )

    beforeEach { clearAllMocks() }

    context("create") {
        test("충돌 없으면 저장 성공") {
            val saved = sampleSchedule()
            every { loadPort.findAll() } returns emptyList()
            every { savePort.save(any()) } returns saved

            val result = service.create(createCommand())

            result.title shouldBe "팀 회의"
            verify { savePort.save(any()) }
        }

        test("시간이 겹치는 일정이 있으면 ScheduleConflictException") {
            val existing = sampleSchedule(UUID.randomUUID())
            every { loadPort.findAll() } returns listOf(existing)

            shouldThrow<ScheduleConflictException> {
                service.create(createCommand())
            }
        }
    }

    context("getById") {
        test("존재하는 일정 반환") {
            every { loadPort.findById(scheduleId) } returns sampleSchedule()

            val result = service.getById(scheduleId)
            result.id shouldBe scheduleId
        }

        test("존재하지 않으면 ScheduleNotFoundException") {
            every { loadPort.findById(scheduleId) } returns null

            shouldThrow<ScheduleNotFoundException> {
                service.getById(scheduleId)
            }
        }
    }

    context("update") {
        test("정상 수정") {
            val updated = sampleSchedule()
            every { loadPort.findById(scheduleId) } returns sampleSchedule()
            every { loadPort.findAll() } returns emptyList()
            every { savePort.save(any()) } returns updated

            val result = service.update(
                UpdateScheduleCommand(
                    id = scheduleId,
                    title = "팀 회의",
                    startTime = future,
                    endTime = future.plusHours(1),
                )
            )
            result.id shouldBe scheduleId
        }

        test("존재하지 않는 일정 수정 시 ScheduleNotFoundException") {
            every { loadPort.findById(scheduleId) } returns null

            shouldThrow<ScheduleNotFoundException> {
                service.update(
                    UpdateScheduleCommand(
                        id = scheduleId,
                        title = "팀 회의",
                        startTime = future,
                        endTime = future.plusHours(1),
                    )
                )
            }
        }
    }

    context("delete") {
        test("정상 삭제") {
            every { loadPort.findById(scheduleId) } returns sampleSchedule()
            every { deletePort.delete(scheduleId) } returns Unit

            service.delete(scheduleId)
            verify { deletePort.delete(scheduleId) }
        }

        test("존재하지 않는 일정 삭제 시 ScheduleNotFoundException") {
            every { loadPort.findById(scheduleId) } returns null

            shouldThrow<ScheduleNotFoundException> {
                service.delete(scheduleId)
            }
        }
    }

    context("getConflicts") {
        test("충돌 일정 반환") {
            val a = Schedule(title = "A", startTime = future, endTime = future.plusHours(2))
            val b = Schedule(title = "B", startTime = future.plusHours(1), endTime = future.plusHours(3))
            every { loadPort.findAll() } returns listOf(a, b)

            val conflicts = service.getConflicts()
            conflicts.size shouldBe 2
        }

        test("충돌 없으면 빈 리스트") {
            val a = Schedule(title = "A", startTime = future, endTime = future.plusHours(1))
            val b = Schedule(title = "B", startTime = future.plusHours(2), endTime = future.plusHours(3))
            every { loadPort.findAll() } returns listOf(a, b)

            service.getConflicts() shouldBe emptyList()
        }
    }
})
