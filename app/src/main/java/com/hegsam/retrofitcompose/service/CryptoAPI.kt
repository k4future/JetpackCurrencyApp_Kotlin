package com.hegsam.retrofitcompose.service

import com.hegsam.retrofitcompose.model.CryptoModel
import retrofit2.Response
import retrofit2.http.GET

interface CryptoAPI {
    @GET("prices?key=f775e68f620b7fb094df283bc632cc90a0d6224c")
    suspend fun getData () : Response<List<CryptoModel>>
}