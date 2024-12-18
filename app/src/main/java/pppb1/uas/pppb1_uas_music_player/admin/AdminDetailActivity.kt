package pppb1.uas.pppb1_uas_music_player.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pppb1.uas.pppb1_uas_music_player.R
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityAdminDetailBinding

class AdminDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding) {
            val song_name = intent.getStringExtra("song_name")
            val artist = intent.getStringExtra("artist")
            val album_name = intent.getStringExtra("album_name")
            val release = intent.getStringExtra("release")

            txtJudulLagu.text = song_name
            txtArtist.text = artist
            txtAlbum.text = album_name
            txtRilis.text = release

            ivBack.setOnClickListener {
                finish()
            }

        }
    }

}