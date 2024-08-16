package com.example.graduat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.databinding.ActivityMainBinding
import com.example.graduat.fragment.*
import meow.bottomnavigation.MeowBottomNavigation

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.option_buttom,"menu"))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.reel_buutom,"reel"))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.home_buttom,"home"))
        bottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.ticket_buttom,"ticket"))
        bottomNavigation.add(MeowBottomNavigation.Model(5, R.drawable.notification_buttom,"notification"))
        setFragment(HomeFragment())
        bottomNavigation.show(3, false)
        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> {
                    setFragment(MenuFragment())

                }
                2 -> {
                    setFragment(ReelFragment())
                }
                3 -> {
                    setFragment(HomeFragment())
                }
                4 -> {
                    setFragment(TicketFragment())
                }
                5 -> {
                    setFragment(NotiFragment())
                }
            }
        }

    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//    }

    private fun setFragment(fragment: Fragment) {
        var manager = supportFragmentManager.beginTransaction()
        manager.replace(binding.fragmentContainer.id, fragment)
        manager.commit()
    }
//    companion object{
//
//             fun removeMeow(){
//
//                 var binding: ActivityMainBinding =DataBindingUtil.setContentView(, R.layout.activity_main)
//
//                binding.activityHome.removeView(binding.bottomNavigation)
//
//            }
//
//
//
//    }
}
