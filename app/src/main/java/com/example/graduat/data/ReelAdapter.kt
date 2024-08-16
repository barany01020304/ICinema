package com.example.graduat.data

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.graduat.R
import com.example.graduat.fragment.Reel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import de.hdodenhof.circleimageview.CircleImageView


class ReelAdapter(var context: Context,
                  var reels: List<Reel>,
                  var videoPreparedListener: OnVideoPreparedListener):RecyclerView.Adapter<ReelAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //put the layout

        val vie=LayoutInflater.from(parent.context).inflate(R.layout.reel_recycle_item,parent,false)
        return ViewHolder(vie,context,videoPreparedListener)
    }

    override fun getItemCount(): Int {
        return reels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //put recycel data to view
        var reels =reels[position]
        holder.profileImage.setImageResource(reels.cinemaImageResourceId)
        holder.cinemaNameText.text=reels.cinemaName
        holder.followButton.text= if (reels.isFollowing) holder.itemView.context.getString(R.string.following) else holder.itemView.context.resources.getString(R.string.follow)
        holder.loveImage.setImageResource(if (reels.isLoved) R.drawable.heart_clicked else R.drawable.heart_unchekced)
        holder.setVideoPath(reels.videoUrl)
        holder.itemView.setOnClickListener {
            videoPreparedListener.onVideoClick(position)
        }
        holder.loveImage.setOnClickListener {
                holder.loveImage.setImageResource(R.drawable.heart_clicked)
            }


    }
    class ViewHolder(val binding: View, var context: Context, var videoPreparedListener: OnVideoPreparedListener):RecyclerView.ViewHolder(binding) {
        //put layout item
         val profileImage = itemView.findViewById<CircleImageView>(R.id.cinema_image)
        val test:LinearLayout=itemView.findViewById(R.id.test)
         val cinemaNameText = itemView.findViewById<TextView>(R.id.cinema_name_text)
         val followButton = itemView.findViewById<AppCompatButton>(R.id.follow_button)
         val playerView = itemView.findViewById<com.google.android.exoplayer2.ui.StyledPlayerView>(R.id.playerView)
         val expandImage = itemView.findViewById<ImageView>(R.id.expand_image)
         val loveImage = itemView.findViewById<ImageView>(R.id.love_image)
         val loveCountText = itemView.findViewById<TextView>(R.id.love_count_text)
         val commentImage = itemView.findViewById<ImageView>(R.id.comment_image)
         val commentText = itemView.findViewById<TextView>(R.id.comment_text)
         val sendImage = itemView.findViewById<ImageView>(R.id.send_image)
         val sendText = itemView.findViewById<TextView>(R.id.send_text)
        val pbLoading=itemView.findViewById<ProgressBar>(R.id.pbLoading)
        lateinit var exoPlayer: ExoPlayer
        lateinit var mediaSource: MediaSource
        fun setVideoPath(url: String) {

            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        pbLoading.visibility = View.VISIBLE
                    } else if (playbackState == Player.STATE_READY) {
                        pbLoading.visibility = View.GONE
                    }
                }
            })

            playerView.player = exoPlayer

            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            val dataSourceFactory = DefaultDataSource.Factory(context)

            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                MediaItem.fromUri(Uri.parse(url)))

            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()

            if (absoluteAdapterPosition == 0) {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }
            playerView.setOnClickListener {
                (playerView.player as ExoPlayer).pause()            }

            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))
        }
//        init {
//            loveImage.setOnClickListener {
//                loveImage.setImageResource(R.drawable.heart_clicked)
//            }
//
//
//        }

    }
    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
        fun onVideoClick(position: Int)
    }

}
class ExoPlayerItem(
    var exoPlayer: ExoPlayer,
    var position: Int
)