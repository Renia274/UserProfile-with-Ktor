package com.example.practice.datapi

interface DataLoadingApi {
    fun loadProfessions(): List<String>
    fun loadInterests(): List<String>
}
