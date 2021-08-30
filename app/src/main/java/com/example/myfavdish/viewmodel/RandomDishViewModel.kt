package com.example.myfavdish.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.myfavdish.model.entities.RandomDish
import com.example.myfavdish.model.network.RandomDishApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class RandomDishViewModel {

    private val randomRecipeApiService = RandomDishApiService()

    private val compositeDisposable = CompositeDisposable()

    val loadRandomDish = MutableLiveData<Boolean>()
    val randomDishResponse = MutableLiveData<RandomDish.Recipes>()
    val randomDishLoadingError = MutableLiveData<Boolean>()

    fun getRandomRecipeFromAPI(){
        loadRandomDish.value = true

        compositeDisposable.add(
                randomRecipeApiService.getRandomDish()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object: DisposableSingleObserver<RandomDish.Recipes>(){
                            override fun onSuccess(value: RandomDish.Recipes) {
                                loadRandomDish.value = false
                                randomDishResponse.value = value
                                randomDishLoadingError.value = false
                            }

                            override fun onError(e: Throwable) {
                                loadRandomDish.value = false
                                randomDishLoadingError.value = true
                                e.printStackTrace()
                            }

                        })
        )
    }
}