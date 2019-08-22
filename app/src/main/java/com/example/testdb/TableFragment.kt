package com.example.testdb

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.WorkerThread
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.common.api.Response
import kotlinx.android.synthetic.main.fragment_table.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import org.json.JSONObject
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.absoluteValue

class TableFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val fView = inflater.inflate(R.layout.fragment_table, container, false)

        val tableView: RecyclerView = fView.findViewById(R.id.tableView)

        tableView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        val tableInfo = ArrayList<TableInfo>()

        val sp = activity!!.getSharedPreferences("table", MODE_PRIVATE)

        val backBtn: Button = fView.findViewById(R.id.buttonBack)

        backBtn.setOnClickListener {

            val intent = Intent(context, MenuActivity::class.java)
            startActivity(intent)
        }

        val table = sp.getStringSet("tableInfo", null)

        Log.d("heiahie", table.toString())

        for (i in table) {

            val splitTeamInfo = i.split(")")
            for (i in splitTeamInfo) {
                val splitValues = i.split(",")

                Log.d("yeyayo", splitValues[0])
                if (splitValues.size > 1) {
                    if (!splitValues[1].contains("name")) {
                        val rank = splitValues[1].replace("TableInfo(rank=", "")
                            .replace('"', ' ')
                            .replace(" ", "")
                        Log.d("yayayaya", rank)
                        val name = splitValues[2].replace("name=:", "")
                            .replace('"', ' ')
                            .replace(" ", "")
                        val games = splitValues[3].replace("games=:", "")
                            .replace('"', ' ')
                            .replace(" ", "")
                        val wins = splitValues[4].replace("wins=:", "")
                            .replace('"', ' ')
                            .replace(" ", "")
                        val draws = splitValues[5].replace("draws=:", "")
                            .replace('"', ' ')
                            .replace(" ", "")
                        val losses = splitValues[6].replace("losses=:", "")
                            .replace('"', ' ')
                            .replace(" ", "")
                        val goalDiff = splitValues[7].replace("goalDiff=:", "")
                            .replace('"', ' ')
                            .replace(" ", "")
                        val points = splitValues[8].replace("points=:", "")
                            .replace('"', ' ')
                            .replace(" ", "")

                        Log.d("yoheiadu", rank + name + games + wins + draws + losses + goalDiff)

                        tableInfo.add(TableInfo(rank, name, games, wins, draws, losses, goalDiff, points))

                    }
                } else {


                }
            }

        }

        val adapter = TableAdapter(tableInfo)

        tableView.adapter = adapter

        return fView
    }

}