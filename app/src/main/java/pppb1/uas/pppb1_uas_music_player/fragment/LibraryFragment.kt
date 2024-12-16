package pppb1.uas.pppb1_uas_music_player.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pppb1.uas.pppb1_uas_music_player.DetailActivity
import pppb1.uas.pppb1_uas_music_player.ItemFavoriteAdapter
import pppb1.uas.pppb1_uas_music_player.database.LibraryDao
import pppb1.uas.pppb1_uas_music_player.database.LibraryRoomDatabase
import pppb1.uas.pppb1_uas_music_player.databinding.FragmentLibraryBinding
import pppb1.uas.pppb1_uas_music_player.model.Library
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 * Use the [LibraryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LibraryFragment : Fragment() {
    private lateinit var binding : FragmentLibraryBinding
    private lateinit var adapter : ItemFavoriteAdapter
    private lateinit var  libraryDao : LibraryDao
    private lateinit var executorService: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()
        val db = LibraryRoomDatabase.getDatabase(requireContext())
        libraryDao = db!!.libraryDao()
        setupRecyclerView()

        return binding.root
    }

    private fun deleteFavorite(library: Library) {
        executorService.execute {
            libraryDao.delete(library)
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Berhasil menghapus musik dari favorit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        adapter = ItemFavoriteAdapter(requireContext(),
            deleteAction = { data ->
                deleteFavorite(data) // Handle delete action
            },
            LibraryDetail = { data ->
                // Navigate to detail activity
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra("id", data.id)
                    putExtra("judul", data.judul)
                    putExtra("artis", data.artis)
                    putExtra("album", data.album)
                    putExtra("rilis", data.rilis)
                }
                startActivity(intent)
            }
        )
        binding.rvFavorite.adapter = adapter

        libraryDao.getAllLibrary().observe(viewLifecycleOwner) { dataList ->
            adapter.submitList(dataList) // Perbarui dataset
        }
    }
}