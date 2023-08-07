package com.example.portfoliokeuangan.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.portfoliokeuangan.data.Portfolio
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class PortfolioViewModel(val context: Context): ViewModel() {
    private var portofolioList: List<Portfolio> = emptyList()

    init {
        portofolioList = loadPortfolioFromAssets()
    }

    private fun loadPortfolioFromAssets(): List<Portfolio> {
        val inputStream = context.assets.open("portfolio.json")
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        val sType = object : TypeToken<List<Portfolio>>() {}.type
        return parseArray<List<Portfolio>>(json = json, typeToken = sType)
    }
    inline fun <reified T> parseArray(json: String, typeToken: Type): T {
        val gson = GsonBuilder().create()
        return gson.fromJson<T>(json, typeToken)
    }


    fun getPortfolio() = portofolioList
}