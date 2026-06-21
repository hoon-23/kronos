package com.kronos.infra.ai

import com.kronos.application.schedule.port.inbound.CreateScheduleCommand
import com.kronos.application.schedule.port.outbound.NaturalLanguageParsePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import tools.jackson.databind.ObjectMapper
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ClaudeAdapter(
    @Value("\${claude.api.key}") private val apiKey: String,
    @Value("\${claude.api.model}") private val model: String,
    private val objectMapper: ObjectMapper,
) : NaturalLanguageParsePort {

    private val restClient = RestClient.builder()
        .baseUrl("https://api.anthropic.com")
        .build()

    // 자연어 텍스트를 Claude API에 전달하여 일정 정보(제목, 시간, 참여자)로 파싱
    override fun parse(text: String): CreateScheduleCommand {
        val today = LocalDate.now()
        val prompt = buildPrompt(text, today)

        val requestBody = mapOf(
            "model" to model,
            "max_tokens" to 1024,
            "messages" to listOf(mapOf("role" to "user", "content" to prompt)),
        )

        val response = restClient.post()
            .uri("/v1/messages")
            .header("x-api-key", apiKey)
            .header("anthropic-version", "2023-06-01")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(ClaudeResponse::class.java)
            ?: throw IllegalStateException("Claude API 응답이 비어있습니다")

        val json = response.content.first().text
        return parseResponse(json)
    }

    // Claude에게 전달할 시스템 프롬프트 구성
    private fun buildPrompt(text: String, today: LocalDate): String = """
        오늘 날짜: $today

        아래 자연어 텍스트를 분석해서 일정 정보를 JSON으로 추출해줘.
        반드시 아래 JSON 형식만 응답하고, 다른 텍스트는 포함하지 마.

        {
          "title": "일정 제목",
          "description": "일정 설명 (없으면 null)",
          "start_time": "yyyy-MM-ddTHH:mm:ss",
          "end_time": "yyyy-MM-ddTHH:mm:ss",
          "participants": ["참여자1", "참여자2"]
        }

        규칙:
        - endTime이 명시되지 않으면 startTime + 1시간으로 설정
        - participants가 없으면 빈 배열
        - "다음주 화요일" 같은 상대 날짜는 오늘 기준으로 계산

        텍스트: $text
    """.trimIndent()

    // Claude 응답 JSON을 CreateScheduleCommand로 변환
    private fun parseResponse(json: String): CreateScheduleCommand {
        val parsed = objectMapper.readValue(json, ParsedSchedule::class.java)
        return CreateScheduleCommand(
            title = parsed.title,
            description = parsed.description,
            startTime = LocalDateTime.parse(parsed.startTime),
            endTime = LocalDateTime.parse(parsed.endTime),
            participants = parsed.participants,
        )
    }
}

// Claude가 반환하는 파싱된 일정 JSON 구조
data class ParsedSchedule(
    val title: String,
    val description: String?,
    val startTime: String,
    val endTime: String,
    val participants: List<String> = emptyList(),
)

// Claude Messages API 응답 구조
data class ClaudeResponse(
    val content: List<ContentBlock>,
)

data class ContentBlock(
    val type: String,
    val text: String,
)
