package com.imdumb.data.cache

import android.content.SharedPreferences
import com.google.gson.Gson
import com.imdumb.domain.entities.Category
import javax.inject.Inject

class CategoryCache @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    private val KEY_CATEGORIES = "cached_categories"

    fun saveCategories(categories: List<Category>) {
        val json = gson.toJson(categories)
        sharedPreferences.edit().putString(KEY_CATEGORIES, json).apply()
    }

    fun getCategories(): List<Category>? {
        val json = sharedPreferences.getString(KEY_CATEGORIES, null)
        return json?.let {
            val type = object : com.google.gson.reflect.TypeToken<List<Category>>() {}.type
            gson.fromJson(it, type)
        }
    }
}
