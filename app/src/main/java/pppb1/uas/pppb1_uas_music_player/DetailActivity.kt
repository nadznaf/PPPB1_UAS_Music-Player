package pppb1.uas.pppb1_uas_music_player

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pppb1.uas.pppb1_uas_music_player.database.FavoriteDao
import pppb1.uas.pppb1_uas_music_player.database.FavoriteRoomDatabase
import pppb1.uas.pppb1_uas_music_player.databinding.ActivityDetailBinding
import pppb1.uas.pppb1_uas_music_player.databinding.ItemDialogBinding
import pppb1.uas.pppb1_uas_music_player.model.Favorite
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var executorService: ExecutorService
    private lateinit var favoriteDao: FavoriteDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val favoriteRoomDatabase = FavoriteRoomDatabase.getDatabase(applicationContext)
        favoriteDao = favoriteRoomDatabase?.favoriteDao()!!

        with(binding) {
            txtJudulLagu.text = intent.getStringExtra("song_name")
            txtArtist.text = intent.getStringExtra("artist")
            txtAlbum.text = intent.getStringExtra("album_name")
            txtRilis.text = intent.getStringExtra("release")

            ivBack.setOnClickListener {
                finish()
            }
            btnFavorite.setOnClickListener {
                showFavoriteDialog()
            }
        }
    }

    private fun insert(favorite: Favorite) {
        executorService.execute {
            favoriteDao.insert(favorite)

            // Move observation to the main thread
            runOnUiThread {
                // Observe the LiveData on the main thread
                favoriteDao.getAllFavorite().observe(this@DetailActivity, { musicList ->
                    // This will be triggered when the data changes or is initially available
                    musicList?.forEach { favorite ->
                        Log.d("Favorite Inserted", "ID: ${favorite.id}, Name: ${favorite.song_name}")
                    }
                })
            }
        }
    }

    private fun showFavoriteDialog() {
        val dialogBinding = ItemDialogBinding.inflate(LayoutInflater.from(this))

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()
        dialogBinding.dialogIcon.setImageResource(R.drawable.like_full)
        dialogBinding.dialogTitle.text = "Tambah ke Favorit"
        dialogBinding.dialogMessage.text =
            "Apakah Anda yakin ingin menambahkan lagu ini ke favorit?"

        dialogBinding.btnConfirm.setOnClickListener {
            // Insert the item into the database
            val favorite = Favorite(
                id = intent.getStringExtra("id").toString(),
                song_name = intent.getStringExtra("song_name").toString(),
                artist = intent.getStringExtra("artist").toString(),
                album_name = intent.getStringExtra("album_name").toString(),
                release = intent.getStringExtra("release").toString(),
            )
            insert(favorite)
            dialog.dismiss() // Dismiss the dialog after confirming
            Toast.makeText(this, "Lagu berhasil ditambahkan ke favorit", Toast.LENGTH_SHORT)
                .show()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog if canceled
        }

        dialog.show() // Show the dialog
    }

}