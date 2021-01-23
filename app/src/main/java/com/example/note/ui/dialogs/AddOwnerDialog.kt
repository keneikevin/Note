package com.example.note.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.note.R
import com.example.note.databinding.EditTextEmailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_note_detail.*

class AddOwnerDialog:DialogFragment() {
    private var _binding: EditTextEmailBinding? = null
    //This property is only valid between onCreateDialog and
    //onDestroy
    private val binding get() = _binding
    private var positiveListener:((String) -> Unit)? = null

    fun setPositiveListener(listener: (String) -> Unit){
        positiveListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = EditTextEmailBinding.inflate(LayoutInflater.from(context))

        return MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_add_person)
            .setTitle("Add owner to note")
            .setMessage("Enter an E-Mail of a person you want to share the note with." +
                    "This person will be able to read and edit the note.")
            .setView(binding?.etAddOwnerEmail)
            .setPositiveButton("Add"){_,_ ->
                val email = binding?.etAddOwnerEmail?.findViewById<EditText>(R.id.etAddOwnerEmail)?.text.toString()
                positiveListener?.let { yes->
                    yes(email)
                }
            }
            .setNegativeButton("Cancel"){ dialogInterface, _ ->
        dialogInterface.cancel()
            }.create()
    }
}
