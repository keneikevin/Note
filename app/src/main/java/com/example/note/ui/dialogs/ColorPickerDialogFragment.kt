package com.example.note.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.note.databinding.EditTextEmailBinding
import com.example.note.databinding.FragmentAuthBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.databinding.LayoutDialogColorpickerBinding
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class ColorPickerDialogFragment:DialogFragment() {
    private var _binding: LayoutDialogColorpickerBinding? = null
    //This property is only valid between onCreateDialog and
    //onDestroy
    private val binding get() = _binding
    private var positiveListener: ((String) -> Unit)? = null

   fun setPositiveListener(listener:(String) -> Unit){
       positiveListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = LayoutDialogColorpickerBinding.inflate(LayoutInflater.from(context))
        return ColorPickerDialog.Builder(requireContext())
            .setTitle("Choose a color")
            .setPositiveButton("Ok", object : ColorEnvelopeListener{
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    positiveListener?.let { yes ->
                        envelope?.let { yes(it.hexCode) }
                    }
                }
            }).setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .setBottomSpace(12)
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .create()
    }
}