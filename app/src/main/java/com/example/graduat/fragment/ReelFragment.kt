package com.example.graduat.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import androidx.viewpager2.widget.ViewPager2
import com.example.graduat.R
import com.example.graduat.data.ExoPlayerItem
import com.example.graduat.data.ReelAdapter
import com.example.graduat.databinding.FragmentReelBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ReelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReelFragment : Fragment() {


    lateinit var binding: FragmentReelBinding
    lateinit var adapter:ReelAdapter
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    private var pause = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
binding = DataBindingUtil.inflate(inflater, com.example.graduat.R.layout.fragment_reel,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reelList = listOf(
            Reel("Cinema One", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",11_000, 5_000, 1_000, false,false),
            Reel("Cinema Two", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", 9_000, 3_000, 500, false,false),
            Reel("Cinema Three", R.drawable.cinemaone,"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                , 15_000, 7_000, 2_500, false,true),
            Reel("Cinema One", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",11_000, 5_000, 1_000, false,false),
            Reel("Cinema Two", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", 9_000, 3_000, 500, false,false),
            Reel("Cinema Three", R.drawable.cinemaone,"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                , 15_000, 7_000, 2_500, false,true),
            Reel("Cinema One", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",11_000, 5_000, 1_000, false,false),
            Reel("Cinema Two", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", 9_000, 3_000, 500, false,false),
            Reel("Cinema Three", R.drawable.cinemaone,"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                , 15_000, 7_000, 2_500, false,true),
            Reel("Cinema One", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",11_000, 5_000, 1_000, false,false),
            Reel("Cinema Two", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", 9_000, 3_000, 500, false,false),
            Reel("Cinema Three", R.drawable.cinemaone,"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                , 15_000, 7_000, 2_500, false,true),
            Reel("Cinema One", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",11_000, 5_000, 1_000, false,false),
            Reel("Cinema Two", R.drawable.cinemaone, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", 9_000, 3_000, 500, false,false),
            Reel("Cinema Three", R.drawable.cinemaone,"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                , 15_000, 7_000, 2_500, false,true),
            // add more reels as needed
        )

        adapter = ReelAdapter(requireContext(),reelList , object : ReelAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }


            override fun onVideoClick(position: Int) {

                val index =
                    exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
                if (pause==false) {
                    val player = exoPlayerItems[index].exoPlayer
                    player.pause()
                    pause=true
                    binding.ivSpeaker.setImageResource(android.R.drawable.ic_media_pause)
                }else if (pause==true){
                    val player = exoPlayerItems[index].exoPlayer
                    player.play()
                    pause=false
                    binding.ivSpeaker.setImageResource(android.R.drawable.ic_media_play)
                }
                binding.ivSpeaker.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.ivSpeaker.visibility = View.GONE
                }, 1000)
            }



        })

        binding.viewPager2.adapter = adapter

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })

    }

    override fun onPause() {
        super.onPause()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
}


data class Reel(
    val cinemaName: String,
    val cinemaImageResourceId: Int,
    val videoUrl: String,
    var loveCount: Int,
    val commentCount: Int,
    val sendCount: Int,
    var isFollowing: Boolean,
    var isLoved:Boolean,
)

