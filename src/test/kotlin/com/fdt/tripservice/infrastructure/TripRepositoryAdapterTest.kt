package com.fdt.tripservice.infrastructure

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.exception.TripNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
    private lateinit var tripRepository: TripRepository

    @BeforeEach
    fun setup() {
        tripRepository.deleteAll()
    }

    @Test
    fun `when try to find by id a not existing trip then must fail`() {
        assertThrows<TripNotFoundException> {
            tripRepository.findById("not-existing-id")
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
                tripRepository.findNearBy(savedTrip.departure, savedTrip.arrival).first()
        )
    }

    @Test
    fun `out of range`() {
        val trip = givenAnyTrip()
        val savedTrip = tripRepository.save(trip)
        assertEquals(
                emptyList<Trip>(),
                tripRepository.findNearBy(savedTrip.departure.copy(lat = 1, lon = 1), savedTrip.arrival)
        )
    }

    private fun givenAnyTrip(): Trip {
        val departure = Location(0L, 0L)
        val arrival = Location(1L, 1L)
        return Trip(null, departure, arrival, LocalDate.now(), listOf(), 1L, 1)
    }
}

class MyElasticsearchContainer : ElasticsearchContainer() {
    init {
        addFixedExposedPort(9300, 9300)
        //addEnv("cluster.name","trip-service")
    }
}
