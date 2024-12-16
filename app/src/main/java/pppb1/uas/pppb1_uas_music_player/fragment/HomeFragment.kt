package pppb1.uas.pppb1_uas_music_player.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pppb1.uas.pppb1_uas_music_player.DetailActivity
import pppb1.uas.pppb1_uas_music_player.HomeMusicAdapter
import pppb1.uas.pppb1_uas_music_player.ItemUserAdapter
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var executorService: ExecutorService
    private lateinit var client: ApiService
    private lateinit var musicList: ArrayList<Musics>
    private lateinit var adapterMusics: HomeMusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        musicList = ArrayList()

        executorService = Executors.newSingleThreadExecutor()
        val client = ApiClient.getInstance()
        with(binding) {
            adapterMusics = HomeMusicAdapter(musicList, client) {
                data ->
                val intent = Intent(requireActivity(), DetailActivity::class.java).apply { 
                    putExtra("id", data.id)
                    putExtra("songName", data.judul)
                    putExtra("artis", data.artis)
                    putExtra("album", data.album)
                    putExtra("rilis", data.rilis)
                }
                startActivity(intent)
            }
            rvMusics.layoutManager = LinearLayoutManager(requireContext())
            rvMusics.adapter = adapterMusics
        }
        fetchMusicList()

        return binding.root
    }

    private fun fetchMusicList() {
        val client = ApiClient.getInstance()
        val response = client.getAllMusics()

        response.enqueue(object : Callback<List<Musics>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<Musics>>, response: Response<List<Musics>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Tambahkan data ke dalam list dan perbarui adapter
                    musicList.clear()
                    musicList.addAll(response.body()!!)
                    adapterMusics.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal memuat data musik",
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

