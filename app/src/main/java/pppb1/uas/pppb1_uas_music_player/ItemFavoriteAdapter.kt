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
import pppb1.uas.pppb1_uas_music_player.model.Library

class ItemFavoriteAdapter(
    private val Context: Context,
    private val deleteAction: (Library) -> Unit,
    private val LibraryDetail: (Library) -> Unit
) : ListAdapter<Library, ItemFavoriteAdapter.ItemFavoriteViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Library>() {
        override fun areItemsTheSame(oldItem: Library, newItem: Library): Boolean {
            return oldItem.id == newItem.id // Check if the IDs are the same
        }
        override fun areContentsTheSame(oldItem: Library, newItem: Library): Boolean {
            return oldItem == newItem // Check if the content is the same
        }
    }

    inner class ItemFavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Library) {
            with(binding) {
                txtJudulLagu.text = data.judul
                artis.text = data.artis

                imgLike.setOnClickListener {
                    val dialogBinding =
                        ItemDialogBinding.inflate(LayoutInflater.from(itemView.context))
                    val dialog = AlertDialog.Builder(itemView.context)
                        .setView(dialogBinding.root)
                        .create()
                    dialogBinding.dialogTitle.text = "Konfirmasi Hapus"
                    dialogBinding.dialogMessage.text =
                        "Apakah Anda yakin ingin menghapus lagu ${data.judul}?"

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
                    LibraryDetail(data)
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
