package pppb1.uas.pppb1_uas_music_player

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pppb1.uas.pppb1_uas_music_player.databinding.ItemDialogBinding
import pppb1.uas.pppb1_uas_music_player.databinding.ItemFavoriteBinding
import pppb1.uas.pppb1_uas_music_player.model.Favorite

class ItemFavoriteAdapter(private val Context: Context,
                          private val deleteAction: (Favorite) -> Unit,
                          private val favoriteDetail: (Favorite) -> Unit
) : ListAdapter<Favorite, ItemFavoriteAdapter.ItemFavoriteViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.id == newItem.id // Check if the IDs are the same
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem // Check if the content is the same
        }
    }

    inner class ItemFavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Favorite) {
            with(binding) {
                txtJudulLagu.text = data.song_name
                txtArtis.text = data.artist

                // Handle delete button click
                btnUnfavorite.setOnClickListener {
                    val dialogBinding =
                        ItemDialogBinding.inflate(LayoutInflater.from(itemView.context))
                    val dialog = AlertDialog.Builder(itemView.context)
                        .setView(dialogBinding.root)
                        .create()
                    dialogBinding.dialogTitle.text = "Konfirmasi Hapus"
                    dialogBinding.dialogMessage.text =
                        "Apakah Anda yakin ingin menghapus lagu ${data.song_name} dari favorit?"

                    dialogBinding.btnConfirm.setOnClickListener {
                        deleteAction(data)

                        dialog.dismiss()
                    }
                    dialogBinding.btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }
                itemView.setOnClickListener{
                    favoriteDetail(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFavoriteViewHolder, position: Int) {
        holder.bind(getItem(position)) // Only pass the data to bind
    }
}
