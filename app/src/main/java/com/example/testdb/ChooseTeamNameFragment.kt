package com.example.testdb

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase

class ChooseTeamNameFragment : Fragment() {

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         val fView = inflater.inflate(R.layout.fragment_choose_team_name, container, false)

         val confirmNameBtn: Button = fView.findViewById(R.id.confirm_name_button)
         val teamNameEditText: EditText =fView.findViewById(R.id.team_name)

         val sp = MainActivity().getSharedPreferences("button", AppCompatActivity.MODE_PRIVATE)
         val spEditor = sp.edit()
         val isFirstLaunch = sp.getBoolean("isFirstLaunch", false)

         val firebaseDb = FirebaseDatabase.getInstance()
         val teamNameRef = firebaseDb.getReference("teamname")

         val teamName = teamNameEditText.text.toString()

         confirmNameBtn.setOnClickListener {
             teamNameRef.setValue(teamName)
             val intent = Intent(context, MainActivity::class.java)
             startActivity(intent)
             spEditor.putBoolean("isFirstLaunch", true)
             spEditor.commit()
         }

        return fView
    }
}