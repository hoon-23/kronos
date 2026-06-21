package com.kronos.infra.cache

import com.kronos.domain.schedule.Schedule
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.UUID

@Component
class ScheduleCacheAdapter(
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    private val ttl = Duration.ofMinutes(10)

    fun get(id: UUID): Schedule? =
        redisTemplate.opsForValue().get(cacheKey(id)) as? Schedule

    fun put(schedule: Schedule) {
        redisTemplate.opsForValue().set(cacheKey(schedule.id), schedule, ttl)
    }

    fun evict(id: UUID) {
        redisTemplate.delete(cacheKey(id))
    }

    private fun cacheKey(id: UUID) = "schedule:$id"
}
