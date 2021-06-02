package com.mahmoudsalah.swansontask.entities.response

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "currency")
class ParaResponseModel{
    private var success: Boolean? = null
    private var timestamp: Int? = null
    private var base: String? = null
    private var date: String? = null
    private var status: String? = null
    private var rates: Rates? = null

    fun getRates():Rates
    {
        return this!!.rates!!
    }
}
