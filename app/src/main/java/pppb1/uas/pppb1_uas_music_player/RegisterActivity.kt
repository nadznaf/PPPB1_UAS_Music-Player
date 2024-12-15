package pppb1.uas.pppb1_uas_music_player

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityLoginBinding
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)

        with(binding) {
            btnRegis.setOnClickListener {
                val username = inputUsnRegis.text.toString()
                val email = inputEmailRegis.text.toString()
                val password = inputPwRegis.text.toString()
                val confirmPassword = inputConfirmRegis.text.toString()
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Mohon isikan semua data terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Input password berbeda",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    prefManager.saveUsername(username)
                    prefManager.savePassword(password)
                    prefManager.setLoggedIn(true)
                    checkLoginStatus()
                }
            }
            btnToLogin.setOnClickListener {
                startActivity(
                    Intent(this@RegisterActivity, LoginActivity::class.java)
                )
            }
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@RegisterActivity, "Anda berhasil melakukan registrasi!",
                Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@RegisterActivity, "Registrasi gagal dilakukan",
                Toast.LENGTH_SHORT).show()
        }
    }
}