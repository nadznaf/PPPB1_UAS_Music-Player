package pppb1.uas.pppb1_uas_music_player.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pppb1.uas.pppb1_uas_music_player.FavoriteDetailActivity
import pppb1.uas.pppb1_uas_music_player.ItemFavoriteAdapter
import pppb1.uas.pppb1_uas_music_player.database.FavoriteDao
import pppb1.uas.pppb1_uas_music_player.database.FavoriteRoomDatabase
import pppb1.uas.pppb1_uas_music_player.databinding.FragmentFavoriteBinding
import pppb1.uas.pppb1_uas_music_player.model.Favorite
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteFragment : Fragment() {
    private lateinit var binding : FragmentFavoriteBinding
    private lateinit var adapter : ItemFavoriteAdapter
    private lateinit var  favoriteDao: FavoriteDao
    private lateinit var executorService: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()
        val db = FavoriteRoomDatabase.getDatabase(requireContext())
        favoriteDao = db!!.favoriteDao()
        setupRecyclerView()

        return binding.root
    }

    private fun deleteFavorite(favorite: Favorite) {
        executorService.execute {
            favoriteDao.delete(favorite)
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Berhasil menghapus lagu dari favorit", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupRecyclerView() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        adapter = ItemFavoriteAdapter(requireContext(),
            deleteAction = { data ->
                deleteFavorite(data) // Handle delete action
            },
            favoriteDetail = { data ->
                // Navigate to detail activity
                val intent = Intent(requireContext(), FavoriteDetailActivity::class.java).apply {
                    putExtra("id", data.id)
                    putExtra("song_name", data.song_name)
                    putExtra("artist", data.artist)
                    putExtra("album_name", data.album_name)
                    putExtra("release", data.release)
                }
                startActivity(intent)
            }
        )
        binding.rvFavorite.adapter = adapter

        // Amati perubahan data dari LiveData
        favoriteDao.getAllFavorite().observe(viewLifecycleOwner) { dataList ->
            adapter.submitList(dataList) // Perbarui dataset
        }
    }
}