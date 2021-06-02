package com.mahmoudsalah.swansontask.utils

import androidx.annotation.Nullable


class StateData <T> {
    private var status: DataStatus? = null

    @Nullable
    private var data: T? = null

    @Nullable
    private var error: Throwable? = null

    fun StateData() {
        status = DataStatus.CREATED
        data = null
        error = null
    }

    fun loading(): StateData<T>? {
        status = DataStatus.LOADING
        data = null
        error = null
        return this
    }

    fun success(data: T): StateData<T>? {
        status = DataStatus.SUCCESS
        this.data = data
        error = null
        return this
    }

    fun error(error: Throwable): StateData<T>? {
        status = DataStatus.ERROR
        data = null
        this.error = error
        return this
    }

    fun complete(): StateData<T>? {
        status = DataStatus.COMPLETE
        return this
    }

    fun getStatus(): DataStatus? {
        return this!!.status
    }

    @Nullable
    fun getData(): T? {
        return data
    }

    @Nullable
    fun getError(): Throwable? {
        return error
    }

    enum class DataStatus {
        CREATED, SUCCESS, ERROR, LOADING, COMPLETE
    }
}