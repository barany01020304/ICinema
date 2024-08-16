package com.example.graduat.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduat.R
import com.example.graduat.database.MovieData
import com.example.graduat.fragment.MovieRecycleRecommenation
import com.squareup.picasso.Picasso
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration


class MovieRecycleView(
    private val items: List<MovieRecycleRecommenation>,
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment
) : RecyclerView.Adapter<MovieRecycleView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textView: TextView = itemView.findViewById(R.id.text_view)
        val saveButton: ImageView = itemView.findViewById(R.id.sava_button)
        val cinemaName: TextView = itemView.findViewById(R.id.cinema_has_this_movie)
        val cinemaImage: ImageView = itemView.findViewById(R.id.cinema_image)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_recycler_item, parent, false)
        return ViewHolder(view)
    }
//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Picasso.get().load(item.imageRes).error(R.drawable.poster_recycle).into(holder.imageView)
        holder.textView.text = item.text
        holder.cinemaName.text = item.cinemaName
        Picasso.get().load(item.cinemImageView).fit().error(R.drawable.cinema).into(holder.cinemaImage)
        var saveList: Array<Boolean> = Array(items.size) { false }

        save(position, saveList, holder.saveButton)
        holder.imageView.setOnClickListener {
            val beginTransaction = fragmentManager.beginTransaction()

            beginTransaction.setCustomAnimations(
                R.anim.slide_in_down,
                R.anim.slide_out_top,
                R.anim.slide_in_top,
                R.anim.slide_out_down
            )
            val bundle = Bundle()
            bundle.putString("movie_image", item.imageRes)
            bundle.putString("movie_name", item.text)
            fragment.arguments = bundle
            beginTransaction.replace(R.id.open_cinema, fragment)
                .addToBackStack("book")
                .commit()


//            var manager=supportFragmentManager.beginTransaction()
//            manager.replace(binding.fragmentContainer.id,fragment)
//            manager.commit()

        }


    }

    fun save(position: Int, saveList: Array<Boolean>, saveButton: ImageView) {
        saveButton.setOnClickListener {
            if (saveList.get(position) == false) {
                saveButton.setImageResource(R.drawable.saved_drawble)
                saveList[position] = true
                val config = RealmConfiguration.create(schema = setOf(MovieData::class))
                val realm: Realm = Realm.open(config)
                realm.writeBlocking {
                    copyToRealm(MovieData().apply {
                        movieName = items[position].text?:"movieeror"
                        moviePicture = items[position].imageRes
                        cinemaPicture= items[position].cinemImageView
                        cinemaName = items[position].cinemaName
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

            } else {
                saveButton.setImageResource(R.drawable.save_drawble)
                saveList[position] = false
            }
        }
    }


    override fun getItemCount() = items.size
}