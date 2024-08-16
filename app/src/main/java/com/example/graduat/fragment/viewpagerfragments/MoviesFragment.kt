package com.example.graduat.fragment.viewpagerfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduat.R
import com.example.graduat.data.MovieRecycleView
import com.example.graduat.databinding.FragmentMoviesBinding
import com.example.graduat.fragment.MovieCointanerFragment
import com.example.graduat.fragment.MovieRecycleRecommenation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoviesFragment : Fragment() {
 lateinit var binding:FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_movies,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var manager=requireActivity().supportFragmentManager

        val forYouAdapter = MovieRecycleView(cinemaMovie(),manager, MovieCointanerFragment())
        binding.cinemaRecyclerView.adapter = forYouAdapter
        binding.cinemaRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

    }
    private fun cinemaMovie(): List<MovieRecycleRecommenation> {
        val cinemaMovie = listOf(
            MovieRecycleRecommenation(R.drawable.poster_recycle.toString(), "Item 1","alex",R.drawable.cinemaone.toString()),
            MovieRecycleRecommenation(R.drawable.poster_recycle.toString(), "Item 2","alex",R.drawable.cinemaone.toString()),
            MovieRecycleRecommenation(R.drawable.poster_recycle.toString(), "Item 3","alex",R.drawable.cinemaone.toString()),
            MovieRecycleRecommenation(R.drawable.poster_recycle.toString(), "Item 4","alex",R.drawable.cinemaone.toString()),
            MovieRecycleRecommenation(R.drawable.poster_recycle.toString(), "Item 5","alex",R.drawable.cinemaone.toString()),
        )
        return cinemaMovie
    }

}

