package pppb1.uas.pppb1_uas_music_player.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.uas.pppb1_uas_music_player.R
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityAdminCreateBinding
import pppb1.uas.pppb1_uas_music_player.model.Library
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import pppb1.uas.pppb1_uas_music_player.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnSubmitLibrary.setOnClickListener{
                val songName = inputSongName.text.toString()
                val artists = inputArtist.text.toString()
                val albumName = inputAlbum.text.toString()
                val release = inputRilis.text.toString()

                val music = Musics(
                    judul = songName,
                    artis = artists,
                    album = albumName,
                    rilis = release
                )
                val ApiService = ApiClient.getInstance()
                val response = ApiService.createMusic(music)
                response.enqueue(object : Callback<Musics>{
                    override fun onResponse(call: Call<Musics>, response: Response<Musics>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AdminCreateActivity, "Musik berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@AdminCreateActivity, AdminActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@AdminCreateActivity, "Gagal menambahkan musik", Toast.LENGTH_SHORT).show()
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

