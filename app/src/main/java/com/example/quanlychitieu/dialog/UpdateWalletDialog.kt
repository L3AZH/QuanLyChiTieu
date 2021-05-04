package com.example.quanlychitieu.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.CreateWalletRequest
import com.example.quanlychitieu.api.UpdateWalletRequest
import com.example.quanlychitieu.api.WalletInfo
import com.example.quanlychitieu.databinding.DialogUpdateWalletBinding
import com.example.quanlychitieu.db.modeldb.WalletType
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class UpdateWalletDialog(val wallet: WalletInfo) : DialogFragment() {

    lateinit var binding: DialogUpdateWalletBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_update_wallet, null, false)
            viewModel = (requireActivity() as HomeActivity).homeViewModel
            binding.amountUpdateTextInputEditText.setText(wallet.amount)
            setOnclickCancelBtn()
            setOnclickSaveBtn()
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity must not empty")
    }

    fun setOnclickCancelBtn() {
        binding.cancelDialogUpdateBtn.setOnClickListener {
            dialog?.cancel()
        }
    }

    fun setOnclickSaveBtn() {
        binding.saveUpdateWalletBtn.setOnClickListener {
            val sharePreference =
                requireActivity().getSharedPreferences(
                    "com.example.quanlychitieu",
                    Context.MODE_PRIVATE
                )
            val token = sharePreference.getString("accountToken", "null")
            CoroutineScope(Dispatchers.Default).launch {
                val loadingDialog = LoadingDialog()
                loadingDialog.show(requireActivity().supportFragmentManager, "loading dialog")
                val result = viewModel.updateWallet(
                    token!!,
                    wallet.type,
                    UpdateWalletRequest(binding.amountUpdateTextInputEditText.text.toString())
                ).await()
                loadingDialog.cancelDialog()
                if (result[0].equals("200")) {
                    viewModel.setListWallet(token!!)
                    dialog?.cancel()
                } else {
                    Snackbar.make(binding.root, result[1], Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}