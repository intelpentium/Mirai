package fathurrahman.projeku.mirai.services.rest

import androidx.annotation.Keep
import fathurrahman.projeku.mirai.services.Response
import fathurrahman.projeku.mirai.services.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MainRest {

    //weather
    @GET("weather")
    suspend fun weather(
        @Query("q") q: String,
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String? = "id",
    ) : ResponseWeather

    //weather hour
    @GET("forecast")
    suspend fun weatherHour(
        @Query("q") q: String,
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String? = "id",
        @Query("cnt") cnt: String? = "5",
    ) : ResponseWeatherHour
}