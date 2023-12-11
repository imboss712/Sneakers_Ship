package com.shashi.sneakersship.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashi.sneakersship.model.Sneaker
import com.shashi.sneakersship.utils.Constants
import com.shashi.sneakersship.utils.Constants.JSON_READ_RESPONSE_TIMEOUT
import com.shashi.sneakersship.utils.Resource
import com.shashi.sneakersship.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull

class SneakerRepository {

    suspend fun getSneakers(context: Context) = flow {
        emit(Resource.Loading)

        withTimeoutOrNull(JSON_READ_RESPONSE_TIMEOUT) {
            val json = Utils.readJsonFromAssets(context, Constants.FILENAME_SNEAKERS)

            val listType = object : TypeToken<ArrayList<Sneaker>>() {}.type
            val sneakersData: List<Sneaker> = Gson().fromJson(json, listType)

            emit(Resource.Success(sneakersData))
        } ?: emit(Resource.Error(408, "Timeout! Please try again"))
    }.catch { throwable ->
        emit(Resource.Error(500, throwable.message ?: throwable.toString()))
    }.flowOn(Dispatchers.IO)
}