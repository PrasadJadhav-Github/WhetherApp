package com.example.whetherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import com.example.whetherapp.dataClass.whetherapp
import com.example.whetherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//e3c79845667f733478e0e2dc87511f90
class MainActivity : AppCompatActivity() {
    private  val   binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWhetherData("Pune")
        searchCity()
    }

    private  fun searchCity(){
        val searchView=binding.searchView
      searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
          android.widget.SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(query: String?): Boolean {
              if (query != null) {
                  fetchWhetherData(query)
              }
              return true
          }

          override fun onQueryTextChange(newText: String?): Boolean {
              return true
          }


      })
    }
    private fun fetchWhetherData(cityname:String){
        val retrofit=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityname,"e3c79845667f733478e0e2dc87511f90","metric")
        response.enqueue(object :Callback<whetherapp>{
            override fun onResponse(call: Call<whetherapp>, response: Response<whetherapp>) {
                val responseBody=response.body()
                if (response.isSuccessful&& responseBody!=null){
                    val tempreature=responseBody.main.temp.toString()
                    val humidity =responseBody.main.humidity
                    val windspeed=responseBody.wind.speed
                    val  sunrise =responseBody.sys.sunrise.toLong()
                    val  sunset=responseBody.sys.sunset.toLong()
                    val sealevel=responseBody.main.pressure
                    val condition =responseBody.weather.firstOrNull()?.main?:"unknown"
                    val maxtemp=responseBody.main.temp_max
                    val  mintemp=responseBody.main.temp_min
                    //Log.d("TAG", "onResponse: $tempreature")
                        binding.temp.text="$tempreature °C"
                        binding.whether.text=condition
                        binding.maxTemp.text="MAX TEMP: $maxtemp °C"
                        binding.minTemp.text="MIN TEMP :$mintemp °C"
                        binding.humidity.text="$humidity %"
                        binding.wind.text="$windspeed m/s"
                        binding.sunrise.text="${Time(sunrise)}"
                        binding.sunset.text="${Time(sunset)}"
                        binding.sea.text="$sealevel hPa"
                        binding.cityName.text="$cityname"
                        binding.condition.text=condition
                        binding.day.text=dayName(System.currentTimeMillis())
                        binding.date.text=date()

                    changeImageAccordingToWhetherCondition(condition)
                }
            }

            override fun onFailure(call: Call<whetherapp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun changeImageAccordingToWhetherCondition(conditions: String) {
        when(conditions){
            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds","Clouds","Overcast"," Mist","Foggy"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow","Moderate Snow","Heavy Snow","Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }else ->{
            binding.root.setBackgroundResource(R.drawable.sunny_background)
            binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()

    }

    fun  dayName(timestamp: Long):String{
        val sdf=SimpleDateFormat("EEEE",Locale.getDefault())
        return sdf.format((Date()))

    }

    fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }


    fun Time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))
    }

}


