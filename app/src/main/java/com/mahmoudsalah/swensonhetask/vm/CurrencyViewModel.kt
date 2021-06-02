package com.base.medicalgate.ui.activties

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.base.medicalgate.data.repository.CurrencyRepository
import com.mahmoudsalah.swansontask.entities.request.currencies.ParaSendModel
import com.mahmoudsalah.swansontask.entities.response.ParaResponseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*

class CurrencyViewModel
@ViewModelInject constructor(private val repository: CurrencyRepository) : ViewModel() {

    //val logins = repository.login()

    var updateEvent = SingleLiveEvent<Response<ParaResponseModel>>()

    fun loacCurrencies(
        sendParaCurrency: ParaSendModel,
        headers: HashMap<String, Any>?,
        _updateEvent : SingleLiveEvent<Response<ParaResponseModel>>) {

        updateEvent = _updateEvent
        CoroutineScope(Dispatchers.IO).launch {
            repository.loadCurrencies("834799350620af86f12ac1b36958693c", "1", updateEvent)
        }
    }

}