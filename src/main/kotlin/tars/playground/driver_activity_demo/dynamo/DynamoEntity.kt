package tars.playground.driver_activity_demo.dynamo

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.time.LocalDateTime


@DynamoDbBean
data class TruckerActivityEventDynamoEntity(
    @get:DynamoDbPartitionKey
    var id: String? = null,

    @get:DynamoDbConvertedBy(EntitySourceAttributeConverter::class)
    @get:DynamoDbAttribute("entity_source")
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["entity_event_type_idx"])
    var entitySource: TruckerActivityEvent.EntitySource? = null,

    @get:DynamoDbAttribute("user_id")
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["trucker_event_date_idx"])
    var userId: Long? = null,

    @get:DynamoDbConvertedBy(EventTypeAttributeConverter::class)
    @get:DynamoDbAttribute("event_type")
    @get:DynamoDbSecondarySortKey(indexNames = ["entity_event_type_idx"])
    var eventType: TruckerActivityEvent.EventType? = null,

    var description: String? = null,

//    var metadata: Map<String, Any> = emptyMap(),

    @get:DynamoDbAttribute("ip_address")
    var ipAddress: String? = null,

    @get:DynamoDbAttribute("device_info")
    var deviceInfo: DeviceInfo? = null,

    @get:DynamoDbConvertedBy(LocalDateTimeAttributeConverter::class)
    @get:DynamoDbAttribute("event_occurred_at")
    @get:DynamoDbSecondarySortKey(indexNames = ["trucker_event_date_idx"])
    var eventOccurredAt: LocalDateTime? = null
) {
    @DynamoDbBean
    data class DeviceInfo(
        @get:DynamoDbConvertedBy(DeviceTypeAttributeConverter::class)
        @get:DynamoDbAttribute("device_type")
        var deviceType: TruckerActivityEvent.DeviceInfo.DeviceType? = null,

        @get:DynamoDbAttribute("os")
        var os: String? = null,

        @get:DynamoDbAttribute("app_version")
        var appVersion: String? = null
    )
}


class LocalDateTimeAttributeConverter : AttributeConverter<LocalDateTime> {
    override fun transformFrom(input: LocalDateTime?): AttributeValue {
        return AttributeValue.builder().s(input?.toString()).build()
    }

    override fun transformTo(input: AttributeValue?): LocalDateTime? {
        return input?.s()?.let { LocalDateTime.parse(it) }
    }

    override fun type(): EnhancedType<LocalDateTime> {
        return EnhancedType.of(LocalDateTime::class.java)
    }

    override fun attributeValueType(): AttributeValueType {
        return AttributeValueType.S
    }
}

class EntitySourceAttributeConverter : AttributeConverter<TruckerActivityEvent.EntitySource> {
    override fun transformFrom(input: TruckerActivityEvent.EntitySource?): AttributeValue {
        return AttributeValue.builder().s(input?.name).build()
    }

    override fun transformTo(input: AttributeValue?): TruckerActivityEvent.EntitySource? {
        return input?.s()?.let { TruckerActivityEvent.EntitySource.valueOf(it) }
    }

    override fun type(): EnhancedType<TruckerActivityEvent.EntitySource> {
        return EnhancedType.of(TruckerActivityEvent.EntitySource::class.java)
    }

    override fun attributeValueType(): AttributeValueType {
        return AttributeValueType.S
    }
}

class EventTypeAttributeConverter : AttributeConverter<TruckerActivityEvent.EventType> {
    override fun transformFrom(input: TruckerActivityEvent.EventType?): AttributeValue {
        return AttributeValue.builder().s(input?.name).build()
    }

    override fun transformTo(input: AttributeValue?): TruckerActivityEvent.EventType? {
        return input?.s()?.let { TruckerActivityEvent.EventType.valueOf(it) }
    }

    override fun type(): EnhancedType<TruckerActivityEvent.EventType> {
        return EnhancedType.of(TruckerActivityEvent.EventType::class.java)
    }

    override fun attributeValueType(): AttributeValueType {
        return AttributeValueType.S
    }
}

class DeviceTypeAttributeConverter : AttributeConverter<TruckerActivityEvent.DeviceInfo.DeviceType> {
    override fun transformFrom(input: TruckerActivityEvent.DeviceInfo.DeviceType?): AttributeValue {
        return AttributeValue.builder().s(input?.name).build()
    }

    override fun transformTo(input: AttributeValue?): TruckerActivityEvent.DeviceInfo.DeviceType? {
        return input?.s()?.let { TruckerActivityEvent.DeviceInfo.DeviceType.valueOf(it) }
    }

    override fun type(): EnhancedType<TruckerActivityEvent.DeviceInfo.DeviceType> {
        return EnhancedType.of(TruckerActivityEvent.DeviceInfo.DeviceType::class.java)
    }

    override fun attributeValueType(): AttributeValueType {
        return AttributeValueType.S
    }
}

