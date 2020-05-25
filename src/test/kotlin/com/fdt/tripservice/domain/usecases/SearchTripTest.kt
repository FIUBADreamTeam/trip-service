package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDate

class SearchTripTest {

    @Mock
    private lateinit var tripRepository: TripRepository

    private lateinit var useCase: SearchTrip
    private val departureAt = LocalDate.now()
    private val departure = TripUtilsTest().anyLocation()
    private val arrival = TripUtilsTest().anyLocation()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = SearchTrip(tripRepository)
    }

    @Test
    fun `when exist some trips then should be returned`() {
        // given
        givenSomeTrips()

        // when
        val trips = useCase.execute(departure, arrival, departureAt)

        //
        tripsWereFound(trips)
    }

    @Test
    fun `when trips does not exist then should return an empty list`() {
        // given
        givenNoTrips()

        // when
        val trips = useCase.execute(departure, arrival, departureAt)

        // then
        tripsWereNotFound(trips)
    }

    private fun givenNoTrips() {
        `when`(tripRepository.findNearByAndDepartureAt(any(), any(), any()))
                .thenReturn(emptyList<Trip>())
    }

    private fun tripsWereNotFound(trips: List<Trip>) {
        assertTrue(trips.isEmpty())
    }

    private fun tripsWereFound(trips: List<Trip>) {
        assertFalse(trips.isEmpty())
    }

    private fun givenSomeTrips() {
        `when`(tripRepository.findNearByAndDepartureAt(any(), any(), any()))
                .thenReturn(listOf(TripUtilsTest().anyTrip()))
    }

}