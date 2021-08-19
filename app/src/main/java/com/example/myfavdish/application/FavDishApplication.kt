package com.example.myfavdish.application

import android.app.Application
import com.example.myfavdish.model.database.FavDishRepository
import com.example.myfavdish.model.database.FavDishRoomDatabase

class FavDishApplication : Application() {

    private val database by lazy {FavDishRoomDatabase.getDatabase(this@FavDishApplication)}

    val repository by lazy { FavDishRepository(database.favDishDao())}
}