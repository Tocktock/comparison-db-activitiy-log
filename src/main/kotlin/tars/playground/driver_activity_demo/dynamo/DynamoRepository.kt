package tars.playground.driver_activity_demo.dynamo

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException

@RestController
@Profile("dynamo")
class TruckerActivityEventDynamoRepositoryTest(
    private val dynamoDbEnhancedClient: DynamoDbEnhancedClient
) {
    private val table = dynamoDbEnhancedClient.table("User", TableSchema.fromBean(User::class.java))

    init {
        // Create table if it doesn't exist
        try {
            table.describeTable()
        } catch (e: ResourceNotFoundException) {
            table.createTable()
        }
    }

    @GetMapping("/testa")
    fun test() {
        val user = User(
            id = "1",
            name = "John Doe",
            email = ""
        )
        table.putItem(user)
    }
}

@DynamoDbBean
@Profile("dynamo")
data class User(
    @get:DynamoDbPartitionKey
    var id: String? = null,
    var name: String? = null,
    var email: String? = null
)


@Repository
@Profile("dynamo")
class TruckerActivityEventDynamoRepository(
    private val dynamoDbEnhancedClient: DynamoDbEnhancedClient
) {

    private val tableName = "trucker_activity_events" // Ensure this matches your DynamoDB table name
    private val table: DynamoDbTable<TruckerActivityEventDynamoEntity>
        get() = dynamoDbEnhancedClient.table(
            tableName,
            TableSchema.fromBean(TruckerActivityEventDynamoEntity::class.java)
        )

    init {
        try {
            table.describeTable()
        } catch (e: ResourceNotFoundException) {
            table.createTable()
            println(table.describeTable())
        }
    }

    fun save(event: TruckerActivityEvent) {
        val entity = TruckerActivityEventDynamoEntity(
            id = event.id,
            entitySource = event.entitySource,
            userId = event.userId,
            eventType = event.eventType,
            description = event.description,
//            metadata = event.metadata,
            ipAddress = event.ipAddress,
            deviceInfo = event.deviceInfo?.let {
                TruckerActivityEventDynamoEntity.DeviceInfo(
                    deviceType = it.deviceType,
                    os = it.os,
                    appVersion = it.appVersion
                )
            },
            eventOccurredAt = event.eventOccurredAt
        )
        table.putItem(entity)
    }

    fun getAll(): List<Any> {
        return table.scan().items().toList()
    }
}

// create controller to test repository
@RestController
@Profile("dynamo")
class TruckerActivityEventController(
    private val repository: TruckerActivityEventDynamoRepository
) {

    @GetMapping("/events")
    fun getAllEvents(): List<Any> {
        return repository.getAll()
    }

    // save
    @GetMapping("/save")
    fun saveEvent(): String {
        val event = TruckerActivityEvent.create(
            userId = 1,
            eventType = TruckerActivityEvent.EventType.DRIVER_LOCATION,
            description = "Driver location updated",
            metadata = mapOf("latitude" to 37.1234, "longitude" to 127.5678),
            ipAddress = ""
        )
        repository.save(event)
        return "Event saved"
    }
}