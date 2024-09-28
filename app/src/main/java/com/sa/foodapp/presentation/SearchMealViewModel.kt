package com.sa.foodapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.foodapp.data.data_source.remote.Resource
import com.sa.foodapp.data.repository.FoodRepository
import com.sa.foodapp.domain.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMealViewModel @Inject constructor(
    private val repository: FoodRepository
) :ViewModel(){
    private val _foodListStateFlow=MutableStateFlow<Resource<List<Meal?>?>>(Resource.Success(emptyList()))
    val foodListStateFlow=_foodListStateFlow.asStateFlow()

    fun searchMeal(query:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchMeal(query).collect{
                _foodListStateFlow.value=it
            }
        }
    }

}