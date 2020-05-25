package com.fdt.tripservice.domain.usecases

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import java.time.LocalDate

class SearchTrip(private val tripRepository: TripRepository) {

    fun execute(departure: Location, arrival: Location, departureAt: LocalDate): List<Trip> {
        return tripRepository.findNearByAndDepartureAt(departure, arrival, departureAt)
    }
}