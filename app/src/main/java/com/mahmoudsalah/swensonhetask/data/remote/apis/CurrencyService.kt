package com.base.medicalgate.data.remote.apis

import com.mahmoudsalah.swansontask.entities.response.ParaResponseModel
import retrofit2.Response
import retrofit2.http.*

interface CurrencyService {

    //@POST("auth/login")
    @POST("/api/latest")
    suspend fun loadCurrencies(@Query("access_key") access_key: String,
                               @Query("format") format: String) : Response<ParaResponseModel>

    @POST("/api/latest")
    suspend fun loadCurrencies() : Response<ParaResponseModel>

    @POST("/api/latest")
    suspend fun getCurrency(id:Int) : Response<ParaResponseModel>
}