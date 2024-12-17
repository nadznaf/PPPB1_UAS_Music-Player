package pppb1.uas.pppb1_uas_music_player

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityFavoriteDetailBinding

class FavoriteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){

            ivBack.setOnClickListener{
                finish()
            }
            val song_name = intent.getStringExtra("song_name")
            val artist = intent.getStringExtra("artist")
            val album_name = intent.getStringExtra("album_name")
            val release = intent.getStringExtra("release")

            txtJudulLagu.text = song_name
            txtArtist.text = artist
            txtAlbum.text = album_name
            txtRilis.text = release
        }
    }
}