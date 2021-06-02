package com.mahmoudsalah.swansontask.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        /**
         * Our API Call will be intercepted here
         */
        var original: Request? = chain.request()

        // Request customization: add request headers

        // Request customization: add request headers
        val requestBuilder: Request.Builder = original!!.newBuilder()
            .header("Authorization", "auth-value") // <-- this is the important line

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}
