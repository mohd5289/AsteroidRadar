package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.ImageApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.Image
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
// This code is attributed to nourishnew
///
//Asteroid-Radar-App-Udacity-project-2
enum class ImageApiStatus { LOADING, ERROR, DONE }
class MainViewModel(application: Application) : AndroidViewModel(application) {



     private val asteroids = MutableLiveData<MutableList<Asteroid>>(mutableListOf(
  Asteroid(37126710,"2015RC", "2015-09-08",24.3, 82.0427064882,
      19.4850295824,10.4730648551,true),
         Asteroid(27123435,"R2D2","2017-05-16",16.2,51.23653421,
         21.32345,10.4730,true),
         Asteroid(45281234,"NEM32-IN","2009-12-11",19.1,53.854685836,
         10.923648286,25.534266,false),
         Asteroid(72413425,"4123BC","2016-04-12",18.4,92.1335747658,
         12.2453456,21.3264588,true),
         Asteroid(3134489,"T2B1","2018-07-11",13.4,72.849724686484,
         25.3445748582492,52.46824002,false),
         Asteroid(6428796491,"4EB1","2012-09-08",11.0,83.5133227577,
         11.32344439,91.4395997539,false),
         Asteroid(874687345638,"K3-9I","2003-01-23",15.6,73.2799292994,
         8.47365482343,41.29736493,false),
         Asteroid(74291198,"G5-E4","2018-09-23",30.2,121.37628758582,
         29.43876876485,11.87357425746,true),
         Asteroid(16385289539,"2017VE","2017-03-30",20.2,100.2875875828582,
         42.8448628482, 9.625847824,true),
         Asteroid(6474725477,"MEN-B4","2011-02-01",11.2,99.8402970420,
         52.572457427,6.86047003,true)
     ))



    private val database= getDatabase(application)
    private val asteroidRepository= AsteroidRepository(database)
    val newAsteroids= asteroidRepository.asteroids

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
        getImageOfTheDay()
    }
    private val _asteroid = MutableLiveData<Asteroid>()
    val getAsteroid: LiveData<Asteroid>
        get() = _asteroid

    //fun getShoeLiveData(): LiveData<MutableList<Shoe>> = Shoes
    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val Asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    fun getAsteroidLiveData():LiveData<MutableList<Asteroid>> = asteroids


    fun onAsteroidClicked(asteroid: Asteroid) {
     displayPropertyDetails(asteroid)
        Toast.makeText(this.getApplication(), "This ${asteroid.id} was clicked", Toast.LENGTH_SHORT)
      //  _navigateToSleepDataQuality.value = id
    }




    private val _status = MutableLiveData<ImageApiStatus>()

    val status: LiveData<ImageApiStatus>?
        get() = _status

    private val _image = MutableLiveData<Image>()
    val image: LiveData<Image>
        get() = _image



    fun displayPropertyDetails(asteroid: Asteroid) {
        _asteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _asteroid.value = null
    }

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            _status?.value= (ImageApiStatus.LOADING)
            try {
                var result = ImageApi.retrofitService.getImageOfTheDay(Constants.NASA_API_KEY)
                _image?.value =  (result)
                _status?.value =  ImageApiStatus.DONE
            } catch (e: Exception) {
                _status?.value =  ImageApiStatus.ERROR
                _image?.value = (Image("","","","","","","",""))
            }
        }
    }






    fun onViewWeekAsteroidsClicked() {
        viewModelScope.launch {

            database.asteroidDao.getAsteroidsByCloseApproachDate(getNextSevenDaysFormattedDates()[0], getNextSevenDaysFormattedDates()[6])
                .collect {
                    _asteroids.value = it
                }
        }
    }

    fun onTodayAsteroidsClicked() {
        viewModelScope.launch {
            database.asteroidDao.getAsteroidsByCloseApproachDate(getNextSevenDaysFormattedDates()[0], getNextSevenDaysFormattedDates()[6])
                .collect {
                    _asteroids.value = it
                }

        }
    }

    fun onSavedAsteroidsClicked() {
        viewModelScope.launch {
            database.asteroidDao.getAllAsteroids().collect  {
                _asteroids.value = it
            }
        }
    }









}