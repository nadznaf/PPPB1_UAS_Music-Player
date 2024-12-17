package pppb1.uas.pppb1_uas_music_player.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.uas.pppb1_uas_music_player.R
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityAdminUpdateBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import retrofit2.Call
import retrofit2.Callback

class AdminUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = ApiClient.getInstance()
        with(binding) {
            val id = intent.getStringExtra("id")
            val song_name = intent.getStringExtra("song_name")
            val artist = intent.getStringExtra("artist")
            val album_name = intent.getStringExtra("album_name")
            val release = intent.getStringExtra("release")

            inputSongName.setText(song_name)
            inputArtist.setText(artist)
            inputAlbumName.setText(album_name)
            inputRilis.setText(release)

            btnEditMusic.setOnClickListener {
                val updatedSongName = inputSongName.text.toString()
                val updatedArtist = inputArtist.text.toString()
                val updatedAlbum = inputAlbumName.text.toString()
                val updatedRelease = inputAlbumName.text.toString()

                if (validateForm(updatedSongName, updatedArtist, updatedAlbum, updatedRelease)) {
                    val musics = Musics(
                        id = id,
                        song_name = updatedSongName,
                        artist = updatedArtist,
                        album_name = updatedAlbum,
                        release = updatedRelease
                    )
                    val response = client.updateMusics(id.toString(),musics)
                    response.enqueue(object : Callback<Musics> {
                        override fun onResponse(
                            call: Call<Musics>,
                            response: retrofit2.Response<Musics>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@AdminUpdateActivity,
                                    "Lagu berhasil diupdate",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@AdminUpdateActivity, AdminActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@AdminUpdateActivity,
                                    "Gagal update lagu",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Musics>, t: Throwable) {
                            Toast.makeText(
                                this@AdminUpdateActivity,
                                "Error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        }
    }

    private fun validateForm(
        song_name: String,
        artist: String,
        album_name: String,
        release: String
    ): Boolean {
        return song_name.isNotEmpty() &&
                artist.isNotEmpty() &&
                album_name.isNotEmpty() &&
                release.isNotEmpty()
    }

}