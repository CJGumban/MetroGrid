package com.example.metrogrid.data.api.model

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "stations")
data class Station(
    @PrimaryKey
    val id: String,
    val location: Location? = Location(
        1.1,1.1,""
    ),
    @SerializedName("name")
    val name: String? = "",
    val type: String? = "",
    val weight: Double? = 0.0
){
    fun valid() = location != null && name != null && type != null && weight != null
}
@Serializable
data class Location(
    val latitude: Double? =1.1,
    val longitude: Double? = 1.1,
    val type: String? = "",
)