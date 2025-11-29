package com.example.metrogrid.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartureResult(
    val departures: List<Departure>,
    val realtimeDataUpdatedAt: Long

)

@Serializable
data class Departure(
    val tripId: String,
    val stop: Stop,
    @SerialName("when")
    val whenTime: String?,               // formerly "when"
    val plannedWhen: String?,
    val prognosedWhen: String? = null,
    val delay: Int?,
    val platform: String?,
    val plannedPlatform: String?,
    val prognosedPlatform: String? = null,
    val prognosisType: String? = null,
    val direction: String,
    val provenance: String? = null,
    val line: Line,
    val remarks: List<Remark>,
    val origin: Stop? = null,
    val destination: Stop,
    val cancelled: Boolean? = null,
    val occupancy: String? = null,
    val currentTripPosition: DepartureLocation? = null
)

@Serializable
data class Stop(
    val type: String,
    val id: String,
    val name: String,
    val location: DepartureLocation,
    val products: Products
)

@Serializable
data class DepartureLocation(
    val type: String? = null,
    val id: String? = null,
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class Products(
    val suburban: Boolean,
    val subway: Boolean,
    val tram: Boolean,
    val bus: Boolean,
    val ferry: Boolean,
    val express: Boolean,
    val regional: Boolean
)

@Serializable
data class Line(
    val type: String,
    val id: String,
    val fahrtNr: String,
    val name: String,

    @SerialName("public")
    val isPublic: Boolean,   // "public" isn't a keyword in Kotlin, but recommended to rename

    val adminCode: String,
    val productName: String,
    val mode: String,
    val product: String,
    val operator: Operator
)

@Serializable
data class Operator(
    val type: String,
    val id: String,
    val name: String
)

@Serializable
data class Remark(
    val type: String,
    val code: String,
    val text: String
)
