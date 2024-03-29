package com.amanpreet.notesapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.amanpreet.notesapp.databinding.FragmentLoginBinding
import com.amanpreet.notesapp.databinding.FragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var auth = Firebase.auth
    lateinit var binding: FragmentRegisterBinding
    lateinit var registrationActivity: RegistrationActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationActivity = activity as RegistrationActivity
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
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            if(binding.etEmail.text.toString().isNullOrEmpty()){
                binding.etEmail.error = "Enter email"
            } else if(binding.etPassword.text.toString().isNullOrEmpty()){
                binding.etPassword.error = "Enter password"
            } else if(binding.etPassword.text.toString().length <6){
                binding.etPassword.error = "Password should be of 6 characters"
            } else {
                auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(),
                    binding.etPassword.text.toString())
                    .addOnSuccessListener {
                        AlertDialog.Builder(registrationActivity).apply {
                            setTitle("Congratulations")
                            setMessage("You are registered successfully")
                            setPositiveButton("Login"){_,_->
                                findNavController().popBackStack()
                            }
                            show()
                        }
                    }.addOnFailureListener{
                        Toast.makeText(registrationActivity, "Sorry", Toast.LENGTH_LONG).show()
                    }
            }
        }

        binding.tvLogin.setOnClickListener{
            findNavController().popBackStack()
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
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}