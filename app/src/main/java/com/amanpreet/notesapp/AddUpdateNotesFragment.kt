package com.amanpreet.notesapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amanpreet.notesapp.databinding.FragmentAddUpdateNotesBinding
import com.amanpreet.notesapp.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddUpdateNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddUpdateNotesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var db = Firebase.firestore
    var auth = Firebase.auth
    lateinit var binding: FragmentAddUpdateNotesBinding
    lateinit var mainActivity: MainActivity
    var notesId : String = ""
    var notes = Notes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUpdateNotesBinding.inflate(layoutInflater)

        arguments?.let {
            notesId = it.getString("notesId")?:""
            if(notesId.isEmpty() == false){
                db.collection("notes").document(notesId).get().addOnSuccessListener {
                    notes = it.toObject(Notes::class.java)?: Notes()

                    binding.etTitle.setText(notes.title)
                    binding.etDescription.setText(notes.description)
                    binding.btnAddNotes.setText("Update Notes")
                    when(notes.priority){
                        3-> binding.rbHigh.isChecked = true
                        2-> binding.rbMedium.isChecked = true
                        else-> binding.rbLow.isChecked = true
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddNotes.setOnClickListener {
            if(binding.etTitle.text.toString().isNullOrEmpty()){
                binding.etTitle.error = "Enter title"
            } else if(binding.etDescription.text.toString().isNullOrEmpty()){
                binding.etDescription.error = "Enter description"
            } else {
                var priority = when(binding.rbPriority.checkedRadioButtonId){
                    R.id.rbHigh -> 3
                    R.id.rbMedium -> 2
                    else->1
                }
                notes.title = binding.etTitle.text.toString()
                notes.description = binding.etDescription.text.toString()
                notes.userId = auth.currentUser?.uid
                notes.priority = priority

                if(notesId.isEmpty()){
                    notes.timestamp = Timestamp.now()
                    db.collection("notes").add(notes).addOnSuccessListener {
                        findNavController().popBackStack()
                    }
                }else{

                    db.collection("notes").document(notesId).set(notes).addOnSuccessListener {
                        findNavController().popBackStack()
                    }
                }

            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddUpdateNotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}