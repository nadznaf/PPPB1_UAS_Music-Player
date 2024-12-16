package pppb1.uas.pppb1_uas_music_player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pppb1.uas.pppb1_uas_music_player.databinding.ItemMusicBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiService

typealias onClickMusics = (Musics) -> Unit

class HomeMusicAdapter(
    private val listMusics: ArrayList<Musics>,
    private val client: ApiService,
    private val onClickMusics: onClickMusics,
) : RecyclerView.Adapter<HomeMusicAdapter.ItemMusicViewHolder>() {
    inner class ItemMusicViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Musics) {
                with(binding) {
                    judulLagu.text = data.judul
                    artis.text = data.artis

                    itemView.setOnClickListener {
                        onClickMusics(data)
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
