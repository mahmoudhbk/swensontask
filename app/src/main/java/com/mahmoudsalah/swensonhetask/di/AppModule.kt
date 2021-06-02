package com.base.medicalgate.di

import android.content.Context
import com.base.medicalgate.constants.URL
import com.base.medicalgate.data.local.AppDatabase
import com.base.medicalgate.data.remote.CurrenciesRemoteDataSource
import com.base.medicalgate.data.remote.apis.CurrencyService
import com.base.medicalgate.data.repository.CurrencyRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, http: OkHttpClient.Builder) : Retrofit = Retrofit.Builder()
        .baseUrl(URL.getBASEURL())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(http.build())
        .build()

    @Provides
    fun provideOkHttp(): OkHttpClient.Builder {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        val trustAllCertificates =
            arrayOf<TrustManager>(EasyTrustManager())
        val easyHostnameVerifier: HostnameVerifier = EasyHostnameVerifier()

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor) // same for .addInterceptor(...)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(easyHostnameVerifier)
            .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
            .newBuilder()

    }

    @Provides
    fun provideLogger(level : HttpLoggingInterceptor.Level): HttpLoggingInterceptor.Level = level


    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    //Login
    @Provides
    fun provideLoginService(retrofit: Retrofit): CurrencyService = retrofit.create(
        CurrencyService::class.java)

    @Singleton
    @Provides
    fun provideLoginRemoteDataSource(currencyService: CurrencyService) = CurrenciesRemoteDataSource(currencyService)

    //@Singleton
    //@Provides
    //fun provideLoginDao(db: AppDatabase) = db.loginDao()

    @Singleton
    @Provides
    fun provideLoginRepository(remoteDataSource: CurrenciesRemoteDataSource, retrofit: Retrofit) =
        CurrencyRepository(remoteDataSource, retrofit)


}