package tars.playground.driver_activity_demo.entity

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.CassandraType
import java.time.LocalDateTime
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Table("user_activity_log")
data class UserActivityEvent(
    @PrimaryKeyColumn
    val id: String= "",

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    val userId: String,

    @PrimaryKeyColumn(name = "entity_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    val entityId: String,

    @PrimaryKeyColumn(name = "entity_type", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    val entityType: EntityType,

    val eventType: EventType,
    val description: String,

    // Cassandra collections can be defined as sets or maps
    val metadata: String,  // Store metadata as a JSON string

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = [CassandraType.Name.DOUBLE])
    val coordinates: List<Double>,

    val ipAddress: String,

    val deviceInfo: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val userRole: String
) {

    data class DeviceInfo(
        val deviceType: DeviceType,
        val os: String,
        val appVersion: String? = null
    ) {
        enum class DeviceType {
            MOBILE, TABLET, DESKTOP, LAPTOP, SMART_TV, WEARABLE, OTHER
        }
    }

    enum class EntityType {
        USER, POST, COMMENT, FILE, PRODUCT, ORDER, TRANSACTION, NOTIFICATION,
        MESSAGE, GROUP, EVENT, ARTICLE, REVIEW, LOCATION, TASK
    }

    enum class EventType {
        LOGIN, LOGOUT, VIEW, CREATE, UPDATE, DELETE, DOWNLOAD, UPLOAD, SHARE,
        COMMENT, LIKE, DISLIKE, REGISTER, PASSWORD_CHANGE
    }
}
//
@Repository
interface UserActivityEventRepository : CassandraRepository<UserActivityEvent, String>