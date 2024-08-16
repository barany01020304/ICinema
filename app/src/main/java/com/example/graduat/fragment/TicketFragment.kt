package com.example.graduat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduat.R
import com.example.graduat.data.TicketAdapter
import com.example.graduat.databinding.FragmentTicketBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults


class TicketFragment : Fragment() {
    lateinit var binding: FragmentTicketBinding
    lateinit var realm:Realm
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_ticket,container,false)
        val config = RealmConfiguration.create(schema = setOf(com.example.graduat.database.TicketData::class))
         realm = Realm.open(config)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ticketRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        var items: RealmResults<com.example.graduat.database.TicketData> = realm.query<com.example.graduat.database.TicketData>().find()

        val adapter = TicketAdapter(items)
        binding.ticketRecyclerView.adapter = adapter

    }

//    private fun addDataToList() {
//        mList.add(
//            TicketData(
//                "Java",
//                R.drawable.third_image,
//                "","","",""
//            )
//        )
//        mList.add(
//            TicketData(
//                "Java",
//                R.drawable.third_image,
//                "","","",""
//            )
//        )
//        mList.add(
//            TicketData(
//                "Java",
//                R.drawable.third_image,
//                "","","",""
//            )
//        )
//        mList.add(
//            TicketData(
//                "Java",
//                R.drawable.third_image,
//                "","","",""
//            )
//        )
//        mList.add(
//            TicketData(
//                "Java",
//                R.drawable.third_image,
//                "","","",""
//            )
//        )
//        mList.add(
//            TicketData(
//                "Java",
//                R.drawable.third_image,
//                "","","",""
//            )
//        )
//        mList.add(
//            TicketData(
//                "Java",
//                R.drawable.third_image,
//                "","","",""
//            )
//        )
//        mList.add(
//            TicketData(
//                "Java",
//                R.drawable.third_image,
//                "","","",""
//            )
//        )
//    }


}
data class TicketData(
    val title: String,
    val logo: Int,
    val data: String,
    val time:String,
    val chairs:String,
    val qrUrl:String,
    var isExpandable: Boolean = false
)