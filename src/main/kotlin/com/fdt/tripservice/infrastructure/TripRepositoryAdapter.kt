package com.fdt.tripservice.infrastructure

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.exception.TripNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TripRepositoryAdapter(
        private val tripRepository: ElasticsearchTripRepository
) : TripRepository {
    override fun save(trip: Trip): Trip =
        tripRepository.save(trip)

    override fun findById(id: Long): Trip =
        tripRepository.findByIdOrNull(id) ?: throw TripNotFoundException("Trip with id $id not exists")

    override fun findNearBy(location: Location): List<Trip> =
        tripRepository.findNearBy(location.lat, location.lon)
}
