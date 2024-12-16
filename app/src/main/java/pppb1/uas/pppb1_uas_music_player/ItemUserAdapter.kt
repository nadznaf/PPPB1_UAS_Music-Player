package pppb1.uas.pppb1_uas_music_player

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pppb1.uas.pppb1_uas_music_player.databinding.ItemMusicBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiService

typealias onClickMusic = (Musics) -> Unit

@SuppressLint("ViewConstructor")
class ItemUserAdapter(
    private val listMusics: ArrayList<Musics>,
    private val client: ApiService,
    private val onClickMusic: onClickMusic,
) : RecyclerView.Adapter<ItemUserAdapter.ItemMusicViewHolder>() {
    inner class ItemMusicViewHolder(
        private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Musics) {
            with(binding) {
                judulLagu.text = data.judul
                artis.text = data.artis

                itemView.setOnClickListener{
                    onClickMusic(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMusicViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMusicViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  listMusics.size
    }

    override fun onBindViewHolder(holder: ItemMusicViewHolder, position: Int) {
        holder.bind(listMusics[position])
    }

}
