package pppb1.uas.pppb1_uas_music_player.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityAdminCreateBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnSubmit.setOnClickListener{
                val song_name = inputSongName.text.toString()
                val artist = inputArtist.text.toString()
                val album_name = inputAlbum.text.toString()
                val release = inputRilis.text.toString()

                val musics = Musics(
                    song_name = song_name,
                    artist = artist,
                    album_name = album_name,
                    release = release,
                )
                val apiService = ApiClient.getInstance()
                val response = apiService.createMusics(musics)
                response.enqueue(object : Callback<Musics> {
                    override fun onResponse(call: Call<Musics>, response: retrofit2.Response<Musics>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AdminCreateActivity, "Lagu berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@AdminCreateActivity, AdminActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@AdminCreateActivity, "Gagal menambah lagu", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Musics>, t: Throwable) {
                        Toast.makeText(this@AdminCreateActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }
    }
}