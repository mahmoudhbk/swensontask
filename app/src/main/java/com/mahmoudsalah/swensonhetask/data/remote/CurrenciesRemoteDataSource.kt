package com.base.medicalgate.data.remote

import com.base.medicalgate.data.remote.apis.CurrencyService
import com.mahmoudsalah.swansontask.entities.request.currencies.ParaSendModel
import java.util.HashMap
import javax.inject.Inject

class CurrenciesRemoteDataSource @Inject constructor(
    private val currencyService: CurrencyService
): BaseDataSource() {

    suspend fun loadCurrencies(
        accessKey:String,
        format:String)
    {
        currencyService.loadCurrencies(accessKey, format )
    }

    suspend fun getCurrencies() = getResult { currencyService.loadCurrencies() }
    suspend fun getCurrency(id: Int) = getResult { currencyService.getCurrency(id) }

}