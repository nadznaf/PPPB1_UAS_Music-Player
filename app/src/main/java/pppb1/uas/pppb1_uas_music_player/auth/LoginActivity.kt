package pppb1.uas.pppb1_uas_music_player.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pppb1.uas.pppb1_uas_music_player.MainActivity
import pppb1.uas.pppb1_uas_music_player.admin.AdminActivity
//import pppb1.uas.pppb1_uas_music_player.admin.AdminHomePage
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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()
        val client = ApiClient.getInstance()

        with(binding){
            btnLogin.setOnClickListener {
                btnLogin.isEnabled = false

                val response = client.getAllUsers()
                response.enqueue(object : Callback<List<User>> {
                    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                        btnLogin.isEnabled = true

                        if (response.isSuccessful && response.body() != null) {
                            var loginSuccess = false
                            response.body()?.forEach { i ->
                                if (i.username == inputUsnLogin.text.toString() && i.password == inputPwLogin.text.toString()) {
                                    loginSuccess = true
                                    prefManager.setLoggedIn(true)
                                    prefManager.saveUsername(inputUsnLogin.text.toString())
                                    prefManager.savePassword(inputPwLogin.text.toString())
                                    prefManager.saveRole(i.role)
                                    prefManager.saveEmail(i.email)
                                    checkLoginStatus()
                                    finish()
                                }
                            }
                            if (!loginSuccess) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Kata sandi atau username salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Log.e("API Error", "Response not successful or body is null")
                            Toast.makeText(
                                this@LoginActivity,
                                "Terjadi kesalahan, coba lagi nanti",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        btnLogin.isEnabled = true
                        Toast.makeText(
                            this@LoginActivity,
                            "Koneksi error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
            btnToRegis.setOnClickListener{
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

        }

    }
    fun checkLoginStatus(){
        if(prefManager.isLoggedIn()){
            if(prefManager.getRole() == "admin"){
                val intentToHome = Intent(this@LoginActivity, AdminActivity::class.java)
                startActivity(intentToHome)
            }else if(prefManager.getRole() == "user"){
                val intentToHome = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentToHome)
            }
        }
    }
}