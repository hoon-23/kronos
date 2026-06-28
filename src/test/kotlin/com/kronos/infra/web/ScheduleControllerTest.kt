package com.kronos.infra.web

import com.kronos.application.schedule.port.inbound.CreateScheduleUseCase
import com.kronos.application.schedule.port.inbound.DeleteScheduleUseCase
import com.kronos.application.schedule.port.inbound.GetScheduleUseCase
import com.kronos.application.schedule.port.inbound.ParseNaturalLanguageUseCase
import com.kronos.application.schedule.port.inbound.UpdateScheduleUseCase
import com.kronos.domain.schedule.Schedule
import com.kronos.domain.schedule.ScheduleConflictException
import com.kronos.domain.schedule.ScheduleNotFoundException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import java.time.LocalDateTime
import java.util.UUID

@WebMvcTest(ScheduleController::class)
class ScheduleControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    @MockitoBean lateinit var createUseCase: CreateScheduleUseCase
    @MockitoBean lateinit var getUseCase: GetScheduleUseCase
    @MockitoBean lateinit var updateUseCase: UpdateScheduleUseCase
    @MockitoBean lateinit var deleteUseCase: DeleteScheduleUseCase
    @MockitoBean lateinit var nlpUseCase: ParseNaturalLanguageUseCase

    private val future = LocalDateTime.now().plusDays(1)

    private fun sampleSchedule(id: UUID = UUID.randomUUID()) = Schedule(
        id = id,
        title = "팀 회의",
        startTime = future,
        endTime = future.plusHours(1),
    )

    @Test
    fun `POST schedules - 일정 생성 성공`() {
        val schedule = sampleSchedule()
        whenever(createUseCase.create(any())).thenReturn(schedule)

        val body = mapOf(
            "title" to "팀 회의",
            "start_time" to future.toString(),
            "end_time" to future.plusHours(1).toString(),
        )

        mockMvc.post("/api/v1/schedules") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.title") { value("팀 회의") }
        }
    }

    @Test
    fun `POST schedules - 충돌 시 409 반환`() {
        whenever(createUseCase.create(any()))
            .doThrow(ScheduleConflictException("팀 회의", "스탠드업"))

        val body = mapOf(
            "title" to "팀 회의",
            "start_time" to future.toString(),
            "end_time" to future.plusHours(1).toString(),
        )

        mockMvc.post("/api/v1/schedules") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andExpect {
            status { isConflict() }
        }
    }

    @Test
    fun `GET schedules-{id} - 존재하는 일정 반환`() {
        val id = UUID.randomUUID()
        whenever(getUseCase.getById(id)).thenReturn(sampleSchedule(id))

        mockMvc.get("/api/v1/schedules/$id")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(id.toString()) }
            }
    }

    @Test
    fun `GET schedules-{id} - 없는 일정이면 404 반환`() {
        val id = UUID.randomUUID()
        whenever(getUseCase.getById(id)).doThrow(ScheduleNotFoundException(id))

        mockMvc.get("/api/v1/schedules/$id")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `DELETE schedules-{id} - 정상 삭제`() {
        val id = UUID.randomUUID()

        mockMvc.delete("/api/v1/schedules/$id")
            .andExpect {
                status { isNoContent() }
            }
    }
}
