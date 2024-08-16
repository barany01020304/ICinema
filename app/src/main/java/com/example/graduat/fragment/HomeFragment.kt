package com.example.graduat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.graduat.Api.HomeApi
import com.example.graduat.Api.HomeApiInterface
import com.example.graduat.R
import com.example.graduat.data.CinemaRecycleView
import com.example.graduat.data.MovieRecycleView
import com.example.graduat.data.SlideRecycleView
import com.example.graduat.database.PersonalData
import com.example.graduat.databinding.FragmentHomeBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {
    lateinit var retrofit: Retrofit
    lateinit var binding: FragmentHomeBinding
    lateinit var config: RealmConfiguration
    lateinit var realm: Realm
    lateinit var homeApi: HomeApiInterface
    lateinit var personlData: RealmResults<PersonalData>
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        retrofit = Retrofit.Builder().baseUrl("http://icinema.live/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        homeApi = retrofit.create(HomeApiInterface::class.java)


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        config = RealmConfiguration.create(schema = setOf(PersonalData::class))
        realm = Realm.open(config)
        personlData = realm.query<PersonalData>().find()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            binding.profilePhoto.setImageURI(personlData[0].accountPic.toUri())

        } catch (e: Exception) {
//
        }
        binding.refreshLayout.setColorSchemeColors(
            this.requireContext().getColor(R.color.theme_color)
        )
        binding.refreshLayout.setProgressBackgroundColorSchemeColor(
            this.requireContext().getColor(R.color.view_color)
        )
        binding.profilePhoto.setOnClickListener {
            setFragment(MyProfileFragment())
        }
        //first item in main Fragment
        //to move element from one fragment to another
        var manager = requireActivity().supportFragmentManager
        var movieCointanerFragment = MovieCointanerFragment()
        var cinemaCointanerFragment = CinemaFragment()
        //slide
        val slideAdapter = SlideRecycleView(slideItems(), manager, movieCointanerFragment)
        binding.slideRecycle.adapter = slideAdapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.slideRecycle)

        binding.slideRecycle.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        //  binding.numberImage.setFactory { ImageView(this.context) }
        var position =
            (binding.slideRecycle.layoutManager as LinearLayoutManager)?.findFirstVisibleItemPosition()
                ?: 0
        binding.numberImage.setFactory {
            val imgView = ImageView(requireContext())
            imgView.scaleType = ImageView.ScaleType.FIT_CENTER
            imgView.setPadding(8, 8, 8, 8)
            imgView
        }
        binding.numberImage.setImageResource(slideItems()[0].imagebaner)

        binding.viewBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("movie_image", slideItems()[position].image)
            bundle.putString("movie_name", slideItems()[position].text)
            var fragment = MovieCointanerFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().addToBackStack("")
                .replace(R.id.open_cinema, fragment).commit()
        }
        binding.nextBtn.setOnClickListener {
            position++
            // Check if the position is within the range of the list size
            if (position < slideItems().size) {
                // Scroll the RecyclerView to the next item
                binding.slideRecycle.smoothScrollToPosition(position)
                binding.numberImage.setImageResource(slideItems()[position].imagebaner)

            } else {
                position = slideItems().size - 1
                // Reset the position to zero
//                position = 0
//                // Scroll the RecyclerView to the first item
//                binding.slideRecycle.smoothScrollToPosition(position)
            }
        }

        binding.backBtn.setOnClickListener {
            position--
            // Check if the position is within the range of the list size
            if (position > -1) {
                // Scroll the RecyclerView to the next item
                binding.slideRecycle.smoothScrollToPosition(position)
                binding.numberImage.setImageResource(slideItems()[position].imagebaner)

            } else {
                // Reset the position to zero
                position = 0
//                // Scroll the RecyclerView to the first item
//                binding.slideRecycle.smoothScrollToPosition(position)
            }

        }


        //For you customize
        val forYouItems = forYouItems()
        val forYouAdapter = MovieRecycleView(forYouItems, manager, movieCointanerFragment)
        binding.forYouRecyclerView.adapter = forYouAdapter
        binding.forYouRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)


        //cinema customize
        val cinemaSugestList = cinemaSugestList()
        val cinmaAdapter = CinemaRecycleView(cinemaSugestList, manager, cinemaCointanerFragment)
        binding.cinameRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.cinameRecyclerView.adapter = cinmaAdapter

        //showing now
        val showingNow = showingNowItems()
        val showingNowAdapter = MovieRecycleView(showingNow, manager, movieCointanerFragment)
        binding.showingNowRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.showingNowRecyclerView.adapter = showingNowAdapter


        var animIn = AnimationUtils.loadAnimation(this.context, R.anim.slide_in_right)
        var animout = AnimationUtils.loadAnimation(this.context, R.anim.slide_out_left)
        var animInBack = AnimationUtils.loadAnimation(this.context, android.R.anim.slide_in_left)
        var animoutBack = AnimationUtils.loadAnimation(this.context, android.R.anim.slide_out_right)
        var index = 0

        binding.refreshLayout.setOnRefreshListener {
            // Refresh logic goes here
            // Call your API or update your data here
            binding.refreshLayout.isRefreshing = false // when done, set refreshing to false
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun slideItems(): ArrayList<SlideData> {
        val call = homeApi.categories
        var arr = arrayListOf(
            SlideData(R.drawable.loading.toString(), "movie 1", R.drawable.first_item_recycle),
            SlideData(R.drawable.loading.toString(), "movie 2", R.drawable.second_item_recycle),
            SlideData(R.drawable.loading.toString(), "movie 3", R.drawable.third_item_recycle),
            SlideData(R.drawable.loading.toString(), "movie 4", R.drawable.fourth_item_recycle)
        )

        call.enqueue(object : Callback<HomeApi> {
            override fun onResponse(call: Call<HomeApi>, response: Response<HomeApi>) {

                arr[0] = SlideData(
                    response.body()?.data?.homeSlides!![0].movie?.image!!,
                    response.body()?.data?.homeSlides!![0].movie?.name!!,
                    R.drawable.first_item_recycle
                )

                arr[1] = SlideData(
                    response.body()?.data?.homeSlides!![1].movie?.image!!,
                    response.body()?.data?.homeSlides!![1].movie?.name!!,
                    R.drawable.second_item_recycle
                )
                arr[2] = SlideData(
                    response.body()?.data?.homeSlides!![2].movie?.image!!,
                    response.body()?.data?.homeSlides!![2].movie?.name!!,
                    R.drawable.third_item_recycle
                )
                arr[3] = SlideData(
                    response.body()?.data?.homeSlides!![3].movie?.image!!,
                    response.body()?.data?.homeSlides!![3].movie?.name!!,
                    R.drawable.fourth_item_recycle
                )
                arr[0] = SlideData(
                    response.body()?.data?.homeSlides!![0].movie?.image!!,
                    response.body()?.data?.homeSlides!![0].movie?.name!!,
                    R.drawable.first_item_recycle
                )
            }

            override fun onFailure(call: Call<HomeApi>, t: Throwable) {

            }

        })
        return arr
    }

    private fun forYouItems(): List<MovieRecycleRecommenation> {
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
                R.drawable.poster_recycle.toString(), "Item 3", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 3", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 3", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 3", "alex", R.drawable.cinemaone.toString()
            ),
            MovieRecycleRecommenation(
                R.drawable.poster_recycle.toString(), "Item 3", "alex", R.drawable.cinemaone.toString()
            ),
        )
        val call = homeApi.categories
        call.enqueue(object : Callback<HomeApi> {
            override fun onResponse(call: Call<HomeApi>, response: Response<HomeApi>) {
                forYouItems[0]=MovieRecycleRecommenation(response.body()?.data?.categories!![0].movies[0].image!!, response.body()?.data?.categories!![0].movies[0].name!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!, response.body()?.data?.categories!![0].movies[0].cinema?.logo!!)
                forYouItems[1]=MovieRecycleRecommenation(response.body()?.data?.categories!![0].movies[1].image!!, response.body()?.data?.categories!![0].movies[1].name!!,  response.body()?.data?.categories!![0].movies[1].cinema?.name!!, response.body()?.data?.categories!![0].movies[1].cinema?.logo!!)
                forYouItems[2]=MovieRecycleRecommenation(response.body()?.data?.categories!![0].movies[2].image!!, response.body()?.data?.categories!![0].movies[2].name!!,  response.body()?.data?.categories!![0].movies[2].cinema?.name!!, response.body()?.data?.categories!![0].movies[2].cinema?.logo!!)
                forYouItems[3]=MovieRecycleRecommenation(response.body()?.data?.categories!![0].movies[3].image!!, response.body()?.data?.categories!![0].movies[3].name!!, response.body()?.data?.categories!![0].movies[3].cinema?.name!!, response.body()?.data?.categories!![0].movies[3].cinema?.logo!!)
                forYouItems[4]=MovieRecycleRecommenation(response.body()?.data?.categories!![0].movies[4].image!!, response.body()?.data?.categories!![0].movies[4].name!!,  response.body()?.data?.categories!![0].movies[4].cinema?.name!!, response.body()?.data?.categories!![0].movies[4].cinema?.logo!!)
                forYouItems[5]=MovieRecycleRecommenation(response.body()?.data?.categories!![0].movies[5].image!!, response.body()?.data?.categories!![0].movies[5].name!!,  response.body()?.data?.categories!![0].movies[5].cinema?.name!!, response.body()?.data?.categories!![0].movies[5].cinema?.logo!!)
                forYouItems[6]=MovieRecycleRecommenation(response.body()?.data?.categories!![0].movies[6].image!!, response.body()?.data?.categories!![0].movies[6].name!!,  response.body()?.data?.categories!![0].movies[6].cinema?.name!!, response.body()?.data?.categories!![0].movies[6].cinema?.logo!!)
                forYouItems[7]=MovieRecycleRecommenation(response.body()?.data?.categories!![0].movies[7].image!!, response.body()?.data?.categories!![0].movies[7].name!!,  response.body()?.data?.categories!![0].movies[7].cinema?.name!!, response.body()?.data?.categories!![0].movies[7].cinema?.logo!!)
            }

            override fun onFailure(call: Call<HomeApi>, t: Throwable) {

            }

        })

        return forYouItems
    }

    private fun cinemaSugestList(): List<CinemaRecycleRecommenation> {
        val cinemaViewImage =
            arrayListOf(R.drawable.cinema, R.drawable.cinema, R.drawable.cinema, R.drawable.cinema)

        val cinemaSugestList = mutableListOf(
            CinemaRecycleRecommenation(R.drawable.cinemaone.toString(), "Item 1", cinemaViewImage,""),
            CinemaRecycleRecommenation(R.drawable.cinemaone.toString(), "Item 2", cinemaViewImage,""),
            CinemaRecycleRecommenation(R.drawable.cinemaone.toString(), "Item 3", cinemaViewImage,""),
            CinemaRecycleRecommenation(R.drawable.cinemaone.toString(), "Item 4", cinemaViewImage,""),
            CinemaRecycleRecommenation(R.drawable.cinemaone.toString(), "Item 5", cinemaViewImage,""),
            CinemaRecycleRecommenation(R.drawable.cinemaone.toString(), "Item 3", cinemaViewImage,""),
            CinemaRecycleRecommenation(R.drawable.cinemaone.toString(), "Item 4", cinemaViewImage,""),
            CinemaRecycleRecommenation(R.drawable.cinemaone.toString(), "Item 5", cinemaViewImage,""),
        )
        val call = homeApi.categories
        call.enqueue(object : Callback<HomeApi> {
            override fun onResponse(call: Call<HomeApi>, response: Response<HomeApi>) {
                cinemaSugestList[0]=CinemaRecycleRecommenation(response.body()?.data?.categories!![0].movies[0].cinema?.cover!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!,  cinemaViewImage,response.body()?.data?.categories!![0].movies[0].cinema?.logo!!)
                cinemaSugestList[1]=CinemaRecycleRecommenation(response.body()?.data?.categories!![0].movies[1].cinema?.cover!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!,  cinemaViewImage,response.body()?.data?.categories!![0].movies[1].cinema?.logo!!)
                cinemaSugestList[2]=CinemaRecycleRecommenation(response.body()?.data?.categories!![0].movies[2].cinema?.cover!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!,  cinemaViewImage,response.body()?.data?.categories!![0].movies[2].cinema?.logo!!)
                cinemaSugestList[3]=CinemaRecycleRecommenation(response.body()?.data?.categories!![0].movies[3].cinema?.cover!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!,  cinemaViewImage,response.body()?.data?.categories!![0].movies[3].cinema?.logo!!)
                cinemaSugestList[4]=CinemaRecycleRecommenation(response.body()?.data?.categories!![0].movies[4].cinema?.cover!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!,  cinemaViewImage,response.body()?.data?.categories!![0].movies[4].cinema?.logo!!)
                cinemaSugestList[5]=CinemaRecycleRecommenation(response.body()?.data?.categories!![0].movies[5].cinema?.cover!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!,  cinemaViewImage,response.body()?.data?.categories!![0].movies[5].cinema?.logo!!)
                cinemaSugestList[6]=CinemaRecycleRecommenation(response.body()?.data?.categories!![0].movies[6].cinema?.cover!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!,  cinemaViewImage,response.body()?.data?.categories!![0].movies[6].cinema?.logo!!)
                cinemaSugestList[7]=CinemaRecycleRecommenation(response.body()?.data?.categories!![0].movies[7].cinema?.cover!!, response.body()?.data?.categories!![0].movies[0].cinema?.name!!,  cinemaViewImage,response.body()?.data?.categories!![0].movies[7].cinema?.logo!!)
            }

            override fun onFailure(call: Call<HomeApi>, t: Throwable) {

            }

        })
        return cinemaSugestList
    }

    private fun setFragment(fragment: Fragment) {
        var manager = requireActivity().supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragment_container, fragment).addToBackStack("home")
        manager.commit()
    }

    private fun showingNowItems(): List<MovieRecycleRecommenation> {
        val showingNowItem = mutableListOf(
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
            ),
        )
        val call = homeApi.categories
        call.enqueue(object : Callback<HomeApi> {
            override fun onResponse(call: Call<HomeApi>, response: Response<HomeApi>) {
                showingNowItem[0]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[0].image!!, response.body()?.data?.categories!![2].movies[0].name!!, response.body()?.data?.categories!![2].movies[0].cinema?.name!!, response.body()?.data?.categories!![2].movies[0].cinema?.logo!!)
                showingNowItem[1]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[1].image!!, response.body()?.data?.categories!![2].movies[1].name!!,  response.body()?.data?.categories!![2].movies[1].cinema?.name!!, response.body()?.data?.categories!![2].movies[1].cinema?.logo!!)
                showingNowItem[2]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[2].image!!, response.body()?.data?.categories!![2].movies[2].name!!,  response.body()?.data?.categories!![2].movies[2].cinema?.name!!, response.body()?.data?.categories!![2].movies[2].cinema?.logo!!)
                showingNowItem[3]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[3].image!!, response.body()?.data?.categories!![2].movies[3].name!!, response.body()?.data?.categories!![2].movies[3].cinema?.name!!, response.body()?.data?.categories!![2].movies[3].cinema?.logo!!)
                showingNowItem[4]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[4].image!!, response.body()?.data?.categories!![2].movies[4].name!!,  response.body()?.data?.categories!![2].movies[4].cinema?.name!!, response.body()?.data?.categories!![2].movies[4].cinema?.logo!!)
                showingNowItem[5]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[5].image!!, response.body()?.data?.categories!![2].movies[5].name!!,  response.body()?.data?.categories!![2].movies[5].cinema?.name!!, response.body()?.data?.categories!![2].movies[5].cinema?.logo!!)
                showingNowItem[6]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[6].image!!, response.body()?.data?.categories!![2].movies[6].name!!,  response.body()?.data?.categories!![2].movies[6].cinema?.name!!, response.body()?.data?.categories!![2].movies[6].cinema?.logo!!)
                showingNowItem[7]=MovieRecycleRecommenation(response.body()?.data?.categories!![2].movies[7].image!!, response.body()?.data?.categories!![2].movies[7].name!!,  response.body()?.data?.categories!![2].movies[7].cinema?.name!!, response.body()?.data?.categories!![2].movies[7].cinema?.logo!!)
            }

            override fun onFailure(call: Call<HomeApi>, t: Throwable) {

            }

        })
        return showingNowItem
    }


}


data class SlideData(val image: String, val text: String, val imagebaner: Int)
data class MovieRecycleRecommenation(
    val imageRes: String, val text: String, val cinemaName: String, val cinemImageView: String
)

data class CinemaRecycleRecommenation(
    val CinemaImage: String, val cinemaName: String, val cinemaView: ArrayList<Int>,val CinemaLogo: String
)

//data class Cinema(val imageRes: Int, val text: String)
//data class ShowingNow(val imageRes: Int, val text: String)
