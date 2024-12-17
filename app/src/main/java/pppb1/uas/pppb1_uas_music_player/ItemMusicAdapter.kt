package pppb1.uas.pppb1_uas_music_player

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pppb1.uas.pppb1_uas_music_player.databinding.ItemMusicBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiService

typealias onClickMusic = (Musics) -> Unit

class ItemMusicAdapter(
    private val listMusics: ArrayList<Musics>,
    private val client: ApiService,
    private val onClickMusic: onClickMusic,
) : RecyclerView.Adapter<ItemMusicAdapter.ItemLatihanViewHolder>() {
    inner class ItemLatihanViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Musics) {
            with(binding) {
                txtJudulLagu.text = data.song_name
                txtArtis.text = data.artist

                itemView.setOnClickListener {
                    onClickMusic(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemLatihanViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemLatihanViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listMusics.size
    }

    override fun onBindViewHolder(holder: ItemLatihanViewHolder, position: Int) {
        holder.bind(listMusics[position])
    }

}