package com.example.graduat.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.graduat.R
import com.squareup.picasso.Picasso
import io.realm.kotlin.query.RealmResults

class TicketAdapter(private var mList: RealmResults<com.example.graduat.database.TicketData>,var isExpandable: Boolean = false) :
    RecyclerView.Adapter<TicketAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.movie_logo)
        val titleTv: TextView = itemView.findViewById(R.id.movie_name)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        val arrowImage: ImageView = itemView.findViewById(R.id.arrow_image)
        val firstCointainer = itemView.findViewById<ConstraintLayout>(R.id.first_cointainer)
        val secondCointainer = itemView.findViewById<LinearLayout>(R.id.second_cointainer)
        val showDetails = itemView.findViewById<TextView>(R.id.show_datils_text_view)
        val qrImage = itemView.findViewById<ImageView>(R.id.qr_image_view)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int,): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {

        val languageData = mList[position]
        Picasso.get().load(languageData.moviePicture).fit().error(R.drawable.second_image).into(holder.logo)


        holder.titleTv.text = languageData.movieName

        val isExpandable: Boolean = isExpandable
        holder.arrowImage.setImageResource(if (isExpandable) R.drawable.up_arrow else R.drawable.down_arrow)
        holder.firstCointainer.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.secondCointainer.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.showDetails.text = if (isExpandable) "Hide Details" else "Show Details"
        holder.qrImage.setImageResource(R.drawable.qrcode)
        holder.constraintLayout.setOnClickListener {
            isAnyItemExpanded(position)
            this.isExpandable = !isExpandable
            notifyItemChanged(position, Unit)
        }

    }

    private fun isAnyItemExpanded(position: Int) {
        val temp = mList.indexOfFirst {
            isExpandable
        }
        if (temp >= 0 && temp != position) {
            isExpandable = false
            notifyItemChanged(temp, 0)
        }
    }

    override fun onBindViewHolder(
        holder: LanguageViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        if (payloads.isNotEmpty() && payloads[0] == 0) {
        } else {

            super.onBindViewHolder(holder, position, payloads)

        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}