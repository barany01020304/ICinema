package com.example.graduat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.graduat.databinding.ActivityBookBinding
import com.example.graduat.fragment.ChooseCinemaFragment
import com.example.graduat.fragment.DateFragment

class BookActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_book)
        if (intent.getBooleanExtra("getItFroMCinema",false)){
            supportFragmentManager.beginTransaction().replace(R.id.book_activity_container, ChooseCinemaFragment()).commitNow()

        }
        else{
            supportFragmentManager.beginTransaction().replace(R.id.book_activity_container, DateFragment()).commitNow()

        }

    }
}