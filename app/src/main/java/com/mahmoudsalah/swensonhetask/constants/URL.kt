package com.base.medicalgate.constants

object  URL {

    val releaseURL = "http://data.fixer.io/"
    val BASE_URL = this.releaseURL

    fun getBASEURL(): String {
        return this.BASE_URL
    }
}