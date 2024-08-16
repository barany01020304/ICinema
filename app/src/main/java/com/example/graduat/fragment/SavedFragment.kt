package com.example.graduat.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.R
import com.example.graduat.database.MovieData
import com.example.graduat.databinding.FragmentSavedBinding
import com.squareup.picasso.Picasso
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults


class SavedFragment : Fragment() {

    lateinit var binding: FragmentSavedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val config = RealmConfiguration.create(schema = setOf(MovieData::class))
        val realm: Realm = Realm.open(config)

        var items: RealmResults<MovieData> = realm.query<MovieData>().find()

        binding.movieSavedGrid.adapter = GridRVAdapter(courseList = items, this.requireContext(), realm)

        // on below line we are adding on item
        // click listener for our grid view.
        binding.movieSavedGrid.setOnItemLongClickListener { adapterView, view, i, l ->

            val builder = AlertDialog.Builder(context)

            // Set the title and message for the dialog.
            builder.setTitle("Delete")
            builder.setMessage("Do you want really delete this object")

            // Create a positive button and set its click listener.
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                binding.progressHorizontal.visibility=View.VISIBLE
                binding.progressHorizontal.postDelayed({
                    binding.progressHorizontal.visibility=View.GONE

                },500)

                Toast.makeText(this.context,"int value is $i and long value is $l",Toast.LENGTH_LONG).show()
                realm.writeBlocking {
                    val writeTransactionItems = query<MovieData>().find()
                    delete(writeTransactionItems[i])
                }


                items = realm.query<MovieData>().find()
                binding.movieSavedGrid.adapter = GridRVAdapter(courseList = items, this.requireContext(), realm)
                Toast.makeText(this.context,items.size.toString(),Toast.LENGTH_SHORT).show()

            })

            // Create a negative button and set its click listener.
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                // Do something when the negative button is clicked.
            })

            // Create and show the dialog.
            val dialog = builder.create()
            dialog.show()
//
            true
        }
        binding.movieSavedGrid.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                // inside on click method we are simply displaying
                // a toast message with course name.
//            Toast.makeText(
//                applicationContext, courseList[position].courseName + " selected",
//                Toast.LENGTH_SHORT
//            ).show()
            }


    }

}

internal class GridRVAdapter(
    // on below line we are creating two
    // variables for course list and context
    private val courseList: RealmResults<MovieData>,
    private val context: Context,
    private val realm: Realm
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null

    //private lateinit var saveDrawble: ImageView
    private lateinit var movieName: TextView
    private lateinit var movieImage: ImageView

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return courseList.size
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
            convertView = layoutInflater!!.inflate(R.layout.custom_grid_view, null)
        }
        // on below line we are initializing our course image view
        // and course text view with their ids.
        movieImage = convertView!!.findViewById(R.id.movie_image_grid)
        movieName = convertView!!.findViewById(R.id.movie_name_grid)
        // on below line we are setting image for our course image view.
        Picasso.get().load(courseList.get(position).moviePicture).into(movieImage)


        // on below line we are setting text in our course text view.
        movieName.setText(courseList.get(position).movieName)
//        saveDrawble.setImageResource(R.drawable.saved_drawble)


        // at last we are returning our convert view.
        return convertView
    }
}
