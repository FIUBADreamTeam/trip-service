package com.fdt.tripservice.infrastructure

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Trip
import com.fdt.tripservice.domain.trip.TripRepository
import com.fdt.tripservice.domain.trip.exception.TripNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class TripRepositoryAdapter(
        private val tripRepository: ElasticsearchTripRepository
) : TripRepository {


    override fun save(trip: Trip): Trip {
        return tripRepository.save(trip)
    }

    override fun findById(id: String): Trip {
        return tripRepository.findByIdOrNull(id)
                ?: throw TripNotFoundException("Trip with id $id not exists")
    }

    // TODO agregar busqueda por departureAt
    override fun findNearByAndDepartureAt(departure: Location, arrival: Location, departureAt: LocalDate): List<Trip> {
        return tripRepository.findNearBy(departure.lat, departure.lon, arrival.lat, arrival.lon)
    }

    override fun deleteAll() {
        tripRepository.deleteAll()
    }
}
