package com.mahmoudsalah.swansontask.contracts

import com.mahmoudsalah.swansontask.entities.response.Rates

interface CurrencyContract {
    fun setupRecyclerView()
    fun loadFromFixer()
    fun renderCurrencies(rates: Rates)
    fun warnInternetConnection()
    fun errorAuthenticationFailure(error:String)
}