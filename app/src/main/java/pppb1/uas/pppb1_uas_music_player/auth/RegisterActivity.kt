package pppb1.uas.pppb1_uas_music_player.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityRegisterBinding
import pppb1.uas.pppb1_uas_music_player.model.User
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnRegis.setOnClickListener {
                val username = binding.inputUsnRegis.text.toString()
                val email = binding.inputEmailRegis.text.toString()
                val password = binding.inputPwRegis.text.toString()
                val confirmPassword = binding.inputConfirmRegis.text.toString()

                if (validateForm(username, email, password, confirmPassword)) {
                    val apiService = ApiClient.getInstance()
                    val response = apiService.getAllUsers()
                    btnRegis.isEnabled = false

                    response.enqueue(object : Callback<List<User>> {
                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                val users = response.body()
                                val existingUser = users?.find { it.username == username }
                                val existingEmail = users?.find { it.email == email }
                                if (existingUser != null || existingEmail != null) {
                                    btnRegis.isEnabled = true

                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Username or email already exists",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {

                                    val user = User(
                                        id = null,
                                        username = username,
                                        email = email,
                                        password = password,
                                        role = "user"
                                    )
                                    createUser(user)
                                }
                            } else {
                                btnRegis.isEnabled = true
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Failed to fetch users",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            btnRegis.isEnabled = true
                            Toast.makeText(
                                this@RegisterActivity,
                                "Error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                } else {
                    Toast.makeText(this@RegisterActivity, "Please fill out the form correctly!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnToLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    // Fungsi untuk validasi form
    private fun validateForm(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    }

    // Fungsi untuk membuat user baru
    private fun createUser(user: User) {
        val apiService = ApiClient.getInstance()
        val call = apiService.createUser(user)

        // Melakukan request API untuk membuat user
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    // Jika berhasil, arahkan ke LoginActivity
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration Successful! please login again ",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    // Jika gagal
                    Toast.makeText(
                        this@RegisterActivity,
                        "Failed to register user",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Jika gagal terhubung ke API
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}