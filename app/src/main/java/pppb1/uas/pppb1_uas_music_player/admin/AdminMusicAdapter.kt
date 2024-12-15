package pppb1.uas.pppb1_uas_music_player.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pppb1.uas.pppb1_uas_music_player.DetailActivity
import pppb1.uas.pppb1_uas_music_player.R
import pppb1.uas.pppb1_uas_music_player.databinding.ItemDialogBinding
import pppb1.uas.pppb1_uas_music_player.databinding.MusicItemBinding
import pppb1.uas.pppb1_uas_music_player.databinding.MusicItemDashboardBinding
import pppb1.uas.pppb1_uas_music_player.model.Musics
import pppb1.uas.pppb1_uas_music_player.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

typealias OnClickMusics = (Musics) -> Unit
class AdminMusicAdapter(
    private val listMusics: ArrayList<Musics>,
    private val client: ApiService,
    private val onClickMusics: OnClickMusics
): RecyclerView.Adapter<AdminMusicAdapter.ItemMusicViewHolder>(){
    inner class ItemMusicViewHolder
        (private val binding: MusicItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Musics) {
                with(binding) {
                    judulLagu.text = data.judul
                    itemView.setOnClickListener{
                        onClickMusics(data)
                    }
                    btnEdit.setOnClickListener{
                        val intentEdit = Intent(itemView.context, AdminUpdateActivity::class.java)
                        intentEdit.putExtra("id", data.id)
                        intentEdit.putExtra("judul", data.judul)
                        intentEdit.putExtra("artis", data.artis)
                        intentEdit.putExtra("album", data.album)
                        intentEdit.putExtra("rilis", data.rilis)
                        itemView.context.startActivity(intentEdit)
                    }
                    btnDelete.setOnClickListener{
                        val dialogBinding =
                            ItemDialogBinding.inflate(LayoutInflater.from(itemView.context))
                        val dialog = AlertDialog.Builder(itemView.context)
                            .setView(dialogBinding.root)
                            .create()
                        dialogBinding.dialogTitle.text = "Konfirmasi Hapus"
                        dialogBinding.dialogMessage.text =
                            "Apakah Anda yakin ingin menghapus latihan ${data.judul}?"

                        dialogBinding.btnConfirm.setOnClickListener {
                            val response = data.id?.let { it1 -> client.deleteMusic(it1) }
                            response?.enqueue(object : Callback<Musics> {
                                override fun onResponse(
                                    call: Call<Musics>,
                                    response: Response<Musics>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            itemView.context,
                                            "data musik ${data.judul} berhasil dihapus",
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
            MusicItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMusicViewHolder(binding)
    }

    override fun getItemCount(): Int = listMusics.size

    override fun onBindViewHolder(holder: ItemMusicViewHolder, position: Int) {
        holder.bind(listMusics[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int) {
        listMusics.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, listMusics.size)
    }
}

