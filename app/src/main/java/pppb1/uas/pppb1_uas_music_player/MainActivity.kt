package pppb1.uas.pppb1_uas_music_player

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import pppb1.uas.pppb1_uas_music_player.auth.LoginActivity
import pppb1.uas.pppb1_uas_music_player.auth.RegisterActivity
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityMainBinding
import pppb1.uas.pppb1_uas_music_player.fragment.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            btnToLogin.setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            btnToRegister.setOnClickListener {
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            }
        }
    }
}