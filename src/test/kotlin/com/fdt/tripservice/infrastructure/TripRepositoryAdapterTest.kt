package com.fdt.tripservice.infrastructure

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.exception.TripNotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.testcontainers.containers.GenericContainer
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate

@Testcontainers
@SpringBootTest
class TripRepositoryAdapterTest {

    companion object {
        @Container
        private val container = MyElasticsearchContainer()
    }

    @Autowired
    private lateinit var elasticsearchTemplate: ElasticsearchTemplate

    @Autowired
    private lateinit var tripRepository: TripRepository

    @BeforeEach
    fun setup() {
        recreateIndex()
    }

    @Test
    fun `when try to find by id a not existing trip then must fail`() {
        assertThrows<TripNotFoundException> {
            tripRepository.findById(1)
        }
    }

    @Test
    fun `can save an retrieve a trip`() {
        val trip = givenAnyTrip()
        val saved = tripRepository.save(trip)
        val savedTrip = tripRepository.findById(saved.id!!)

        assertEquals(trip.departure, savedTrip.departure)
    }

    @Test
    fun `can find trip near by`() {
        val trip = givenAnyTrip()
        val savedTrip = tripRepository.save(trip)
        assertEquals(
                savedTrip,
                tripRepository.findNearBy(savedTrip.departure).first()
        )
    }

    @Test
    fun `out of range`() {
        val trip = givenAnyTrip()
        val savedTrip = tripRepository.save(trip)
        assertEquals(
                emptyList<Trip>(),
                tripRepository.findNearBy(savedTrip.arrival)
        )
    }

    private fun givenAnyTrip(): Trip {
        val departure = Location(0L, 0L)
        val arrival = Location(1L, 1L)
        return Trip(0L, departure, arrival, LocalDate.now(), listOf(), 1L, 1)
    }

    private fun recreateIndex() {
        if (elasticsearchTemplate.indexExists(Trip::class.java)) {
            println("Deleting index ...")
            elasticsearchTemplate.deleteIndex(Trip::class.java)
        }
        println("Creating index ...")
        elasticsearchTemplate.createIndex(Trip::class.java)
        elasticsearchTemplate.refresh(Trip::class.java)
        elasticsearchTemplate.putMapping(Trip::class.java)

        println("SETTINGS ${elasticsearchTemplate.getSetting(Trip::class.java)}")
        println("GET MAPPING ${elasticsearchTemplate.getMapping(Trip::class.java)}")
    }
}

class MyElasticsearchContainer : ElasticsearchContainer() {
    init {
        addFixedExposedPort(9300, 9300)
        addEnv("cluster.name","elasticsearch")
    }
}
