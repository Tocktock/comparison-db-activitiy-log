package tars.playground.driver_activity_demo

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import tars.playground.driver_activity_demo.entity.UserActivityEvent
import tars.playground.driver_activity_demo.entity.UserActivityEventRepository
import kotlin.random.Random

@RestController
class UserActivityLogController(
    private val userActivityEventRepository: UserActivityEventRepository,
    private val objectMapper: ObjectMapper
) {

    @PostMapping("/user-activity-log/insert")
    fun logUserActivity() {
        // random data
        val userActivityEvent = UserActivityEvent(
            userId = Random.nextInt(1, 1001).toString(),
            entityId = "entity-id",
            entityType = UserActivityEvent.EntityType.entries.let {
                it.elementAt(Random.nextInt(it.size))
            },
            eventType = UserActivityEvent.EventType.entries.let {
                it.elementAt(Random.nextInt(it.size))
            },
            description = "description",
            metadata = getRandomMetadata(),
            coordinates = listOf(1.0, 2.0),
            ipAddress = "${Random.nextInt(0, 256)}.${Random.nextInt(0, 256)}.${Random.nextInt(0, 256)}.${
                Random.nextInt(
                    0,
                    256
                )
            }",
            deviceInfo = objectMapper.writeValueAsString(UserActivityEvent.DeviceInfo(deviceType = UserActivityEvent.DeviceInfo.DeviceType.entries.let {
                it.elementAt(Random.nextInt(it.size))
            }, os = "os")),
            userRole = "user-role"
        )

        userActivityEventRepository.save(userActivityEvent)
    }


    fun getRandomMetadata(): String {
        // Template 1: User Activity Metadata
        val template1 = mapOf(
            "browser" to getRandomBrowser(),
            "os" to getRandomOS(),
            "screenResolution" to getRandomResolution(),
            "sessionTime" to Random.nextInt(1, 3600) // Time in seconds
        )

        // Template 2: File Metadata
        val template2 = mapOf(
            "fileName" to getRandomFileName(),
            "fileSize" to Random.nextInt(100, 10000), // Size in KB
            "fileType" to getRandomFileType(),
            "uploadTime" to System.currentTimeMillis()
        )

        // Template 3: Location Metadata
        val template3 = mapOf(
            "latitude" to Random.nextDouble(-90.0, 90.0),
            "longitude" to Random.nextDouble(-180.0, 180.0),
            "altitude" to Random.nextDouble(0.0, 10000.0),
            "locationAccuracy" to Random.nextInt(1, 100) // Accuracy in meters
        )

        // Template 4: E-commerce Metadata
        val template4 = mapOf(
            "productId" to Random.nextInt(1000, 9999),
            "quantity" to Random.nextInt(1, 100),
            "price" to Random.nextDouble(5.0, 500.0),
            "category" to getRandomCategory()
        )

        // Template 5: Social Media Metadata
        val template5 = mapOf(
            "postId" to Random.nextInt(1000, 9999),
            "likes" to Random.nextInt(0, 1000),
            "shares" to Random.nextInt(0, 500),
            "comments" to Random.nextInt(0, 100),
            "platform" to getRandomPlatform()
        )

        // List of templates
        val templates = listOf(template1, template2, template3, template4, template5)

        // Randomly pick one of the templates
        val selectedTemplate = templates[Random.nextInt(templates.size)]

        // Serialize the selected template to a JSON string
        return objectMapper.writeValueAsString(selectedTemplate)
    }

// Helper functions to generate random values for metadata fields

    fun getRandomBrowser(): String {
        val browsers = listOf("Chrome", "Firefox", "Safari", "Edge", "Opera")
        return browsers[Random.nextInt(browsers.size)]
    }

    fun getRandomOS(): String {
        val osList = listOf("Windows", "macOS", "Linux", "Android", "iOS")
        return osList[Random.nextInt(osList.size)]
    }

    fun getRandomResolution(): String {
        val resolutions = listOf("1920x1080", "1366x768", "1280x720", "1440x900", "1600x900")
        return resolutions[Random.nextInt(resolutions.size)]
    }

    fun getRandomFileName(): String {
        val names = listOf("document", "image", "video", "presentation", "spreadsheet")
        return "${names[Random.nextInt(names.size)]}_${Random.nextInt(1000, 9999)}.txt"
    }

    fun getRandomFileType(): String {
        val types = listOf("txt", "pdf", "docx", "xlsx", "png", "jpg")
        return types[Random.nextInt(types.size)]
    }

    fun getRandomCategory(): String {
        val categories = listOf("Electronics", "Clothing", "Books", "Home & Kitchen", "Beauty")
        return categories[Random.nextInt(categories.size)]
    }

    fun getRandomPlatform(): String {
        val platforms = listOf("Facebook", "Twitter", "Instagram", "LinkedIn", "Snapchat")
        return platforms[Random.nextInt(platforms.size)]
    }


}