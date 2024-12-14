package pppb1.uas.pppb1_uas_music_player.network

import okhttp3.RequestBody
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("user")
    fun getAllUsers(): Call<List<User>>

    @GET("user/{id}")
    fun getUserById(@Path("id") musicId: String): Call<List<User>>

    @GET("musics")
    fun getAllMusics(): Call<List<Musics>>

    @POST("musics")
    fun postNewMusics(@Body rawJson: RequestBody) : Call<Musics>

    @POST("musics/{id}")
    fun updateMusics(@Path("id") musicId: String, @Body rawJson: RequestBody) : Call<Musics>

    @DELETE("musics/{id}")
    fun deleteMusics(@Path("id") musicId: String): Call<List<Musics>>
}