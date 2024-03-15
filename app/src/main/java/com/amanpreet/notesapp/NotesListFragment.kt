package com.amanpreet.notesapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amanpreet.notesapp.databinding.FragmentNotesListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesListFragment : Fragment(), NotesClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentNotesListBinding
    lateinit var adapter: NotesAdapter
    var notesList = ArrayList<Notes>()
    var db = Firebase.firestore
    var auth = Firebase.auth
    lateinit var mainActivity : MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        adapter = NotesAdapter(notesList, this)

        db.collection("notes")
            .addSnapshotListener(object : EventListener<QuerySnapshot?> {
                override fun onEvent(
                     snapshots: QuerySnapshot?,
                     e: FirebaseFirestoreException?
                ) {
                    if (e != null) {
                        Log.w("TAG", "listen:error", e)
                        return
                    }
                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED-> {
                                var notes = dc.document.toObject(Notes::class.java)
                                notes.id = dc.document.id
                                notesList.add(notes)
                                adapter.notifyDataSetChanged()
                            }
                            DocumentChange.Type.MODIFIED-> {
                                var notes = dc.document.toObject(Notes::class.java)
                                notes.id = dc.document.id
                                var index = notesList.indexOfFirst { element-> element.id == notes.id }
                                notesList.set(index,notes)
                                adapter.notifyDataSetChanged()
                            }
                            DocumentChange.Type.REMOVED-> {
                                var notes = dc.document.toObject(Notes::class.java)
                                notes.id = dc.document.id
                                notesList.removeIf { element-> element.id == notes.id }
                                adapter.notifyDataSetChanged()
                            }

                            else->{}
                        }
                    }
                }


            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvNotes.layoutManager = LinearLayoutManager(mainActivity)
        binding.rvNotes.adapter = adapter
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.addUpdateNotesFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotesListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun editClick(notes: Notes) {
        findNavController().navigate(R.id.addUpdateNotesFragment, bundleOf("notesId" to notes.id))

    }

    override fun deleteClick(notes: Notes) {
        AlertDialog.Builder(mainActivity).apply {
            setTitle("Delete Note")
            setMessage("Do you want to delete Note?")
            setPositiveButton("Yes"){_,_->
                db.collection("notes").document(notes.id?:"").delete().addOnSuccessListener {
                    Toast.makeText(mainActivity, "Notes Deleted", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}