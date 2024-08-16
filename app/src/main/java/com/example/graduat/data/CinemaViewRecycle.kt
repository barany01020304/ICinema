package com.example.graduat.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduat.R

class CinemaViewRecycle(var list: ArrayList<Int>) : RecyclerView.Adapter<CinemaViewRecycle.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cinemaViewImage: ImageView =itemView.findViewById(R.id.ciname_view_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cinema_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item=list[position]
        holder.cinemaViewImage.setImageResource(item)
    }
}