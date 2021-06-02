package com.base.medicalgate.di

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

class EasyHostnameVerifier : HostnameVerifier {

    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return true
    }
}