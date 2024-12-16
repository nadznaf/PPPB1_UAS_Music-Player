package pppb1.uas.pppb1_uas_music_player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import pppb1.uas.pppb1_uas_music_player.R
import pppb1.uas.pppb1_uas_music_player.auth.PrefManager
import pppb1.uas.pppb1_uas_music_player.databinding.FragmentProfileBinding
import pppb1.uas.pppb1_uas_music_player.databinding.ItemDialogBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val prefManager = PrefManager.getInstance(requireContext())
        val username = prefManager.getUsername()
        val email = prefManager.getEmail()
        with(binding){
            txtUsername.text = username
            txtEmail.text = email

            btnLogout.setOnClickListener{
                showLogoutDialog()
            }

        }
        return (binding.root)
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        val prefManager = PrefManager.getInstance(requireContext())
        val inflate = requireActivity().layoutInflater
        val binding = ItemDialogBinding.inflate(inflate)
        val dialog = builder.setView(binding.root)
            .setCancelable(false) // Make dialog non-cancelable by tapping outside
            .create()

        with(binding){
            dialogTitle.text = "Logout"
            dialogMessage.text = "Apakah Anda yakin ingin logout?"
            btnConfirm.text = "Ya"
            btnConfirm.setOnClickListener {
                prefManager.clear()
                requireActivity().finish()
            }
            btnCancel.text = "Tidak"
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()

    }

}