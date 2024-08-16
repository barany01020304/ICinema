package com.example.graduat.data

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduat.R
import com.example.graduat.fragment.CinemaRecycleRecommenation
import com.squareup.picasso.Picasso

class CinemaRecycleView(private val items: List<CinemaRecycleRecommenation>, private val fragmentManager: FragmentManager, private val fragment: Fragment) : RecyclerView.Adapter<CinemaRecycleView.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textView: TextView = itemView.findViewById(R.id.text_view)
        val saveButton: ImageView = itemView.findViewById(R.id.sava_button)
        val cinemaLogo: ImageView = itemView.findViewById(R.id.ciname_logo_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaRecycleView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cinema_recycle_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: CinemaRecycleView.ViewHolder, position: Int) {
        val item = items[position]
        Picasso.get().load(item.CinemaImage).error(R.drawable.loading).into(holder.imageView)
        Glide.with(holder.imageView.context).load(item.CinemaLogo).error(R.drawable.loading).into(holder.cinemaLogo)
       // Picasso.get().load(item.CinemaLogo).error(R.drawable.loading).into(holder.cinemaLogo)

        holder.textView.text = item.cinemaName
        var saveList:Array<Boolean> = Array(items.size){false}
        holder.imageView.setOnClickListener {
            val beginTransaction=fragmentManager.beginTransaction()

            beginTransaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_down)
            val bundle = Bundle()
            bundle.putString("cinema_image",item.CinemaImage)
            bundle.putString("cinema_name",item.cinemaName)
            bundle.putIntegerArrayList("cinema_view",item.cinemaView)
            fragment.arguments=bundle
            beginTransaction.replace(R.id.open_cinema, fragment)
                .addToBackStack(null)
                .commit()
//
//            var manager=supportFragmentManager.beginTransaction()
//            manager.replace(binding.fragmentContainer.id,fragment)
//            manager.commit()

        }


        save(position,saveList,holder.saveButton)

    }
    }
    fun save(position: Int,saveList: Array<Boolean>,saveButton: ImageView){
        saveButton.setOnClickListener {
            if (saveList.get(position)==false){
                saveButton.setImageResource(R.drawable.add_cinema)
                saveList[position]=true
            }else {
                saveButton.setImageResource(R.drawable.added_cinema)
                saveList[position]=false
            }
        }
    }


