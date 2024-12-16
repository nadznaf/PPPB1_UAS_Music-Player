package pppb1.uas.pppb1_uas_music_player.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.uas.pppb1_uas_music_player.R
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityAdminDetailBinding

class AdminDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val songName = intent.getStringExtra("judul")
            val artis = intent.getStringExtra("artis")
            val album = intent.getStringExtra("album")
            val rilis = intent.getStringExtra("rilis")

            txtSongName.text = songName
            txtArtist.text = artis
            txtAlbum.text = album
            txtRilis.text = rilis

            ivBack.setOnClickListener {
                finish()
            }
        }
    }
}