package br.com.tairoroberto.easynvest.data

import br.com.tairoroberto.easynvest.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    companion object {
        private var URL = "https://api-simulator-calc.easynvest.com.br/"
        private val gson = GsonBuilder().setLenient().create()
        private val httpClient = OkHttpClient.Builder()

        init {
            val httpClient = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logging)
            }
        }

        fun getApi(): API {
            return Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()
                .create(API::class.java)
        }

        fun setBaseUrl(baseUrl: String) {
            URL = baseUrl
        }
    }
}