package com.fdt.tripservice.domain.trip

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class TripTest {
    @Test
    fun `if a trip contain two existing locations ordered, return true for hasSubtripSection`() {
        val trip = givenAnyTrip()

        assertTrue(Subtrip(locB(),locD()) in trip)
    }

    @Test
    fun `if a trip doesn't contain two locations ordered, return false for hasSubtripSection`() {
        val trip = givenAnyTrip()

        assertFalse(Subtrip(locD(),locB()) in trip)
    }

    @Test
    fun `if a trip doesn't contain one final existing location, return false for hasSubtripSection`() {
        val trip = givenAnyTrip()

        val locLikeB = locB()
        val locLikeE = Location(4L,3L)

        assertFalse(Subtrip(locB(),locLikeE) in trip)
    }

    @Test
    fun `if a trip doesn't contain one initial existing location, return false for hasSubtripSection`() {
        val trip = givenAnyTrip()

        assertFalse(Subtrip(locNotInTrip(),locB()) in trip)
    }

    @Test
    fun `if a trip doesn't contain any locations, return false for hasSubtripSection`() {
        val trip = givenAnyTrip()

        assertFalse(Subtrip(otherLocNotInTrip(),locNotInTrip()) in trip)
    }

    @Test
    fun `if a trip hasAvailableSeatAt section return true`() {
        val trip = givenAnyTrip()

        val subtripSection = Subtrip(locB(),locC())
        assertTrue(trip.hasAvailableSeatAt(subtripSection))
    }

    @Test
    fun `if a trip doesn't hasAvailableSeatAt section return false`() {
        val trip = givenAnyTrip(0)

        val subtripSection = Subtrip(locB(),locC())
        assertFalse(trip.hasAvailableSeatAt(subtripSection))
    }

    @Test
    fun `join to unique seat trip and left no seats open`() {
        val trip = givenAnyTrip(1)

        trip.joinPassengerAt(1L, Subtrip(locB(),locC()))

        assertFalse(trip.hasAvailableSeatAt(Subtrip(locA(),locD())))
    }

    private fun givenAnyTrip(capacity: Int = 4): Trip {
        val locA = locA()
        val locB = locB()
        val locC = locC()
        val locD = locD()
        return Trip(null, locA, locD, LocalDate.now(), listOf(locB, locC), 1L, capacity)
    }

    private fun locD() = Location(3L, 3L)

    private fun locC() = Location(2L, 2L)

    private fun locB() = Location(1L, 1L)

    private fun locA() = Location(0L, 0L)

    private fun locNotInTrip() = Location(4L, 3L)

    private fun otherLocNotInTrip() = Location(1L, 2L)
}