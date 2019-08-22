package com.example.testdb

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.Sensor.STRING_TYPE_ACCELEROMETER
import android.hardware.SensorManager.SENSOR_ACCELEROMETER
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.renderscript.Sampler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chooseplayer.*
import org.w3c.dom.Text
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    val fTransaction= supportFragmentManager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefs = getSharedPreferences("button", MODE_PRIVATE)

        val sp = getSharedPreferences("table", MODE_PRIVATE)

        val editor = sharedPrefs.edit()

        val isComingFromMenu = sp.getBoolean("isComingFromMenu", false)

        if (isComingFromMenu == false) {
            buttonBack.visibility = View.INVISIBLE
        } else {

            buttonBack.visibility = View.VISIBLE
        }

        buttonBack.setOnClickListener {

            val intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(intent)
        }

        var nameList = ArrayList<String>()
        val database = FirebaseDatabase.getInstance()
        val teamRef = database.getReference("team")
        val playerNamesRef = teamRef.child("playernames")


        teamRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value
                if (value != null) {
                    Handler().postDelayed({
                        for (ds in dataSnapshot.children) {

                            if (ds.key == "playernames") {
                                val valueAsString = ds.value.toString()
                                val splitValues = valueAsString.split(",")
                                    val name = splitValues[0].replace("[", "")
                                    val txtViewIdString = splitValues[1].replace("]", "")
                                        .replace(" ", "")
                                    val txtViewId = txtViewIdString.toInt()
                                    val txtView: TextView = findViewById(txtViewId)
                                    txtView.text = name

                            } else {

                                    val valueAsString = ds.value.toString()
                                    val splitValues = valueAsString.split(",")
                                    val btnIdString = splitValues[0].replace("[", "")
                                        .replace('"', ' ')
                                        .replace(" ", "")
                                    val imgResString = splitValues[1].replace("]", "")
                                        .replace('"', ' ')
                                        .replace(" ", "")
                                    val btnId = btnIdString.toInt()
                                    val imgRes = imgResString.toInt()
                                    val btn: Button = findViewById(btnId)
                                    btn.setBackgroundResource(imgRes)

                                }
                            }
                        }, 100)

                    }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        val money = 100

        //val budget: TextView = findViewById(R.id.budget)


        val btnIdsAndImgRes = sharedPrefs.getStringSet("btnidandimgres", null)

        if (btnIdsAndImgRes != null) {

            if (btnIdsAndImgRes.size <= 15) {

                val savedBtnIdsAndImages = sharedPrefs.getStringSet("btnidandimgres", null)
                for (i in savedBtnIdsAndImages) {

                    var splitString = i.split(i[11])
                    val splitStringBtnId = splitString[0].replace("[", "")
                    val splitStringImgRes = splitString[1].replace("]", "")
                    val splitStringImgResYes = splitStringImgRes.replace(" ", "")
                    val btn: Button = findViewById(splitStringBtnId.toInt())
                    btn.setBackgroundResource(splitStringImgResYes.toInt())
                }
            }
        }

        val moneyLeft = sharedPrefs.getInt("budget", 0)

        /*if(moneyLeft != 0){

            budget.text = moneyLeft.toString()
            editor.putInt("budget", moneyLeft)

        } else {
            budget.text = money.toString()
            editor.putInt("budget", money)
            editor.commit()
        }*/

        val bundle = intent.extras
        val savedNamesAndTxtViewIds = sharedPrefs.getStringSet("namesAndTxtViewIds", null)

        if (savedNamesAndTxtViewIds != null){
            for (i in savedNamesAndTxtViewIds) {
                val splitValues = i.split(",")
                if (splitValues.size ==2) {
                    val txtViewId = splitValues[1].replace("[", "")
                        .replace("]]", "")
                        .replace(" ", "")
                        .replace("]", "")
                        val txtView: TextView = findViewById(txtViewId.toInt())
                        val name = splitValues[0].replace("[", "")
                        txtView.text = name
                    }
                }
            }

        if (bundle != null) {
            Log.d("wow", bundle.get("playerposition").toString())
            val btnId = sharedPrefs.getInt("btnID", 0)
            val btnsClicked = sharedPrefs.getStringSet("clickedbuttons", null)
            val clickedBtnsIdList = ArrayList<Int>()
            val btnIdAndImgRes = bundle.get("btnidandimgres")
            val savedBtnIdsAndImages= sharedPrefs.getStringSet("btnidandimgres", null)
            var btnAndImgList = ArrayList<Int>()
            if (savedBtnIdsAndImages != null){

                    for (i in savedBtnIdsAndImages) {

                        var splitString = i.split(i[11])
                        val splitStringBtnId = splitString[0].replace("[", "")
                        val splitStringImgRes = splitString[1].replace("]", "")
                        val splitStringImgResYes = splitStringImgRes.replace(" ", "")
                        val btn: Button = findViewById(splitStringBtnId.toInt())
                        btn.setBackgroundResource(splitStringImgResYes.toInt())
                    }
            } else {
                val btnIdAndImgResList= mutableSetOf(btnIdAndImgRes.toString())
                for (i in btnIdAndImgResList){
                    var splitString = i.split(i[11])
                    val splitStringBtnId = splitString[0].replace("[", "")
                    val splitStringImgRes = splitString[1].replace("]", "")
                    val splitStringImgResYes = splitStringImgRes.replace(" ", "")
                    val btn: Button = findViewById(splitStringBtnId.toInt())
                    btn.setBackgroundResource(splitStringImgResYes.toInt())
                }
                editor.putStringSet("btnidandimgres", btnIdAndImgResList)
            }
            editor.commit()

            for (i in btnsClicked){
                clickedBtnsIdList.add(i.toInt())
            }
            if (bundle.get("name") == "Andreas Lie") {
                var imgSrc: Int = bundle.get("team") as Int
                val btn: Button = findViewById(btnId)
                btn.setBackgroundResource(imgSrc)
            }
        }

        buttonConfirm.setOnClickListener {

            val savedPlayerList = ArrayList<String>()
            val savedBtnIdsAndImgRes = sharedPrefs.getStringSet("btnidandimgres", null)
            val btnIdsAndImgResList = ArrayList<String>()
            val sp = getSharedPreferences("table", MODE_PRIVATE)
            val spEditor = sp.edit()

            for (i in savedBtnIdsAndImgRes) {

                btnIdsAndImgResList.add(i)
            }
            for (i in savedNamesAndTxtViewIds) {

                savedPlayerList.add(i)
            }

                teamRef.setValue(btnIdsAndImgResList)
                playerNamesRef.setValue(savedPlayerList)
                editor.remove("namesAndTxtViewIds")
                editor.remove("btnidandimgres")
                spEditor.remove("isComingFromMenu")
                spEditor.commit()
                editor.commit()
                finish()
                val intent = Intent(applicationContext, MenuActivity::class.java)
                startActivity(intent)

                2.until(17).forEach { idx ->
                    val button = findViewById(resources.getIdentifier("button$idx", "id", packageName))as Button
                    button.visibility = View.INVISIBLE
                }
                buttonConfirm.visibility = View.INVISIBLE
        }

    }

        fun btnClick(view: View) {

            val sharedPrefs = getSharedPreferences("button", MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            val selectedBtn = view as Button
            val choosePlayerFragment = ChoosePlayerFragment()
            val clickedBtns = sharedPrefs.getStringSet("clickedbuttons", null)
            val clickedButtonsSet: MutableSet<String> = mutableSetOf(selectedBtn.id.toString())

            if (selectedBtn.id == button15.id) {

                editor.putInt("textViewId", textView3.id)
            }
            if (selectedBtn.id == button14.id) {

                editor.putInt("textViewId", textView4.id)
            }
            if (selectedBtn.id == button11.id) {

                editor.putInt("textViewId", textView5.id)
            }
            if (selectedBtn.id == button10.id) {

                editor.putInt("textViewId", textView6.id)
            }
            if (selectedBtn.id == button16.id) {

                editor.putInt("textViewId", textView7.id)
            }
            if (selectedBtn.id == button9.id) {

                editor.putInt("textViewId", textView8.id)
            }
            if (selectedBtn.id == button3.id) {

                editor.putInt("textViewId", textView9.id)
            }
            if (selectedBtn.id == button8.id) {

                editor.putInt("textViewId", textView10.id)
            }
            if (selectedBtn.id == button7.id) {

                editor.putInt("textViewId", textView11.id)
            }
            if (selectedBtn.id == button6.id) {

                editor.putInt("textViewId", textView12.id)
            }
            if (selectedBtn.id == button2.id) {

                editor.putInt("textViewId", textView13.id)
            }
            if (selectedBtn.id == button13.id) {

                editor.putInt("textViewId", textView14.id)
            }
            if (selectedBtn.id == button5.id) {

                editor.putInt("textViewId", textView15.id)
            }
            if (selectedBtn.id == button4.id) {

                editor.putInt("textViewId", textView16.id)
            }
            if (selectedBtn.id == button12.id) {

                editor.putInt("textViewId", textView17.id)
            }


            editor.putInt("btnID", selectedBtn.id)
            if (clickedBtns != null) {
                clickedBtns.add(selectedBtn.id.toString())
                editor.putStringSet("clickedbuttons", clickedBtns)
            } else {
                editor.putStringSet("clickedbuttons", clickedButtonsSet)
            }
            editor.commit()
            choosePlayerFragment.onAttach(application)
            fTransaction.replace(R.id.main_activity, choosePlayerFragment)
            fTransaction.commit()
            2.until(17).forEach { idx ->
                    val button = findViewById(resources.getIdentifier("button$idx", "id", packageName))as Button
                    button.isEnabled = false
                }
            buttonConfirm.isEnabled = false

    }


}
