package com.shashi.sneakersship.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

object Utils {
    suspend fun readJsonFromAssets(context: Context, fileName: String): String =
        withContext(Dispatchers.IO) {
            val stringBuilder = StringBuilder()

            val inputStream = context.assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.useLines { lines ->
                lines.forEach { line ->
                    stringBuilder.append(line)
                }
            }

            stringBuilder.toString()
        }
}