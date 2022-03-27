package com.app.topupmama

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity
import com.app.topupmama.presentation.weather.repository.Repository
import com.app.topupmama.ui.MainStateEvent
import com.app.topupmama.ui.MainViewModel
import com.app.topupmama.utils.Constants
import com.app.topupmama.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class FetchDataTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var weatherRepository: Repository

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

    }
    private val testDispatcher = TestCoroutineDispatcher()
    @Test
    fun test_fetchWeatherList() = testDispatcher.runBlockingTest {

        val weatherViewModel = MainViewModel(weatherRepository)

        weatherViewModel.getFavorite {
            Assert.assertEquals(1,it?.keyId)
            Assert.assertEquals(2.5, it?.main?.temp)
        }

    }

}