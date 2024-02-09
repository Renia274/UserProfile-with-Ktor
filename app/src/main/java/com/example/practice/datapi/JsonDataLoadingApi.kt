package com.example.practice.datapi

import android.content.res.Resources
import com.example.practice.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.BufferedReader
import java.io.InputStreamReader

class JsonDataLoadingApi(private val resources: Resources) : DataLoadingApi {

    override fun loadProfessions(): List<String> {
        val inputStream = resources.openRawResource(R.raw.professions)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = reader.use { it.readText() }
        val json = Json.parseToJsonElement(jsonString)
        return json.jsonObject["professions"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
    }

    override fun loadInterests(): List<String> {
        val inputStream = resources.openRawResource(R.raw.interests)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = reader.use { it.readText() }
        val json = Json.parseToJsonElement(jsonString)
        return json.jsonObject["interests"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
    }
}
