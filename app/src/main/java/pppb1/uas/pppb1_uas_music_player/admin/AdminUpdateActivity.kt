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
            val songName = intent.getStringExtra("judul")
            val artists = intent.getStringExtra("artis")
            val albumName = intent.getStringExtra("album")
            val release = intent.getStringExtra("rilis")

            inputSongName.setText(songName)
            inputArtist.setText(artists)
            inputAlbum.setText(albumName)
            inputRilis.setText(release)

            btnEditMusic.setOnClickListener{
                val updatedJudul = inputSongName.text.toString()
                val updatedArtis = inputArtist.text.toString()
                val updatedAlbum = inputAlbum.text.toString()
                val updatedRilis = inputRilis.text.toString()

                if (validateForm(updatedJudul, updatedArtis, updatedAlbum, updatedRilis)) {
                    val musics = Musics(
                        id = id.toString(),
                        judul = updatedJudul,
                        artis = updatedArtis,
                        album = updatedAlbum,
                        rilis = updatedRilis
                    )
                    val response = client.updateMusic(id.toString(),musics)
                    response.enqueue(object : Callback<Musics> {
                        override fun onResponse(
                            call: Call<Musics>,
                            response: retrofit2.Response<Musics>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@AdminUpdateActivity,
                                    "Musik berhasil diupdate",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@AdminUpdateActivity, AdminActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@AdminUpdateActivity,
                                    "Gagal update musik",
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
        judul: String,
        artis: String,
        album: String,
        rilis: String
    ): Boolean {
        return judul.isNotEmpty() &&
                artis.isNotEmpty() &&
                album.isNotEmpty() &&
                rilis.isNotEmpty()
    }
}