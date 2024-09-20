package tars.playground.driver_activity_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@SpringBootApplication
//@EnableCassandraRepositories(basePackages = ["tars.playground.driver_activity_demo.entity"])
class DriverActivityDemoApplication

fun main(args: Array<String>) {
    runApplication<DriverActivityDemoApplication>(*args)
}
