package com.example.graduat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.R
import com.example.graduat.databinding.FragmentPayBinding

class PayFragment : Fragment() {

lateinit var binding: FragmentPayBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        show(binding.creditSwitch,binding.creditCardView,binding.eWalletSwitch,binding.fawrySwitch,binding.iCinemaSwitch,binding)
        show(binding.eWalletSwitch,binding.eWalletView,binding.creditSwitch,binding.fawrySwitch,binding.eWalletSwitch,binding)
        show(binding.fawrySwitch,binding.fawryView,binding.creditSwitch,binding.eWalletSwitch,binding.iCinemaSwitch,binding)
        show(binding.iCinemaSwitch,binding.icenimaWalletView,binding.creditSwitch,binding.eWalletSwitch,binding.fawrySwitch,binding)
      //  show(binding.iCinemaSwitch,binding.currentBallenceText)
        binding.confirmButton.setOnClickListener {
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
                .replace(R.id.book_activity_container,BookSuccesFragment()).commit()        }

    }
}
fun show(switch:SwitchCompat,view: View,switch2:SwitchCompat,switch3:SwitchCompat,switch4:SwitchCompat,binding: FragmentPayBinding){
    switch.setOnClickListener {
        if (switch.isChecked){
            view.visibility=View.VISIBLE
//            switch2.isChecked=false
//            switch3.isChecked=false
//            switch4.isChecked=false
           // show()
        }
        else{
            view.visibility=View.GONE

        }
    }
}
