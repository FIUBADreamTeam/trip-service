package com.fdt.tripservice.domain.trip

import java.time.LocalDate

interface TripRepository {
    fun save(trip: Trip): Trip
    fun findById(id: String): Trip
    fun findNearByAndDepartureAt(departure: Location, arrival: Location, departureAt: LocalDate): List<Trip>
    fun deleteAll()
}
