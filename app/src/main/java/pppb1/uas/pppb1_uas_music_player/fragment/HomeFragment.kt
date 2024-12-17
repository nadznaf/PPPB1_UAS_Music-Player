package pppb1.uas.pppb1_uas_music_player.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pppb1.uas.pppb1_uas_music_player.DetailActivity
import pppb1.uas.pppb1_uas_music_player.ItemMusicAdapter
import pppb1.uas.pppb1_uas_music_player.auth.PrefManager
import pppb1.uas.pppb1_uas_music_player.databinding.FragmentHomeBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import pppb1.uas.pppb1_uas_music_player.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var executorService: ExecutorService
    private lateinit var client: ApiService
    private lateinit var musicList: ArrayList<Musics>
    private lateinit var adapterMusics: ItemMusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        musicList = ArrayList()
        val prefManager = PrefManager.getInstance(requireContext())
        val username = prefManager.getUsername()
        executorService = Executors.newSingleThreadExecutor()
        val client = ApiClient.getInstance()
        with(binding) {
            txtHello.text = "Hello, ${username} !"

            adapterMusics = ItemMusicAdapter(musicList, client) { data ->

                val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra("id", data.id)
                    putExtra("song_name", data.song_name)
                    putExtra("artist", data.artist)
                    putExtra("album_name", data.album_name)
                    putExtra("release", data.release)
                }
                startActivity(intent)
            }
            rvMusics.layoutManager = LinearLayoutManager(requireContext())
            rvMusics.adapter = adapterMusics

            progressBar.visibility = android.view.View.VISIBLE
        }
        fetchLatihanList()

        return binding.root
    }

    private fun fetchLatihanList() {
        val client = ApiClient.getInstance()
        val response = client.getAllMusics()

        response.enqueue(object : Callback<List<Musics>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Musics>>, response: Response<List<Musics>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Tambahkan data ke dalam list dan perbarui adapter
                    binding.progressBar.visibility = android.view.View.GONE
                    musicList.clear()
                    musicList.addAll(response.body()!!)
                    adapterMusics.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal memuat data latihan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Musics>>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}