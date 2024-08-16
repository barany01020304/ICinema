package com.example.graduat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.graduat.R
import com.example.graduat.data.CinemaViewRecycle
import com.example.graduat.databinding.FragmentCinemaBinding
import com.example.graduat.fragment.viewpagerfragments.MoviesFragment
import com.example.graduat.fragment.viewpagerfragments.PostFragment


class CinemaFragment : Fragment() {
    lateinit var binding: FragmentCinemaBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cinema, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.xButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.cinemaImage.setImageResource(requireArguments().getInt("movie_image"))
        binding.cinameName.setText(arguments?.getString("movie_name"))
        val cinemaViewRecycleAdapter =
            CinemaViewRecycle(arguments?.getIntegerArrayList("cinema_view")!!)
        binding.cinemaViewRecycle.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.cinemaViewRecycle.adapter = cinemaViewRecycleAdapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.cinemaViewRecycle)
     //   var supportFragmentManager= requireActivity().supportFragmentManager
        binding.tab.setupWithViewPager(binding.viewPager)

        binding.viewPager.adapter = Pager(requireActivity().supportFragmentManager)

    }
}
        class Pager(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {
            override fun getCount(): Int {
                return 3
            }


            override fun getItem(position: Int): Fragment {
                var fragment: Fragment? = MoviesFragment()
                when (position) {
                    0 -> {
                        fragment = MoviesFragment()

                    }
                    1 -> {
                        fragment = ReelFragment()
                    }

                    2 -> {
                        fragment = PostFragment()

                    }

                }
                return fragment!!

            }

            override fun getPageTitle(position: Int): CharSequence? {
                var text: String? =null
                when(position){
                    0->text="Movies"
                    1->text="Reels"
                    2->text="Posts"
                }
                return text
            }



        }
