package com.example.quanlychitieu.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.WalletTypeDropDownAdapter
import com.example.quanlychitieu.api.CreateWalletRequest
import com.example.quanlychitieu.databinding.DialogAddWalletBinding
import com.example.quanlychitieu.db.modeldb.WalletType
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalStateException

class AddingWalletDialog(val listWalletType: List<WalletType>) : DialogFragment() {

    lateinit var binding: DialogAddWalletBinding
    lateinit var walletTypeDropDownAdapter: WalletTypeDropDownAdapter
    lateinit var viewModel: HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_wallet, null, false)
            viewModel = (requireActivity() as HomeActivity).homeViewModel
            setUpDataForAutoCompleteText()
            setOnclickCancelBtn()
            setOnclickAddBtn()
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity must not empty")
    }

    fun setUpDataForAutoCompleteText() {
        walletTypeDropDownAdapter = WalletTypeDropDownAdapter(requireContext(), listWalletType)
        binding.walletTypeSpinner.adapter = walletTypeDropDownAdapter
    }

    fun setOnclickCancelBtn() {
        binding.cancelDialogAddBtn.setOnClickListener {
            dialog?.cancel()
        }
    }

    fun setOnclickAddBtn() {
        binding.addWalletBtn.setOnClickListener {
            val sharePreference =
                requireActivity().getSharedPreferences(
                    "com.example.quanlychitieu",
                    Context.MODE_PRIVATE
                )
            val token = sharePreference.getString("accountToken", "null")
            CoroutineScope(Dispatchers.Default).launch {
                val loadingDialog = LoadingDialog()
                loadingDialog.show(requireActivity().supportFragmentManager,"loading dialog")
                val walletType = binding.walletTypeSpinner.selectedItem as WalletType
                val result = viewModel.createWallet(
                    token!!, CreateWalletRequest(
                        binding.amountTextInputEditText.text.toString(),
                        walletType.idWalletType.toString()
                    )
                ).await()
                if(result[0].equals("200")){
                    viewModel.setListWallet(token!!)
                    loadingDialog.cancelDialog()
                    dialog?.cancel()
                }
                else{
                    loadingDialog.cancelDialog()
                    Snackbar.make(binding.root,result[1],Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}