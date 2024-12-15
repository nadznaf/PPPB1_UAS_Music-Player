package pppb1.uas.pppb1_uas_music_player

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pppb1.uas.pppb1_uas_music_player.admin.AdminActivity
//import pppb1.uas.pppb1_uas_music_player.admin.AdminHomePage
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)

        with(binding) {
            btnLogin.setOnClickListener {
                val username = inputUsnLogin.text.toString()
                val password = inputPwLogin.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isikan semua data terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (isValidUsernamePassword()) {
                        prefManager.setLoggedIn(true)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Input username atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            btnToRegis.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        RegisterActivity::class.java
                    )
                )
            }
        }
    }

    private fun isValidUsernamePassword(): Boolean {
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsnLogin = binding.inputUsnLogin.text.toString()
        val inputPwLogin = binding.inputPwLogin.text.toString()
        return username == inputUsnLogin && password == inputPwLogin
    }


    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        val username = binding.inputUsnLogin.text.toString()

        if (isLoggedIn) {
            Toast.makeText(this@LoginActivity, "Anda berhasil login!", Toast.LENGTH_SHORT).show()

            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("emailUser", username)
            editor.apply()

            if (username == "admin") {
                prefManager.saveRole("admin")
                startActivity(
                    Intent(
                        this@LoginActivity, AdminActivity::class.java
                    )
                )
            } else {
                prefManager.saveRole("user")
                startActivity(
                    Intent(
                        this@LoginActivity, MainActivity::class.java
                    )
                )
            }
            finish()
        } else {
            Toast.makeText(
                this@LoginActivity, "Login gagal dilakukan", Toast.LENGTH_SHORT
            ).show()
        }
    }
}