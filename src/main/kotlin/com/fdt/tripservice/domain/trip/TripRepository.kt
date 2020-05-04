package com.fdt.tripservice.domain.trip

interface TripRepository {
    fun save(trip: Trip): Trip
    fun findById(id: String): Trip
    fun findNearBy(departure: Location, arrival: Location): List<Trip>
    fun deleteAll()
}
