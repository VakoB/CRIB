package com.example.mysocialmedia.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mysocialmedia.R
import com.example.mysocialmedia.ui.fragments.activities.BaseActivity
import com.google.android.material.snackbar.Snackbar


open class BaseFragment : Fragment(R.layout.fragment_base) {
    private var doubleBackToExitOnce = false
    private lateinit var mProgressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackBar = view?.let {
            Snackbar.make(
                it.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG)
        }
        val snackBarView = snackBar?.view


        if (errorMessage){
            snackBarView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.Error
                )
            )
        }
        else{

            snackBarView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.Success
                )
            )

        }
        snackBar?.show()
    }
    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.findViewById<TextView>(R.id.progressText).text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }


}