package tars.playground.driver_activity_demo.dynamo

import java.time.LocalDateTime
import java.util.UUID


data class TruckerActivityEvent(
    val id: String,
    val entitySource: EntitySource,
    val userId: Long,
    val eventType: EventType,
    val description: String?,
    val metadata: Map<String, Any>,
    val ipAddress: String?,
    val deviceInfo: DeviceInfo?,
    val eventOccurredAt: LocalDateTime,
) {

    companion object {
        fun create(
            entitySource: EntitySource = EntitySource.CORE_SERVER,
            userId: Long,
            eventType: EventType,
            description: String? = null,
            metadata: Map<String, Any> = emptyMap(),
            ipAddress: String? = null,
            deviceInfo: DeviceInfo? = null,
            eventOccurredAt: LocalDateTime = LocalDateTime.now(),
        ) = TruckerActivityEvent(
            id = UUID.randomUUID().toString(),
            entitySource = entitySource,
            userId = userId,
            eventType = eventType,
            description = description,
            metadata = metadata,
            ipAddress = ipAddress,
            deviceInfo = deviceInfo,
            eventOccurredAt = eventOccurredAt
        )
    }

    data class DeviceInfo(
        val deviceType: DeviceType,
        val os: String,
        val appVersion: String? = null
    ) {
        enum class DeviceType {
            MOBILE, TABLET, DESKTOP, LAPTOP, SMART_TV, WEARABLE, OTHER
        }
    }

    enum class EntitySource {
        USER, // Represents a user entity
        CORE_SERVER, // Represents a core server entity
    }

    enum class EventType {
        // 이동 로그 (Movement Logs)
        DRIVER_LOCATION,
        GYRO_SENSOR,
        NETWORK_CONNECTION,

        // 오더 조회 관심 로그 (Order Interaction Logs), 시도를 저장합니다.
        ORDER_ACCEPTED_ATTEMPTED,
        ORDER_ACCEPTED_SUCCEEDED,
        ORDER_ACCEPTED_FAILED,

        ORDER_REJECTED_ATTEMPTED,
        ORDER_REJECTED_SUCCEEDED,
        ORDER_REJECTED_FAILED,

        ORDER_FILTERED_ATTEMPTED,
        ORDER_FILTERED_SUCCEEDED,
        ORDER_FILTERED_FAILED,

        ORDER_CANCELLED_ATTEMPTED,
        ORDER_CANCELLED_SUCCEEDED,
        ORDER_CANCELLED_FAILED,

        ORDER_VIEWED_ATTEMPTED,
        ORDER_VIEWED_SUCCEEDED,
        ORDER_VIEWED_FAILED,

        ORDER_ACCEPT_SPEED,
        ORDER_REJECT_SPEED,

        // 알림 상호작용 로그 (Notification Interaction Logs)
        PUSH_CLICKED,
        PUSH_IGNORED,

        // 활동 로그 (Activity Logs)
        PAGE_VIEWED,
        BUTTON_CLICKED,
        API_CALLED,
        APP_ACTIVATION, // 앱 활성화 (App open/close, stay duration)
        SCREEN_TOUCHED, // 화면 터치 / 스크롤 (Touch and scroll)

        // 커뮤니케이션 로그 (Communication Logs)
        CALL_SHIPPER,
        CALL_CUSTOMER_SUPPORT,
        CHANNEL_TALK_CLICK,

        // 시스템 오류 로그 (System Error Logs)
        BUSINESS_ERROR,
        CRASH_OCCURRENCE,

        // 기타 (Miscellaneous)
        TEMPERATURE_SENSOR,
        BREAK_TIME
    }
}
