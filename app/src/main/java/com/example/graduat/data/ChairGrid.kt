package com.example.graduat.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.graduat.R




class ChairGrid (
    // on below line we are creating two
    // variables for course list and context
    private val context: Context
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null
    private lateinit var chairImage: ImageView

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return 88
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.chairgrid, null)
        }
        chairImage=convertView!!.findViewById(R.id.one_chair_id)
        chairImage.setImageResource(R.drawable.white_chair)
        var saveList: Array<Boolean> = Array(count) { false }
        save(position,saveList,convertView!!)
        // at last we are returning our convert view.
        return convertView
    }
    fun save(position: Int, saveList: Array<Boolean>, convertView: View) {
       // chairImage.setOnClickListener {

        if (position==0||position==10||position==5 ||position==16 ||position==27 ||position==38 ||position==49 ||position==60 ||position==71||position==82 ) {

            chairImage.visibility = View.INVISIBLE
            convertView.visibility=View.INVISIBLE
            saveList[position] = true
        }
       if (!(position==-1)){
           chairImage.visibility = View.INVISIBLE

       }



    //    } else {
      //      chairImage.setImageResource(R.drawable.white_chair)
     //       saveList[position] = false
      //  }
//            if (position==6) {
//
//                chairImage.setImageResource(R.drawable.yellow_chair)
//                saveList[position] = true
//
//
//
//            } else {
//                chairImage.setImageResource(R.drawable.white_chair)
//                saveList[position] = false
//            }

    }
}