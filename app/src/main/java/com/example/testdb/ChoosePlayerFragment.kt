package com.example.testdb

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context.MODE_PRIVATE
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.collections.ArrayList
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chooseplayer.*
import java.util.*
import kotlin.random.Random
import android.support.v4.content.ContextCompat.getSystemService as getSystemService


@Suppress("UNREACHABLE_CODE")
public class ChoosePlayerFragment : Fragment(), SensorEventListener {

    var shake = 0.00f
    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = this.activity!!.getSystemService(Activity.SENSOR_SERVICE) as SensorManager

        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fView = inflater.inflate(R.layout.fragment_chooseplayer, container, false)

        val toast = Toast.makeText(context, "Rist telefonen hvis du vil starte laguttaket på nytt", Toast.LENGTH_LONG)
        toast.show()

        val footballersListView: RecyclerView = fView.findViewById(R.id.footballerList)

        val layoutManager = LinearLayoutManager(this.activity)

        val sp = activity!!.getSharedPreferences("button", MODE_PRIVATE)
        val spEditor = sp.edit()
        val latitude = sp.getString("latitude", "")
        val longitude = sp.getString("longitude", "")


        footballersListView.layoutManager = LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)

        val items = ArrayList<Item>()

            val footballerModel = FootballerModel(activity!!.application)

            val allFootballers = footballerModel.allFootballers


            for (i in allFootballers) {
                var imgSrc: Int = 0
                if (i.team == "Aalesund FK") {
                    imgSrc = R.drawable.aafklogo
                } else if (i.team == "IK Start") {
                    imgSrc = R.drawable.startlogo
                } else if (i.team == "KFUM Oslo") {
                    imgSrc = R.drawable.kfumlogo
                } else if (i.team == "Strømmen IF") {
                    imgSrc = R.drawable.strommenlogo
                } else if (i.team == "FK Jerv") {
                    imgSrc = R.drawable.jervlogo
                } else if (i.team == "Skeid") {
                    imgSrc = R.drawable.skeidlogo
                }
                //chooseplayerfragment.addView(teamLogo)
                items.add(Item(i.name, imgSrc, i.position, i.price))
            }


        val adapter = CustomAdapter(items)

        footballersListView.adapter = adapter

        adapter.setOnItemClickListener(object : CustomAdapter.ClickListener {
            override fun onClick(pos: Int, aView: View) {
                val sp = activity!!.getSharedPreferences("button", MODE_PRIVATE)
                val spEditor = sp.edit()
                val intent = Intent(context, MainActivity::class.java)
                var btnIdAndImgRes = ArrayList<Any>()
                val budget = sp.getInt("budget", 0)
                val moneyLeft = budget - items[pos].price
                val txtViewId = sp.getInt("textViewId", 0)


                val setOfTxtViewIds = sp.getStringSet("setOfTxtViewIds", null)
                spEditor.putInt("budget", moneyLeft)
                spEditor.commit()
                intent.putExtra("team", items[pos].teamLogo)
                intent.putExtra("name", items[pos].name)
                intent.putExtra("playerposition", "")
                if (items[pos].teamLogo == R.drawable.aafklogo) {
                    btnIdAndImgRes = arrayListOf(sp.getInt("btnID", 0), R.drawable.aafkdrakt)
                } else if (items[pos].teamLogo == R.drawable.startlogo) {
                    btnIdAndImgRes = arrayListOf(sp.getInt("btnID", 0), R.drawable.startdrakt)
                } else if (items[pos].teamLogo == R.drawable.kfumlogo) {
                    btnIdAndImgRes = arrayListOf(sp.getInt("btnID", 0), R.drawable.kfumdrakt)
                } else if (items[pos].teamLogo == R.drawable.strommenlogo) {
                    btnIdAndImgRes = arrayListOf(sp.getInt("btnID", 0), R.drawable.strommendrakt)
                } else if (items[pos].teamLogo == R.drawable.jervlogo) {
                    btnIdAndImgRes = arrayListOf(sp.getInt("btnID", 0), R.drawable.jervdrakt)
                } else if (items[pos].teamLogo == R.drawable.skeidlogo) {
                    btnIdAndImgRes = arrayListOf(sp.getInt("btnID", 0), R.drawable.skeiddrakt)
                }

                if (setOfTxtViewIds != null) {
                    setOfTxtViewIds.add(txtViewId.toString())
                    spEditor.putStringSet("setOfTxtViewIds", setOfTxtViewIds)
                    spEditor.commit()
                } else {
                    val txtViewIdSet = mutableSetOf(txtViewId.toString())
                    spEditor.putStringSet("setOfTxtViewIds", txtViewIdSet)
                    spEditor.commit()
                }

                val savedBtnAndImages = sp.getStringSet("btnidandimgres", null)

                if (savedBtnAndImages != null){
                    savedBtnAndImages.add(btnIdAndImgRes.toString())
                    spEditor.putStringSet("btnidandimgres", savedBtnAndImages)
                    spEditor.commit()
                }else {
                    intent.putExtra("btnidandimgres", btnIdAndImgRes)
                }

                val savedNamesAndTxtViewIds = sp.getStringSet("namesAndTxtViewIds", null)
                if (savedNamesAndTxtViewIds == null) {
                    val namesAndTxtViewIds = mutableSetOf(items[pos].name, txtViewId.toString())
                    val playerNames = mutableSetOf(namesAndTxtViewIds.toString())
                    spEditor.putStringSet("namesAndTxtViewIds", playerNames)
                    spEditor.commit()
                } else {
                    val  savedPlayerNames = mutableSetOf(items[pos].name, txtViewId.toString())
                    savedNamesAndTxtViewIds.add(savedPlayerNames.toString())
                    spEditor.putStringSet("namesAndTxtViewIds", savedNamesAndTxtViewIds)
                    spEditor.commit()
                }

                startActivity(intent)

            }
        })

        return fView
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        Log.d("changed", "something changed")
    }

    override fun onSensorChanged(event: SensorEvent?) {

        Handler().postDelayed({
            var acelVal = SensorManager.GRAVITY_EARTH
            var acelLast = SensorManager.GRAVITY_EARTH

            val x = event!!.values[0]
            val z = event.values[1]
            val y = event.values[2]

            acelLast = acelVal
            acelVal = Math.sqrt(((x * x + y * y + z * z).toDouble())).toFloat()

            val delta = acelVal - acelLast

            shake = shake * 0.9f + delta

            if (shake > 100) {

                val sp = activity!!.getSharedPreferences("button", MODE_PRIVATE)
                val editor = sp.edit()
                val db = FirebaseDatabase.getInstance()
                val ref = db.getReference("team")
                ref.setValue(null)
                editor.clear()
                editor.commit()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }, 200)
    }

}