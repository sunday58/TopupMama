package com.app.topupmama.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity
import com.app.topupmama.presentation.weather.repository.Repository
import com.app.topupmama.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val mainRepository: Repository,
): ViewModel(){



    private val _dataState: MutableLiveData<DataState<WeatherRemoteEntity>> = MutableLiveData()
    val dataState: LiveData<DataState<WeatherRemoteEntity>>
    get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent, id: String, units: String, key: String){
        viewModelScope.launch {
            when(mainStateEvent){
                is MainStateEvent.GetWeatherEvents -> {
                    mainRepository.getBlog(id, units, key)
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.None -> {
                    //do nothing
                }
            }
        }
    }

    fun updateFavorite(favoriteEntity: WeatherRemoteEntity.Object, result: (WeatherRemoteEntity.Object?) -> Unit){
        viewModelScope.launch {
            mainRepository.updateFavorite(favoriteEntity){
                result(it)
            }
        }
    }

    fun getFavorite( result: (WeatherRemoteEntity.Object?) -> Unit){
        viewModelScope.launch {
            mainRepository.getFavorite(){
                result(it)
            }
        }
    }

}

sealed class MainStateEvent(){
    object GetWeatherEvents: MainStateEvent()

    object None: MainStateEvent()
}
