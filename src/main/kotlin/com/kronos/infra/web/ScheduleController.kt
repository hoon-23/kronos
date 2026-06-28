package com.kronos.infra.web

import com.kronos.application.schedule.port.inbound.CreateScheduleUseCase
import com.kronos.application.schedule.port.inbound.DeleteScheduleUseCase
import com.kronos.application.schedule.port.inbound.GetScheduleUseCase
import com.kronos.application.schedule.port.inbound.ParseNaturalLanguageUseCase
import com.kronos.application.schedule.port.inbound.UpdateScheduleCommand
import com.kronos.application.schedule.port.inbound.UpdateScheduleUseCase
import com.kronos.infra.web.dto.CreateScheduleRequest
import com.kronos.infra.web.dto.NaturalLanguageRequest
import com.kronos.infra.web.dto.ScheduleResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/schedules")
@Tag(name = "Schedule", description = "일정 관리 API")
class ScheduleController(
    private val createScheduleUseCase: CreateScheduleUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
    private val parseNaturalLanguageUseCase: ParseNaturalLanguageUseCase,
) {

    @Operation(summary = "자연어로 일정 등록", description = "Claude AI가 자연어 텍스트를 분석해 일정을 생성합니다.")
    @PostMapping("/natural")
    @ResponseStatus(HttpStatus.CREATED)
    fun createFromNaturalLanguage(@Valid @RequestBody request: NaturalLanguageRequest): ScheduleResponse =
        ScheduleResponse.from(parseNaturalLanguageUseCase.createFromNaturalLanguage(request.text))

    @Operation(summary = "일정 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateScheduleRequest): ScheduleResponse =
        ScheduleResponse.from(createScheduleUseCase.create(request.toCommand()))

    @Operation(summary = "전체 일정 조회")
    @GetMapping
    fun getAll(): List<ScheduleResponse> =
        getScheduleUseCase.getAll().map(ScheduleResponse::from)

    @Operation(summary = "일정 단건 조회")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ScheduleResponse =
        ScheduleResponse.from(getScheduleUseCase.getById(id))

    @Operation(summary = "충돌 일정 목록 조회", description = "시간이 겹치는 일정 목록을 반환합니다.")
    @GetMapping("/conflicts")
    fun getConflicts(): List<ScheduleResponse> =
        getScheduleUseCase.getConflicts().map(ScheduleResponse::from)

    @Operation(summary = "일정 수정")
    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: CreateScheduleRequest): ScheduleResponse {
        val command = request.toCommand()
        return ScheduleResponse.from(
            updateScheduleUseCase.update(
                UpdateScheduleCommand(
                    id = id,
                    title = command.title,
                    description = command.description,
                    startTime = command.startTime,
                    endTime = command.endTime,
                    participants = command.participants,
                )
            )
        )
    }

    @Operation(summary = "일정 삭제")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) =
        deleteScheduleUseCase.delete(id)
}
