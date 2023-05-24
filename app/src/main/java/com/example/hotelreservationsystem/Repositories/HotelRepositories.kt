package com.example.hotelreservationsystem.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hotelreservationsystem.Models.HotelRequest
import com.example.hotelreservationsystem.Models.HotelResponse
import com.example.hotelreservationsystem.Models.RoomRequest
import com.example.hotelreservationsystem.api.HotelsApi
import com.example.hotelreservationsystem.utils.NetworkResult
import com.example.hotelreservationsystem.utils.constants.TAG
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class HotelRepositories @Inject constructor(private  val hotelsApi: HotelsApi) {
    private  val _hotelLiveData =MutableLiveData<NetworkResult<HotelResponse>>()
        val hotelLiveData : MutableLiveData<NetworkResult<HotelResponse>>
            get() = _hotelLiveData


    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData :LiveData<NetworkResult<String>>
        get()= _statusLiveData


    suspend fun createHotel(ownerId:String,hotelRequest: HotelRequest)

    {
        _statusLiveData.postValue(NetworkResult.Loading())
        _hotelLiveData.postValue(NetworkResult.Loading())
        val response = hotelsApi.createHotel(ownerId ,hotelRequest)
        if(response.isSuccessful && response.body()!= null)
        {
            hotelLiveData.postValue(NetworkResult.Success(response.body()))
        }
        else
        {
            val errotObj = JSONObject(response.errorBody()!!.charStream().readText())
            hotelLiveData.postValue(NetworkResult.Error(errotObj.getString("error")))

        }
        handleresponse(response,"Hotel Created")

    }

    suspend fun  addRoom(ownerId: String,hotelId:String,roomRequest: RoomRequest){
        _hotelLiveData.postValue(NetworkResult.Loading())
        val response = hotelsApi.addRoom(ownerId,hotelId,roomRequest)
        if(response.isSuccessful && response.body()!= null)
        {
            hotelLiveData.postValue(NetworkResult.Success(response.body()))
        }
        else{
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            hotelLiveData.postValue(NetworkResult.Error(errorObj.getString("error")))

        }
    }



    // outside all the functions
    private fun handleresponse(response: Response<HotelResponse>,message:String) {
        if (response.isSuccessful && response.body() != null) {
            Log.d(TAG,response.body().toString())
            _statusLiveData.postValue(NetworkResult.Success(message))

        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            Log.d(TAG,"Maile response pasko xaina")
        }
    }


}
