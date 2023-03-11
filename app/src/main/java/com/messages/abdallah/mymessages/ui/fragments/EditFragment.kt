package com.messages.abdallah.mymessages.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.messages.abdallah.mymessages.R

class EditFragment : Fragment() {

     var MsgTypes_name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MsgTypes_name = EditFragmentArgs.fromBundle(requireArguments()).msgname
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), MsgTypes_name, Toast.LENGTH_LONG).show()
        val editText = view.findViewById<EditText>(R.id.ssss)

        editText.setText(MsgTypes_name)
    }


}