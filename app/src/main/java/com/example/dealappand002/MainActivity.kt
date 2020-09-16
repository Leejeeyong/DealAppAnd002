package com.example.dealappand002

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()


        ButtonLoginAction.setOnClickListener {

            db.collection("UserData")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(editTextID.text.toString() == document.getString("ID")){
                            Log.d(TAG,"아이디 잉치")
                            if(editTextPassword.text.toString() == document.getString("PASSWORD")){
                                Log.d(TAG,"비밀번호 잉치")
                                var intent = Intent(this,MainPage::class.java)
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(applicationContext, "비밀번호 오류", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

        }
    }


}