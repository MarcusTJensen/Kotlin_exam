package com.example.testdb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.coroutines.experimental.async
import java.net.HttpURLConnection
import java.net.URL

class MenuActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_menu)

        sendGet()

        val sp = getSharedPreferences("table", MODE_PRIVATE)
        val spEditor = sp.edit()


        val fTransaction = supportFragmentManager.beginTransaction()

        val myTeamBtn: Button = findViewById(R.id.button_team)
        val tableBtn: Button = findViewById(R.id.button_table)

        myTeamBtn.setOnClickListener {

            spEditor.putBoolean("isComingFromMenu", true)
            spEditor.commit()

            val intent = Intent(applicationContext, MainActivity::class.java)

            startActivity(intent)
        }
        tableBtn.setOnClickListener {

            fTransaction.replace(R.id.menu_fragment, TableFragment())
            fTransaction.commit()
            tableBtn.visibility = View.INVISIBLE
            myTeamBtn.visibility = View.INVISIBLE
        }

    }

    fun sendGet() = async {

        Looper.prepare()
        val teamInfoList = ArrayList<TableInfo>()

        val url = URL("https://api-football-v1.p.rapidapi.com/leagueTable/288")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET
            setRequestProperty(
                "X-RapidAPI-Key",
                "bca57e1d82msh57a987c5d1e492ap1286b5jsne5f2af843e14"
            )

            val sp = getSharedPreferences("table", MODE_PRIVATE)
            val spEditor = sp.edit()

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->

                    val splitLine = line.split("}")
                    println(splitLine)
                    for (i in splitLine) {

                        if (i == splitLine[0]) {
                            val teamInfo = i.substring(43)
                            println(teamInfo)
                        } else {

                            val teamInfo = i.split(",")
                            val rank = teamInfo[1].substring(9)
                            val name = teamInfo[3].substring(10)
                            val matchesPlayed = teamInfo[4].substring(14)
                            val wins = teamInfo[5].substring(5)
                            val draws = teamInfo[6].substring(6)
                            val losses = teamInfo[7].substring(6)
                            val goalDiff = teamInfo[10].substring(11)
                            val points = teamInfo[11].substring(8)
                            println(name)
                            println(matchesPlayed)
                            println(wins)
                            println(draws)
                            println(losses)
                            println(goalDiff)
                            println(points)
                            println(rank)

                            teamInfoList.add(
                                TableInfo(
                                    rank, name, matchesPlayed, wins,
                                    draws, losses, goalDiff, points
                                )
                            )

                            spEditor.putStringSet("tableInfo", mutableSetOf(teamInfoList.toString()))
                            spEditor.commit()
                        }
                    }
                }
            }
        }
    }
}