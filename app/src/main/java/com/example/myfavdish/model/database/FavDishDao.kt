package com.example.myfavdish.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myfavdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow


@Dao
interface FavDishDao {

   @Insert
   suspend fun insertFavDishDetails(favDish: FavDish)


   @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
   fun getAllDishesList(): Flow<List<FavDish>>

   @Update
   suspend fun updateFaveDishDetails(favDish: FavDish)

   @Query("SELECT * FROM FAV_DISHES_TABLE WHERE favorite_dish = 1")
   fun getAllFavoriteDish(): Flow<List<FavDish>>
}