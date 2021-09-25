package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL=Constants.BASE_URL


private val retrofit= Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL).build()


interface NasaAsteroidRadarApiService {
    @GET("neo/rest/v1/feed")
    suspend  fun fetchAsteroidsAsync(@Query("api_key") key:String, @Query("start_date") startDate:String, @Query("end_date") endDate:String): String

}
object NasaAsteroidRadarApi{
    val retrofitService : NasaAsteroidRadarApiService by lazy {
        retrofit.create(NasaAsteroidRadarApiService::class.java)
    }
}



