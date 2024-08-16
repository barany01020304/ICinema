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
import com.example.graduat.fragment.SlideData
import com.squareup.picasso.Picasso

class SlideRecycleView(
    private val items: List<SlideData>,
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment
) : RecyclerView.Adapter<SlideRecycleView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(R.id.slide_image)
        val textUnder: TextView = itemView.findViewById(R.id.slide_text)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.slide_recycle_view_list, parent, false)
        return ViewHolder(view)
    }
//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Picasso.get().load(item.image).error(R.drawable.poster_recycletwo).into(holder.movieImage)

      //  Glide.with(holder.itemView.context).load(item.image).into(holder.movieImage)
       // holder.movieImage.setImageResource(item.image.toInt())
        holder.textUnder.text = item.text



        holder.movieImage.setOnClickListener {
            val beginTransaction = fragmentManager.beginTransaction()

            beginTransaction.setCustomAnimations(
                R.anim.slide_in_down,
                R.anim.slide_out_top,
                R.anim.slide_in_top,
                R.anim.slide_out_down
            )
            val bundle = Bundle()
            bundle.putString("movie_image", item.image.toString())
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




    override fun getItemCount() = items.size
}