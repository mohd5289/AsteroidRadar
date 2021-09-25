package com.udacity.asteroidradar.repository

import android.net.Network
import android.util.Log
import android.util.Log.INFO
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.squareup.moshi.JsonClass

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.api.NasaAsteroidRadarApi.retrofitService

import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Array
import java.util.ArrayList

// This code is attributed to nourishnew
///
//Asteroid-Radar-App-Udacity-project-2
class AsteroidRepository(private val database: AsteroidDatabase) {

    var asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAllLiveAsteroids()){
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
            try {


                val json= retrofitService.fetchAsteroidsAsync(Constants.NASA_API_KEY,nextSevenDaysFormattedDates[0],nextSevenDaysFormattedDates[6])
                val obj = JSONObject(json)
                val asteroidList= parseAsteroidsJsonResult(obj)
                Log.e("asteroidList",""+asteroidList.size)
                // tried printing asteroidlist[0]. this is printing crctly
  // Log.i("asteroidList",""+asteroidList.size)
                var networkContainer = NetworkAsteroidContainer(asteroidList)

                var asteroidArray= networkContainer.asDatabaseModel()



                Log.e("closedate",asteroidArray[0].closeApproachDate)
                // this is printing null.
                database.asteroidDao.insertAll(*asteroidArray)
                //successful
            } catch (t: Throwable) {
         t.printStackTrace()
                //failed
            }

        }


    }
    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deletePreviousDayAsteroids(getNextSevenDaysFormattedDates()[0])
        }
}}





