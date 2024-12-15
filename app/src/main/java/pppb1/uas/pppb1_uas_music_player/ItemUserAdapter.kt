package pppb1.uas.pppb1_uas_music_player

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pppb1.uas.pppb1_uas_music_player.database.MusicaDatabase

class ItemUserAdapter(context: Context, private val itemList: List<Data>) : ArrayAdapter<Data>(context, R.layout.music_item, itemList) {
    private val libraryDao = MusicaDatabase.getInstance(context).libraryDao()

    private var onItemClickListener: ((Data) -> Unit)? = null

    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.music_item, parent, false)

        val musicItem = getItem(position)

        val gambarLagu = view.findViewById<ImageView>(R.id.gambar_lagu)
        val judulLagu = view.findViewById<TextView>(R.id.judul_lagu)
        val imgLike = view.findViewById<ImageView>(R.id.img_like)

        gambarLagu.setImageResource(R.drawable.music)
        judulLagu.text = musicItem?.judul ?: ""
        imgLike.setImageResource(R.drawable.like_line)

        view.setOnClickListener {
            musicItem?.let {
                onItemClickListener?.invoke(it)  // Custom click listener
            }
        }

        imgLike.setOnClickListener {
            musicItem?.let {
                val library = Library(
                    song_name = it.judul,
                    artist = it.artis,
                    album_name = it.album,
                    release = it.rilis,
                    picture = it.gambar
                )
                GlobalScope.launch(Dispatchers.IO) {
                    libraryDao.insertLibrary(library)
                    (context as? Activity)?.runOnUiThread {
                        Toast.makeText(context, "${it.judul} added to library", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }
}
