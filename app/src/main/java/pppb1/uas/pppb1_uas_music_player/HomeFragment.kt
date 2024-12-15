package pppb1.uas.pppb1_uas_music_player

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pppb1.uas.pppb1_uas_music_player.databinding.FragmentHomeBinding
import pppb1.uas.pppb1_uas_music_player.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemUserAdapter: ItemUserAdapter
    private lateinit var recyclerView: RecyclerView
    private var musicList: List<Data> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout menggunakan View Binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val prefManager = PrefManager.getInstance(requireContext())
        val username = prefManager.getUsername()

        // Setel teks di txtName dengan username
        binding.txtHello.text = "Hello, $username!"

        // Setup RecyclerView
        recyclerView = binding.rvMusics // Asumsikan ada RecyclerView di fragment_home.xml
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch data dari API
        fetchUserData()

        return binding.root
    }

    private fun fetchUserData() {
        ApiClient.getInstance().getAllMusics().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    musicList = response.body() ?: listOf()
                    itemUserAdapter = ItemUserAdapter(requireContext(), musicList)
                    recyclerView.adapter = this@HomeFragment.itemUserAdapter

                    // Handle item click
                    itemUserAdapter.setOnItemClickListener { selectedMusic ->
                        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                            putExtra("song_name", musicItem?.judul)
                            putExtra("artist", musicItem?.artis)
                            putExtra("album_name", musicItem?.album)
                            putExtra("release", musicItem?.rilis)
                            putExtra("picture", musicItem?.gambar)
                        }
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.e("API Error", "Failed to fetch data", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Hindari memory leaks
    }
}
