package com.example.note.ui

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseFragment(layoutId:Int): Fragment(layoutId) {
        fun showSnackbar(text: String){
            Snackbar.make(
                requireActivity().rootLayout,
                text,
                Snackbar.LENGTH_LONG
            ).show()
        }
}