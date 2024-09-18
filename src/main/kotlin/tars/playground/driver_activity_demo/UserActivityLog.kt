package tars.playground.driver_activity_demo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Document(collection = "userActivityLog")
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
    val entityType: EntityType,

    @Indexed(name = "idx_event_type")
    val eventType: EventType,
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
        val deviceType: DeviceType,
        val os: String,
        val appVersion: String? = null
    ) {
        enum class DeviceType {
            MOBILE, TABLET, DESKTOP, LAPTOP, SMART_TV, WEARABLE, OTHER
        }
    }

    enum class EntityType {
        USER,               // Represents a user entity
        POST,               // Represents a post or content created by users
        COMMENT,            // Represents comments made on posts or other entities
        FILE,               // Represents a file, document, or other media
        PRODUCT,            // Represents a product in an e-commerce system
        ORDER,              // Represents an order placed by the user
        TRANSACTION,        // Represents a financial transaction
        NOTIFICATION,       // Represents a notification sent to a user
        MESSAGE,            // Represents a direct message between users
        GROUP,              // Represents a group or team entity
        EVENT,              // Represents an event in the system
        ARTICLE,            // Represents articles or blog entries
        REVIEW,             // Represents a user review on a product or service
        LOCATION,           // Represents a location entity (e.g., store or branch)
        TASK                // Represents a task or to-do item in a project
    }


    enum class EventType {
        LOGIN,          // When a user logs into the system
        LOGOUT,         // When a user logs out of the system
        VIEW,           // When a user views an entity
        CREATE,         // When a user creates an entity
        UPDATE,         // When a user updates an entity
        DELETE,         // When a user deletes an entity
        DOWNLOAD,       // When a user downloads a resource
        UPLOAD,         // When a user uploads a file or resource
        SHARE,          // When a user shares an entity
        COMMENT,        // When a user comments on an entity
        LIKE,           // When a user likes an entity
        DISLIKE,        // When a user dislikes an entity
        REGISTER,       // When a user registers a new account
        PASSWORD_CHANGE // When a user changes their password
    }

}

// make repository
@Repository
interface UserActivityEventRepository : MongoRepository<UserActivityEvent, String> {
    fun findByUserId(userId: String): List<UserActivityEvent>
    fun findByUserIdAndCreatedAtBetween(
        userId: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): List<UserActivityEvent>

    fun findByUserIdAndEntityType(userId: String, entityType: String): List<UserActivityEvent>
    fun findByUserIdAndEntityTypeAndCreatedAtBetween(
        userId: String,
        entityType: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): List<UserActivityEvent>

    fun findByUserIdAndEntityTypeAndEventType(
        userId: String,
        entityType: String,
        eventType: String
    ): List<UserActivityEvent>

    fun findByUserIdAndEntityTypeAndEventTypeAndCreatedAtBetween(
        userId: String,
        entityType: String,
        eventType: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): List<UserActivityEvent>
}
