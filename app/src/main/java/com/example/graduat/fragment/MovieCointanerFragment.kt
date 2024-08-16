package com.example.graduat.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduat.Api.HomeApi
import com.example.graduat.Api.HomeApiInterface
import com.example.graduat.BookActivity
import com.example.graduat.R
import com.example.graduat.data.MovieRecycleView
import com.example.graduat.databinding.FragmentMovieCointanerBinding
import com.squareup.picasso.Picasso
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class MovieCointanerFragment : Fragment() {
    lateinit var binding: FragmentMovieCointanerBinding
    lateinit var retrofit: Retrofit
    lateinit var homeApi: HomeApiInterface
    lateinit var config: RealmConfiguration
    lateinit var realm: Realm
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_movie_cointaner,container,false)
        retrofit = Retrofit.Builder().baseUrl("http://icinema.live/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
         config = RealmConfiguration.create(schema = setOf(com.example.graduat.database.TicketData::class))
         realm = Realm.open(config)
        homeApi = retrofit.create(HomeApiInterface::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.xButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
      //  binding.movieImage.setImageResource(requireArguments().getInt("movie_image"))

        Picasso.get().load(requireArguments().getString("movie_image")).fit().placeholder(R.color.black)
            .into(binding.movieImage)
       // Glide.with(requireContext()).load(requireArguments().getString("movie_image")).into(binding.movieImage)
        binding.movieName.setText(arguments?.getString("movie_name"))
        binding.bookButton.setOnClickListener {

        }
        var manager=requireActivity().supportFragmentManager

        binding.relatedRecycle.adapter= MovieRecycleView(related(),manager,MovieCointanerFragment())
        binding.relatedRecycle.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        binding.bookButton.setOnClickListener {
            realm.writeBlocking {
                copyToRealm(com.example.graduat.database.TicketData().apply {
                    movieName = arguments?.getString("movie_name")?:"movieeror"
                    moviePicture = requireArguments().getString("movie_image").toString()
                    cinemaPicture= R.drawable.cinemaone.toString()
                    cinemaName = "alex"
                    starRating = "3.5"
                    starring = "ahmed barany"
                    releaseDate = "2000/5/4"
                    movieType = "enf"
                    movieDescription =
                        "sssssssssssssssssssdjjdjdjkjdjsjsdjdjjdjkdjkdjjjfjfdjfjjfjd"
                    VideoUrl =
                        "\"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4\""
                })
            }
            binding.upCointainer.removeAllViews()
          //  fragment.arguments=bundle
//            requireActivity().supportFragmentManager.beginTransaction().setCustomAnimations(
//                R.anim.slide_in_down,
//                R.anim.slide_out_top,
//                R.anim.slide_in_top,
//                R.anim.slide_out_down
//            ).replace(R.id.book_activity_container,DateFragment()).commitNow()
            val intent =Intent(this.activity,BookActivity::class.java)
            intent.putExtra("getItFroMCinema",arguments?.getString("getItFroMCinema"))
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_top)


        }

        val uri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp44")
        val mediaController = MediaController(this.requireContext())
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(uri)
        binding.videoView.requestFocus()
        binding.videoView.start()

    }
    @SuppressLint("SuspiciousIndentation")
    private fun related(): MutableList<MovieRecycleRecommenation> {
        val forYouItems = mutableListOf(
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 1", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 2", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 3", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 4", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 5", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 3", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 4", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 5", "alex", R.drawable.cinemaone.toString()
            ),)
            val call = homeApi.categories
                call.enqueue(object : Callback<HomeApi> {
                    override fun onResponse(call: Call<HomeApi>, response: Response<HomeApi>) {
                        forYouItems[0]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[0].image!!, response.body()?.data?.categories!![2].movies[0].name!!, response.body()?.data?.categories!![2].movies[0].cinema?.name!!, response.body()?.data?.categories!![2].movies[0].cinema?.logo!!)
                        forYouItems[1]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[1].image!!, response.body()?.data?.categories!![2].movies[1].name!!,  response.body()?.data?.categories!![2].movies[1].cinema?.name!!, response.body()?.data?.categories!![2].movies[1].cinema?.logo!!)
                        forYouItems[2]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[2].image!!, response.body()?.data?.categories!![2].movies[2].name!!,  response.body()?.data?.categories!![2].movies[2].cinema?.name!!, response.body()?.data?.categories!![2].movies[2].cinema?.logo!!)
                        forYouItems[3]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[3].image!!, response.body()?.data?.categories!![2].movies[3].name!!, response.body()?.data?.categories!![2].movies[3].cinema?.name!!, response.body()?.data?.categories!![2].movies[3].cinema?.logo!!)
                        forYouItems[4]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[4].image!!, response.body()?.data?.categories!![2].movies[4].name!!,  response.body()?.data?.categories!![2].movies[4].cinema?.name!!, response.body()?.data?.categories!![2].movies[4].cinema?.logo!!)
                        forYouItems[5]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[5].image!!, response.body()?.data?.categories!![2].movies[5].name!!,  response.body()?.data?.categories!![2].movies[5].cinema?.name!!, response.body()?.data?.categories!![2].movies[5].cinema?.logo!!)
                        forYouItems[6]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[6].image!!, response.body()?.data?.categories!![2].movies[6].name!!,  response.body()?.data?.categories!![2].movies[6].cinema?.name!!, response.body()?.data?.categories!![2].movies[6].cinema?.logo!!)
                        forYouItems[7]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[7].image!!, response.body()?.data?.categories!![2].movies[7].name!!,  response.body()?.data?.categories!![2].movies[7].cinema?.name!!, response.body()?.data?.categories!![2].movies[7].cinema?.logo!!)
                    }

                    override fun onFailure(call: Call<HomeApi>, t: Throwable) {

                    }

                })

        return forYouItems
    }

}

