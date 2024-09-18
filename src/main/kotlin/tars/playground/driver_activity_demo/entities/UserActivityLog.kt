package tars.playground.driver_activity_demo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Document(collection = "user_activity_events")
@CompoundIndex(
    def = "{'userId': 1, 'createdAt': -1}",
    name = "idx_user_id_created_at"
)
@CompoundIndex(
    def = "{'entityId': 1, 'entityType': 1, 'createdAt': -1}",
    name = "idx_entity_id_entity_type_created_at"
)
data class UserActivityEvent(
    @Id
    val id: String? = null,

    @Indexed(name = "idx_user_id")
    val userId: String,

    @Indexed(name = "idx_entity_id_entity_type")
    val entityId: String,
    val entityType: String,

    @Indexed(name = "idx_event_type")
    val eventType: String,
    val description: String,
    val metadata: Map<String, Any>,

    val location: Location?,
    val ipAddress: String,

    val deviceInfo: DeviceInfo,

    @Indexed(name = "idx_created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val userRole: String
) {

    data class Location(
        val coordinates: List<Double>
    )

    data class DeviceInfo(
        val deviceType: String,
        val os: String,
        val appVersion: String? = null
    )
}

// make repository
@Repository
interface UserActivityEventRepository : MongoRepository<UserActivityEvent, String> {
    fun findByUserId(userId: String): List<UserActivityEvent>
    fun findByUserIdAndCreatedAtBetween(userId: String, start: LocalDateTime, end: LocalDateTime): List<UserActivityEvent>
    fun findByUserIdAndEntityType(userId: String, entityType: String): List<UserActivityEvent>
    fun findByUserIdAndEntityTypeAndCreatedAtBetween(userId: String, entityType: String, start: LocalDateTime, end: LocalDateTime): List<UserActivityEvent>
    fun findByUserIdAndEntityTypeAndEventType(userId: String, entityType: String, eventType: String): List<UserActivityEvent>
    fun findByUserIdAndEntityTypeAndEventTypeAndCreatedAtBetween(userId: String, entityType: String, eventType: String, start: LocalDateTime, end: LocalDateTime): List<UserActivityEvent>
}
