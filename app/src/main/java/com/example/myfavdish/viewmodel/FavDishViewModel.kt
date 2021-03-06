package com.example.myfavdish.viewmodel

import androidx.lifecycle.*
import com.example.myfavdish.model.database.FavDishRepository
import com.example.myfavdish.model.entities.FavDish
import kotlinx.coroutines.launch

class FavDishViewModel(private val repository: FavDishRepository) : ViewModel(){

    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishData(dish)
    }
    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

    fun update(dish: FavDish) = viewModelScope.launch {
        repository.updateFavDishData(dish)
    }

    val allFavoriteDishList: LiveData<List<FavDish>> = repository.favoriteDishesList.asLiveData()

    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteFavDish(dish)
    }

    fun getFilteredList(value: String) : LiveData<List<FavDish>> = repository.filteredListDishes(value).asLiveData()
}



class FavDishViewModelFactory(private val repository: FavDishRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavDishViewModel::class.java)){
            return FavDishViewModel(repository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }

    }

}