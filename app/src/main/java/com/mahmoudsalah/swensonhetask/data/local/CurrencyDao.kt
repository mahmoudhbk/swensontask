package com.base.medicalgate.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mahmoudsalah.swansontask.entities.response.Currency
import com.mahmoudsalah.swansontask.entities.response.ParaResponseModel

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies")
    fun getAllCurrencies() : LiveData<Currency>

    @Query("SELECT * FROM currencies WHERE id = :id")
    fun getCurrency(id: Int): LiveData<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currency: List<Currency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currency: Currency)


}