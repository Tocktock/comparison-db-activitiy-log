package tars.playground.driver_activity_demo.dynamo

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
data class User(
    @get:DynamoDbPartitionKey
    var id: String? = null,
    var name: String? = null,
    var email: String? = null
)


@Repository
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
