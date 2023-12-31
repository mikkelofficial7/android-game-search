package com.ewide.test.mikkel.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.ewide.test.mikkel.base.state.FailureState

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    internal var viewBinding: VB? = null
    private var mToolbar: Toolbar? = null

    private lateinit var progress: Dialog
    private lateinit var rootView: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = getUiBinding()
        setContentView(viewBinding?.root)

        rootView = window.decorView.findViewById(android.R.id.content)
        initProgressDialog()
        setupToolbar()
        onFirstLaunch(savedInstanceState)
        initUiListener()
    }

    abstract fun bindToolbar(): Toolbar?
    abstract fun enableBackButton(): Boolean
    abstract fun getUiBinding(): VB
    abstract fun onFirstLaunch(savedInstanceState: Bundle?)
    abstract fun initUiListener()

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> false
    }

    private fun initProgressDialog() {
        if (!::progress.isInitialized) {
            progress = Dialog(this)
            val inflate = LayoutInflater.from(this).inflate(R.layout.progress_loading, null)
            progress.setContentView(inflate)
            progress.setCancelable(false)
            progress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun setupToolbar() {
        bindToolbar()?.let {
            mToolbar = it
            setSupportActionBar(mToolbar)

            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(enableBackButton())
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            }
        }
    }

    fun showProgressDialog() {
        if (!progress.isShowing) progress.show()
    }

    fun hideProgressDialog() {
        if (progress.isShowing) progress.dismiss()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    open fun handleFailure(failureState: FailureState?) {
        when (failureState) {
            is FailureState.NetworkConnection -> showToast(getString(R.string.error_network))
            is FailureState.ServerError -> showToast(getString(R.string.error_server))
            is FailureState.DataNotFound -> showToast(getString(R.string.error_not_found))
            is FailureState.Other -> showToast("${failureState.code} - ${failureState.message}")
            else -> showToast(getString(R.string.error_network))
        }

    }

}