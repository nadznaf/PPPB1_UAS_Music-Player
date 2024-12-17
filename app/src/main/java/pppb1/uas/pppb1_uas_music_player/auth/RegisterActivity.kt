package pppb1.uas.pppb1_uas_music_player.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pppb1.uas.pppb1_uas_music_player.admin.AdminActivity
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityRegisterBinding
import pppb1.uas.pppb1_uas_music_player.model.User
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnRegister.setOnClickListener {
                val username = binding.inputUsername.text.toString()
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()
                val confirmPassword = binding.inputConfirm.text.toString()

                if (validateForm(username, email, password, confirmPassword)) {
                    // Kirim permintaan untuk mengecek apakah username sudah ada
                    val apiService = ApiClient.getInstance()
                    val response = apiService.getAllUsers()
                    progressBar.visibility = View.VISIBLE // Show loading indicator
                    btnRegister.isEnabled = false // Disable button

                    response.enqueue(object : Callback<List<User>> {
                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                val users = response.body()
                                // Mengecek apakah username sudah terdaftar
                                val existingUser = users?.find { it.username == username }
                                val existingEmail = users?.find { it.email == email }
                                if (existingUser != null || existingEmail != null) {
                                    progressBar.visibility = View.GONE // Hide loading
                                    btnRegister.isEnabled = true // Re-enable button
                                    // Username sudah ada, beri tahu pengguna
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Username or email already exists",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    // Username belum ada, lanjutkan dengan pendaftaran
                                    val user = User(
                                        id = null,
                                        username = username,
                                        email = email,
                                        password = password,
                                        role = "user" // Default role "user"
                                    )
                                    createUser(user)
                                }
                            } else {
                                progressBar.visibility = View.GONE // Hide loading
                                btnRegister.isEnabled = true // Re-enable button
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Failed to fetch users",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            progressBar.visibility = View.GONE // Hide loading
                            btnRegister.isEnabled = true
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