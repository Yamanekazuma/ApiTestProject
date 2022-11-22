package com.example.apitestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    private val button : Button by lazy { findViewById(R.id.button) }
    private val text: TextView by lazy { findViewById(R.id.responseText) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            callAPI()
        }
    }

    private fun callAPI() {
        val client = OkHttpClient.Builder().build()

        val response = Retrofit.Builder()
            .baseUrl("http://ml03.z.nitech.ac.jp:3000")
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                )
            )
            .build()
            .create(TestService::class.java)

        lifecycleScope.launch {
            val result: Response<TestResponse> = response.getTestResponse()
            result.body()?.let {
                text.text = "id: ${it.id}, name: ${it.name}"
            }
        }
    }
}

interface TestService {
    @GET("seat/show_list")
    suspend fun getTestResponse (): Response<TestResponse>
}

data class TestResponse (
    val id: Int,
    val name: String,
    val studentID: String,
    val seatNumber: String
)
