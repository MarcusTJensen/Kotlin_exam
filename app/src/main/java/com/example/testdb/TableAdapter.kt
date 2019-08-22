package com.example.testdb

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class TableAdapter (val teamInfoList: ArrayList<TableInfo>): RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.table_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return teamInfoList.size
    }

    override fun onBindViewHolder(holder: TableAdapter.ViewHolder, position: Int) {
        holder.bindItems(teamInfoList[position])
    }

    inner class ViewHolder(val tableInfoView: View) : RecyclerView.ViewHolder(tableInfoView){


        fun bindItems(tableInfo: TableInfo) {

            val textViewRank = tableInfoView.findViewById(R.id.textViewRank) as TextView
            val textViewName  = tableInfoView.findViewById(R.id.textViewName) as TextView
            val textViewGames = tableInfoView.findViewById(R.id.textViewGames) as TextView
            val textViewWins = tableInfoView.findViewById(R.id.textViewWins) as TextView
            val textViewDraws = tableInfoView.findViewById(R.id.textViewDraws) as TextView
            val textViewLosses = tableInfoView.findViewById(R.id.textViewLosses) as TextView
            val textViewGoalDiff = tableInfoView.findViewById(R.id.textViewGoalDiff) as TextView
            val textViewPoints = tableInfoView.findViewById(R.id.textViewPoints) as TextView
            textViewName.text = tableInfo.name
            textViewRank.text = tableInfo.rank + "."
            textViewGames.text = "S:  ${tableInfo.games}"
            textViewWins.text = "V: ${tableInfo.wins}"
            textViewDraws.text = "U: ${tableInfo.draws}"
            textViewLosses.text = "T: ${tableInfo.losses}"
            textViewGoalDiff.text = "MF: ${tableInfo.goalDiff}"
            textViewPoints.text = "P: ${tableInfo.points}"

        }
    }
}