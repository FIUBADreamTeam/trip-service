package com.fdt.tripservice.infrastructure

import com.fdt.tripservice.domain.trip.Trip
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface ElasticsearchTripRepository : ElasticsearchRepository<Trip, Long> {
    @Query("""
        {
            "bool" : {
                "must" : {
                    "match_all" : {}
                },
                "filter" : {
                    "geo_distance" : {
                        "distance" : "100m",
                        "departure" : {
                            "lat" : ?0,
                            "lon" : ?1
                        }
                    }
                }
            }
        }
    """)
    fun findNearBy(lat: Long, lon: Long): List<Trip>
}
