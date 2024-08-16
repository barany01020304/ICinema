package com.example.graduat

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.graduat.database.PersonalData
import com.example.graduat.databinding.ActivityPhonePageBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

class PhonePage : AppCompatActivity() {
    lateinit var binding: ActivityPhonePageBinding
    lateinit var config: RealmConfiguration
    lateinit var realm: Realm
    lateinit var personlData: RealmResults<PersonalData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config = RealmConfiguration.create(schema = setOf(PersonalData::class))
        realm = Realm.open(config)
        personlData = realm.query<PersonalData>().find()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_page)
        supportActionBar?.hide()
        setValue()
        binding.backButton.setOnClickListener {

            finish()
        }
        realm.writeBlocking {

        }
        binding.getcodeButton.setOnClickListener {
        }


    }


    private fun moveInfoPage() {

        val intent = Intent(this, Otp::class.java)
        startActivity(intent)
    }


    private fun setValue() {

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countryGenerate())
        binding.countryLayoutEditTextt.setAdapter(adapter)
        binding.countryLayoutEditTextt.doAfterTextChanged {
            try {
                binding.numberTextInput.setText(
                    "+" + numberGenrate(
                        countryGenerate().indexOf(
                            binding.countryLayoutEditTextt.text.toString()
                        )
                    )
                )
                binding.numberTextInput.requestFocus(5)
            } catch (e: Exception) {
            }
        }
        checkEditTextLength(binding.numberTextInput, binding.getcodeButton)
    }

    private fun numberGenrate(country: Int): Int {
        var countryNumberArrayList = ArrayList<String>()
        var countryArrayList = ArrayList<String>()
        var countryNumberArray = resources.getStringArray(R.array.country_data)
        for (it in countryNumberArray) {
            val pattern = "\\D+".toRegex()
            val numberString = pattern.replace(it, "")
            countryNumberArrayList.add(numberString)

            val pattern2 = "[^\\d]+".toRegex()
            val characterString = pattern.find(it)?.value ?: ""
            countryArrayList.add(characterString)
        }
        return countryNumberArrayList[country].toInt()
    }


    private fun countryGenerate(): ArrayList<String> {
        var countryNumberArrayList = ArrayList<String>()
        var countryArrayList = ArrayList<String>()
        var countryNumberArray = resources.getStringArray(R.array.country_data)
        for (it in countryNumberArray) {
            val pattern = "\\D+".toRegex()
            val numberString = pattern.replace(it, "")
            countryNumberArrayList.add(numberString)

            val pattern2 = "[^\\d]+".toRegex()
            val characterString = pattern.find(it)?.value ?: ""
            countryArrayList.add(characterString)
        }
        return countryArrayList
    }


    private fun checkEditTextLength(editText: EditText, button: Button) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(EditText: Editable?) {
                val input = EditText.toString().trim()
                if (input.length > 9 && resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {

                    button.background = getDrawable(R.drawable.register_button_background_dark)
                    button.setTextColor(getColor(R.color.black))
                } else if (input.length > 9 && resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO) {
                    button.setTextColor(getColor(R.color.white))
                    button.background = getDrawable(R.drawable.register_button_background_light)
                } else if (binding.numberTextInput.text!!.equals("") || input.length < 9) {
                    binding.getcodeButton.setOnClickListener {
                        // interCorrectValueToast()
                    }

                } else {
                    binding.getcodeButton.setOnClickListener {
                        realm.writeBlocking {
                            copyToRealm(PersonalData().apply {
                                phone = binding.numberTextInput.text.toString()

                            })
                        }
                        moveInfoPage()
                    }
                }

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


}