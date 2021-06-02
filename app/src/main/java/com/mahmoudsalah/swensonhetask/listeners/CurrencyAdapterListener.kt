package com.mahmoudsalah.swansontask.listeners

import com.mahmoudsalah.swansontask.entities.response.Rate

interface CurrencyAdapterListener {
    fun onCurrencyClicked(rate: Rate?)
}