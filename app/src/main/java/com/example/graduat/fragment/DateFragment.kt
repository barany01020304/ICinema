package com.example.graduat.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.R
import com.example.graduat.databinding.FragmentDateBinding
import java.text.SimpleDateFormat
import java.util.*


class DateFragment : Fragment() {
    lateinit var binding: FragmentDateBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_date, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val items = arrayOf("Apple", "Banana", "Cherry")
        val timeAdapter =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, timeList())
        val popupTime = ListPopupWindow(this.requireContext())
        popupTime.setAdapter(timeAdapter)

        popupTime.anchorView = binding.timeButton //the button that triggers the popup
        binding.timeButton.setOnClickListener {
            popupTime.show()
        }

        //"yyyy-MM-dd"
         val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val dateTime: Date = Calendar.getInstance().time
        val dateTimeAsLong: Long = dateTime.time
        binding.calendarView.minDate = dateTimeAsLong
        binding.calendarView.maxDate =dateTimeAsLong
        binding.calendarView.date =dateTimeAsLong
        val dateAdapter =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, dayList())
        val popupDate = ListPopupWindow(this.requireContext())
        popupDate.setAdapter(dateAdapter)

        popupDate.anchorView = binding.dateButton //the button that triggers the popup
        binding.dateButton.setOnClickListener {
            popupDate.show()
        }
      //  binding.dateButton.setOnClickListener {
//            binding.calendarView.minDate = 1463918226920L
//            binding.calendarView.maxDate =1463918226920L
//            binding.calendarView.date =1463918226920L
      //  }

         val backToDate: Date = Date(dateTimeAsLong)



        binding.nextBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()

                .setCustomAnimations(

                    androidx.appcompat.R.anim.abc_fade_in,
                    androidx.appcompat.R.anim.abc_fade_out
                    //
                    ////                R.anim.slide_in_right,
                    ////                R.anim.slide_out_left,
                    ////                R.anim.slide_in_right,
                    ////                R.anim.slide_out_left
                )
                .replace(R.id.book_activity_container, ChairFragment())
                .addToBackStack("dateFragment").commit()
        }
        popupDate.setOnItemClickListener { parent, view, position, id ->
            //handle the item click here
            binding.dateButton.text = dayList()[position]
            popupDate.dismiss()
            convertDateToLong(dayList()[position])
            binding.calendarView.minDate = convertDateToLong(dayList()[position])
            binding.calendarView.maxDate =convertDateToLong(dayList()[position])
            binding.calendarView.date =convertDateToLong(dayList()[position])

        }
        popupTime.setOnItemClickListener { parent, view, position, id ->
            //handle the item click here
            binding.timeButton.text = timeList()[position]
            popupTime.dismiss()

        }

//        binding.backButton.setOnClickListener {
//            var manager = requireActivity().supportFragmentManager.beginTransaction()
//            manager.addToBackStack("date").replace(R.id.open_cinema, MovieCointanerFragment())
//            manager.commitNow()
//        }
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
    fun convertDateToLong(date: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.parse(date).time
    }
    fun dayList():ArrayList<String>{
        return arrayListOf("2023-6-29","2023-7-3","2023-7-5")

    }
    fun timeList():ArrayList<String>{
        return arrayListOf("12:00am","3:00pm","8:00pm")

    }


}