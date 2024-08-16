package com.example.graduat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.R
import com.example.graduat.data.ChairGrid
import com.example.graduat.databinding.FragmentChairBinding


class ChairFragment : Fragment() {

    lateinit var binding: FragmentChairBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chair, container, false)
        return binding.root
    }
 val bb = mutableListOf("","")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chairGrid.adapter=ChairGrid(requireContext())

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.nextBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out

//                R.anim.slide_in_right,
//                R.anim.slide_out_left,
//                R.anim.slide_in_right,
//                R.anim.slide_out_left
            ).replace(R.id.book_activity_container, PayFragment()).addToBackStack("chairFragment")
                .commit()
        }
        binding.chairGrid.setOnItemClickListener { adapterView, view, i, l ->
            view.setBackgroundResource(R.drawable.yellow_chair)
          //  Toast.makeText(requireContext(),"hi$i",Toast.LENGTH_SHORT).show()
        }
    }


    }



