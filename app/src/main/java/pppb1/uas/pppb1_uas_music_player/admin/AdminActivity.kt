package pppb1.uas.pppb1_uas_music_player.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import pppb1.uas.pppb1_uas_music_player.auth.PrefManager
import pppb1.uas.pppb1_uas_music_player.R
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityAdminBinding
import pppb1.uas.pppb1_uas_music_player.databinding.ItemDialogBinding
import pppb1.uas.pppb1_uas_music_player.model.Library
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var adapter: AdminMusicAdapter
    private val musicList = ArrayList<Musics>()
    private lateinit var prefmanager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        prefmanager = PrefManager.getInstance(this@AdminActivity)
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchMusicList()

        binding.btnTambah.setOnClickListener {
            startActivity(
                Intent(
                    this@AdminActivity,
                    AdminCreateActivity::class.java
                )
            )
        }
        binding.btnLogout.setOnClickListener{
            showLogoutDialog()

        }
    }
    private fun setupRecyclerView() {
        adapter = AdminMusicAdapter(musicList, ApiClient.getInstance()) {
            data ->
            val intent = Intent(this@AdminActivity, AdminDetailActivity::class.java).apply {
                putExtra("id", data.id)
                putExtra("songName", data.judul)
                putExtra("artis", data.artis)
                putExtra("album", data.album)
                putExtra("rilis", data.rilis)
            }
            startActivity(intent)
        }
        binding.rvAdmin.layoutManager = LinearLayoutManager(this@AdminActivity)
        binding.rvAdmin.adapter = adapter
    }

    private fun fetchMusicList() {
        val client = ApiClient.getInstance()
        val response = client.getAllMusics()

        response.enqueue(object : Callback<List<Musics>> {
            override fun onResponse(call: Call<List<Musics>>, response: Response<List<Musics>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Tambahkan data ke dalam list dan perbarui adapter
                    musicList.clear()
                    musicList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@AdminActivity,
                        "Gagal memuat data lagu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Musics>>, t: Throwable) {
                Toast.makeText(
                    this@AdminActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        val inflate = this.layoutInflater
        val binding = ItemDialogBinding.inflate(inflate)
        val dialog = builder.setView(binding.root)
            .setCancelable(false) // Make dialog non-cancelable by tapping outside
            .create()

        with(binding){
            dialogTitle.text = "Logout"
            dialogMessage.text = "Apakah Anda yakin ingin logout?"
            btnConfirm.text = "Ya"
            btnConfirm.setOnClickListener {
                prefmanager.clear()
                finish()
            }
            btnCancel.text = "Tidak"
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}