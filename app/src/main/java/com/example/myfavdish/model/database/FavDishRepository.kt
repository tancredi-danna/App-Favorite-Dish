package com.example.myfavdish.model.database

import androidx.annotation.WorkerThread
import com.example.myfavdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }
    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishesList()

    @WorkerThread

    suspend fun updateFavDishData(favDish: FavDish){
        favDishDao.updateFaveDishDetails(favDish)
    }

    val favoriteDishesList: Flow<List<FavDish>> = favDishDao.getAllFavoriteDish()

    @WorkerThread

    suspend fun deleteFavDish(favDish: FavDish){

        favDishDao.deleteFavDishDetails(favDish)
    }
     fun filteredListDishes(value: String) : Flow<List<FavDish>> = favDishDao.getFilteredDishList(value)
}