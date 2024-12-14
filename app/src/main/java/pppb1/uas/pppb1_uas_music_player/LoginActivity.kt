package pppb1.uas.pppb1_uas_music_player

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pppb1.uas.pppb1_uas_music_player.admin.AdminHomePage
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityLoginBinding
import pppb1.uas.pppb1_uas_music_player.model.User
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefManager = PrefManager.getInstance(this)

        checkLoginStatus()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = ApiClient.getInstance()

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

                }
                val response = client.getAllUsers()
                response.enqueue(object: Callback<List<User>> {
                    override fun onResponse(
                        call: Call<List<User>>,
                        response: Response<List<User>>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            response.body()?.forEach { i ->
                                if (i.username == inputUsnLogin.text.toString() && i.password == inputPwLogin.text.toString()) {
                                    prefManager.setLoggedIn(true)
                                    prefManager.saveUsername(inputUsnLogin.text.toString())
                                    prefManager.savePassword(inputPwLogin.text.toString())
                                    prefManager.saveRole(i.role)
                                }
                            }
                        } else {
                            Log.e("API Error", "Response not successful or body is null")
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Koneksi error ${t.toString()}", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    fun checkLoginStatus() {
        if (prefManager.isLoggedIn()) {
            val intentToHomePage = if (prefManager.getRole() == "admin") {
                Intent(this@LoginActivity, AdminHomePage::class.java)
            } else {
                Intent(this@LoginActivity, UserHomeActivity::class.java)
            }
            intentToHomePage.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentToHomePage)
        }
    }
}