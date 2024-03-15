package br.com.fiap.weatherapp2.service

import br.com.fiap.weatherapp2.model.Clima
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClimaService {

    //https://api.weatherapi.com/v1/forecast.json?key=a1a3f7af6c0c465a8e0170631241403&q=sao%20paulo&days=1&aqi=no&alerts=yes&lang=pt
    @GET("forecast.json?key=a1a3f7af6c0c465a8e0170631241403&days=1&aqi=no&alerts=yes&lang=pt")
    fun getClima(@Query("q") cidade: String): Call<Clima>

}