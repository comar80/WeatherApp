package br.com.fiap.weatherapp2.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val URL = "https://api.weatherapi.com/v1/"

    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getClimaService(): ClimaService {
        return retrofitFactory.create(ClimaService::class.java)
    }

}