package com.example.testdb

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.FirebaseApp
import kotlin.coroutines.coroutineContext

class CustomAdapter(val itemList: ArrayList<Item>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(itemList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return itemList.size
    }

    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(pos: Int, view: View)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View){
            mClickListener.onClick(adapterPosition, v)
        }

        fun bindItems(item: Item) {
            val textViewName = itemView.findViewById(R.id.textViewName) as TextView
            val textViewAddress  = itemView.findViewById(R.id.imageViewLogo) as ImageView
            val textViewPosition = itemView.findViewById(R.id.positionview) as TextView
            val textViewPrice = itemView.findViewById(R.id.priceview) as TextView
            textViewName.text = item.name
            textViewAddress.setImageResource(item.teamLogo)
            textViewPosition.text = "Posisjon: " + item.pos
            textViewPrice.text = "Pris: " + item.price.toString()
        }
        init {
            itemView.setOnClickListener(this)
        }
    }
}