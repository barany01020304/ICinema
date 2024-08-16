package com.example.graduat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.R
import com.example.graduat.databinding.FragmentChooseCinemaBinding


class ChooseCinemaFragment : Fragment() {
    lateinit var binding: FragmentChooseCinemaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_cinema, container, false)
        return binding.root
    }


}