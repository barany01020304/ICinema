package com.example.graduat.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.MainActivity
import com.example.graduat.R
import com.example.graduat.databinding.FragmentBookSuccesBinding


class BookSuccesFragment : Fragment() {
lateinit var binding: FragmentBookSuccesBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_succes, container, false)        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.home.setOnClickListener {
            val intent= Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
//                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
//                    .commit()


        }

    }


}