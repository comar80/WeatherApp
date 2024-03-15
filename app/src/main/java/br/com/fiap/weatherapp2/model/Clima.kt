package br.com.fiap.weatherapp2.model

import com.google.gson.annotations.SerializedName

data class Clima(
    val location: Location? = null,
    val current: Current? = null,
    val forecast: Forecast? = null,
    val alerts: Alerts? = null
)

data class Location(
    val name: String
)

data class Current (
    @SerializedName("temp_c") val temp: Double,
    val condition: Condition,
    @SerializedName("wind_kph") val wind: Double,
    val humidity: Int,
    @SerializedName("feelslike_c") val feelslike: Double
)

data class Condition (
    val text: String,
    val icon: String
)

data class Forecast(
    val forecastday: ArrayList<Date>
)

data class Date(
    val hour: ArrayList<Hour>
)

data class Hour(
    val time: String,
    @SerializedName("temp_c") val tempHour: String,
    @SerializedName("condition") val conditionHour: ConditionHour
)

data class ConditionHour (
    val text: String,
    val icon: String
)

data class Alerts(
    val alert: ArrayList<Alert>
)

data class Alert(
    val headline: String,
    val category: String,
    val event: String,
    val desc: String
)