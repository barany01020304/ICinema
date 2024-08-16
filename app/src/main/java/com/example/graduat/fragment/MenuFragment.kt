package com.example.graduat.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.MainActivity
import com.example.graduat.R
import com.example.graduat.database.PersonalData
import com.example.graduat.databinding.FragmentMenuBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import java.util.*


class MenuFragment : Fragment() {
    lateinit var binding: FragmentMenuBinding
    lateinit var config: RealmConfiguration
    lateinit var realm: Realm
    lateinit var personlData: RealmResults<PersonalData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        config = RealmConfiguration.create(schema = setOf(PersonalData::class))
        realm = Realm.open(config)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         personlData =realm.query<PersonalData>().find()
        binding.yourNumber.text= personlData[0].phone
        binding.yourName.text=personlData[0].fullName


      //  if (personlData[0].accountPic==R.drawable.account_pic.toString()){
        //    binding.profilePhoto.setImageResource(R.drawable.account_pic)
      //  }else{
            binding.profilePhoto.setImageURI(personlData[0].accountPic.toUri())
      //  }


        binding.myProfileCointainer.setOnClickListener {
            setFragment(MyProfileFragment())
        }

        binding.modSwitchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            // on below line we are checking
            // if switch is checked or not.
            if (isChecked) {

                ThemeHelper.applyTheme(ThemeHelper.LIGHT_MODE)

            } else {
                ThemeHelper.applyTheme(ThemeHelper.DARK_MODE)

            }

        }
        binding.savedMove.setOnClickListener {
                setFragment(SavedFragment())
        }
        binding.langSwitchButton.setOnClickListener {
            setLocale("ar", this.requireActivity() as MainActivity)
        }


    }
    private fun setFragment(fragment: Fragment) {
        var manager = requireActivity().supportFragmentManager.beginTransaction().addToBackStack("back")
        manager.replace(R.id.fragment_container, fragment)
        manager.commit()
    }
    object ThemeHelper {
        const val LIGHT_MODE = "light"
        const val DARK_MODE = "dark"

        fun applyTheme(theme: String) {
            when (theme) {
                LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        fun getTheme(): String {
            return when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_NO -> LIGHT_MODE
                else -> DARK_MODE
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            realm.writeBlocking {
                findLatest(personlData[0])?.accountPic = uri.toString()
            }
            binding.profilePhoto.setImageURI(personlData[0].accountPic.toUri())

            // Use Uri object instead of File to avoid storage permissions
            //   imgProfile.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this.requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    // change lang
    fun setLocale(lang: String, activity: MainActivity) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity.baseContext.resources.updateConfiguration(
            config,
            activity.baseContext.resources.displayMetrics
        )
        activity.recreate()

    }
}