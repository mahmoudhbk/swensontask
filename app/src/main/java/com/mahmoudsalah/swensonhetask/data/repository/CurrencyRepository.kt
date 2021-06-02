package com.base.medicalgate.data.repository

import com.base.medicalgate.data.remote.CurrenciesRemoteDataSource
import com.base.medicalgate.data.remote.apis.CurrencyService
import com.base.medicalgate.ui.activties.SingleLiveEvent
import com.mahmoudsalah.swansontask.entities.request.currencies.ParaSendModel
import com.mahmoudsalah.swansontask.entities.response.ParaResponseModel
import retrofit2.Response
import retrofit2.Retrofit
import java.util.HashMap
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val remoteDataSource: CurrenciesRemoteDataSource,
    private val retrofit: Retrofit)
{

    suspend fun loadCurrencies(
        accessKey:String,
        format:String,
        updateEvent : SingleLiveEvent<Response<ParaResponseModel>>
    ) {
        val request = retrofit.create(CurrencyService::class.java)
        updateEvent.postValue(request.loadCurrencies(accessKey, format))
    }

}