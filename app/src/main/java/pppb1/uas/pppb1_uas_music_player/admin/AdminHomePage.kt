package pppb1.uas.pppb1_uas_music_player.admin

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.uas.pppb1_uas_music_player.PrefManager
import pppb1.uas.pppb1_uas_music_player.R
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityAdminHomePageBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiClient

class AdminHomePage : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHomePageBinding
    private lateinit var prefmanager: PrefManager
    private lateinit var adapterMusics: MusicAdapter
    private val musicList = ArrayList<Musics>()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent5>

    override fun onCreate(savedInstanceState: Bundle?) {
        prefmanager = PrefManager.getInstance(this)
        binding = ActivityAdminHomePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapterMusics = MusicAdapter (musicList, ApiClient.getInstance(),
            onEditMusic = { intent -> activityResultLauncher<Intent>})
    }
}