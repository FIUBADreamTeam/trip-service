package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Trip
import java.time.LocalDate

class TripUtilsTest {

    private fun locD(): Location {
        return Location(3L, 3L)
    }

    private fun locC(): Location {
        return Location(2L, 2L)
    }

    private fun locB(): Location {
        return Location(1L, 1L)
    }

    private fun locA(): Location {
        return Location(0L, 0L)
    }

    fun anyLocation(): Location {
        return Location(0L, 0L)
    }

    fun anyTrip(): Trip {
        val locA = locA()
        val locB = locB()
        val locC = locC()
        val locD = locD()
        return Trip("id", locA, locD, LocalDate.now(), listOf(locB, locC), 1L, 4)
    }
}