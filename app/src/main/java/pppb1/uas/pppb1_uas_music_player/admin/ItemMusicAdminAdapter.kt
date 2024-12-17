package pppb1.uas.pppb1_uas_music_player.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pppb1.uas.pppb1_uas_music_player.databinding.ItemDialogBinding
import pppb1.uas.pppb1_uas_music_player.databinding.ItemMusicAdminBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

typealias OnClickMusics = (Musics) -> Unit

class ItemMusicAdminAdapter(
    private val listMusics: ArrayList<Musics>,
    private val client: ApiService,
    private val onClickMusics: OnClickMusics,
) : RecyclerView.Adapter<ItemMusicAdminAdapter.ItemMusicViewHolder>() {
    inner class ItemMusicViewHolder(private val binding: ItemMusicAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Musics) {
            with(binding) {
                txtJudulLagu.text = data.song_name
                itemView.setOnClickListener {
                    onClickMusics(data)
                }

                btnEdit.setOnClickListener {
                    val intentEdit = Intent(itemView.context, AdminUpdateActivity::class.java)
                    intentEdit.putExtra("id", data.id)
                    intentEdit.putExtra("song_name", data.song_name)
                    intentEdit.putExtra("artist", data.artist)
                    intentEdit.putExtra("album_name", data.album_name)
                    intentEdit.putExtra("release", data.release)

                    itemView.context.startActivity(intentEdit)
                }
                btnDelete.setOnClickListener {
                    val dialogBinding =
                        ItemDialogBinding.inflate(LayoutInflater.from(itemView.context))
                    val dialog = AlertDialog.Builder(itemView.context)
                        .setView(dialogBinding.root)
                        .create()
                    dialogBinding.dialogTitle.text = "Konfirmasi Hapus"
                    dialogBinding.dialogMessage.text =
                        "Apakah Anda yakin ingin menghapus lagu ${data.song_name}?"

                    dialogBinding.btnConfirm.setOnClickListener {
                        val response = data.id?.let { it1 -> client.deleteMusics(it1) }
                        response?.enqueue(object : Callback<Musics> {
                            override fun onResponse(
                                call: Call<Musics>,
                                response: Response<Musics>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        itemView.context,
                                        "data lagu ${data.song_name} berhasil dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val position = adapterPosition
                                    if (position != RecyclerView.NO_POSITION) {
                                        removeItem(position)
                                    }
                                    dialog.dismiss()
                                } else {
                                    Log.e("API Error", "Response not successful or body is null")
                                }
                            }

                            override fun onFailure(call: Call<Musics>, t: Throwable) {
                                Toast.makeText(itemView.context, "koneksi eror", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                    }
                    dialogBinding.btnCancel.setOnClickListener {
                        dialog.dismiss() // Dismiss the dialog when canceled
                    }

                    dialog.show() // Show the dialog
                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMusicViewHolder {

        val binding =
            ItemMusicAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMusicViewHolder(binding)
    }

    override fun getItemCount(): Int = listMusics.size


    override fun onBindViewHolder(holder: ItemMusicViewHolder, position: Int) {
        holder.bind(listMusics[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Musics>) {
        listMusics.clear()
        listMusics.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        listMusics.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, listMusics.size)
    }
}