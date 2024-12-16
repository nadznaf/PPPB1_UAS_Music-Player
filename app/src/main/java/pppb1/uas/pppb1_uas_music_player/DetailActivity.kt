package pppb1.uas.pppb1_uas_music_player

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.uas.pppb1_uas_music_player.database.LibraryDao
import pppb1.uas.pppb1_uas_music_player.database.LibraryRoomDatabase
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityDetailBinding
import pppb1.uas.pppb1_uas_music_player.databinding.ItemDialogBinding
import pppb1.uas.pppb1_uas_music_player.model.Library
import pppb1.uas.pppb1_uas_music_player.model.Musics
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var executorService: ExecutorService
    private lateinit var LibraryDao: LibraryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val libraryRoomDatabase = LibraryRoomDatabase.getDatabase(applicationContext)
        LibraryDao = libraryRoomDatabase?.libraryDao()!!

        with(binding) {
            songName.text = intent.getStringExtra("judul")
            artist.text = intent.getStringExtra("artis")
            album.text = intent.getStringExtra("album")
            rilis.text = intent.getStringExtra("rilis")

            ivBack.setOnClickListener{
                finish()
            }
            btnFav.setOnClickListener {
                showFavoriteDialog()
            }
        }
    }

    private fun insert(library: Library) {
        executorService.execute {
            LibraryDao.insert(library)

            runOnUiThread {
                LibraryDao.getAllLibrary().observe(this@DetailActivity, { musicList ->
                    musicList?.forEach {
                        music -> Log.d("Music Inserted", "ID: ${music.id}, Name: ${music.judul}")
                    }
                })
            }
        }
    }

    private fun showFavoriteDialog() {
        val dialogBinding = ItemDialogBinding.inflate(LayoutInflater.from(this@DetailActivity))

        val dialog = AlertDialog.Builder(this@DetailActivity)
            .setView(dialogBinding.root)
            .create()
        dialogBinding.dialogIcon.setImageResource(R.drawable.like_full)
        dialogBinding.dialogTitle.text = "Tambah ke Favorit"
        dialogBinding.dialogMessage.text =
            "Apakah Anda yakin ingin menambahkan lagu ini ke favorit?"
        dialogBinding.btnConfirm.setOnClickListener {
            val library = Library(
                id = intent.getStringExtra("id").toString(),
                judul = intent.getStringExtra("judul").toString(),
                artis = intent.getStringExtra("artis").toString(),
                album = intent.getStringExtra("album").toString(),
                rilis = intent.getStringExtra("rilis").toString()
            )
            insert(library)
            dialog.dismiss()
            Toast.makeText(this@DetailActivity, "Lagu berhasil ditambahkan ke favorit", Toast.LENGTH_SHORT)
                .show()
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}