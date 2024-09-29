package tars.playground.driver_activity_demo.dynamo

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
@Profile("dynamo")
class DynamoDbConfig(
    @Value("\${DYNAMO_URI:}") private val dynamoDbEndpoint: String
) {

    @Bean
    fun dynamoDbClient(): DynamoDbClient {
        val builder = DynamoDbClient.builder()
            .endpointOverride(URI.create("http://localhost:8000"))
            .region(Region.AP_NORTHEAST_2) // Can be any region
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("accesskeyid", "secretaccesskey")
                )
            )

//        if (dynamoDbEndpoint.isNotBlank()) {
//            println("Using custom endpoint: $dynamoDbEndpoint")
//            builder.endpointOverride(URI.create(dynamoDbEndpoint))
//        }

        return builder.build()
    }

    @Bean
    fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }

//    @Bean
//    fun initializeTable(enhancedClient: DynamoDbEnhancedClient) = CommandLineRunner {
//        val tableName = "Users" // Define your table name
//        val table = enhancedClient.table(tableName, TableSchema.fromBean(TruckerActivityEventDynamoEntity::class.java))
//        val describeResponse = table.describeTable()
//        if (describeResponse.table() == null) {
//            table.createTable()
//            println("Table $tableName created successfully.")
//        } else {
//            println("Table $tableName already exists.")
//        }
//    }
}