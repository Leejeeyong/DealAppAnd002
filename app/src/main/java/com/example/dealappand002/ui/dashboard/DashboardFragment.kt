package com.example.dealappand002.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dealappand002.MainPage
import com.example.dealappand002.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val TAG = "DashBoardFrament"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        root.registDeal_button.setOnClickListener{

            val tabledata = hashMapOf(
                "Writer" to "Alan",
                "Title" to EditTitleUpload.text.toString(),
                "Price" to EditPriceUpload.text.toString().toInt(),
                "Date" to "20200808",
                "State" to 1,
                "ImageInf" to "aaaa"
            )

            // Add a new document with a generated ID
            db.collection("TableData")
                .add(tabledata)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(this.requireContext(), "게시글 등록 성공", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this.requireContext(),MainPage::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(this.requireContext(), "게시글 등록 실패", Toast.LENGTH_SHORT).show()
                }
        }



        return root
    }

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(EditPriceUpload.windowToken,  0)
        imm.hideSoftInputFromWindow(EditTitleUpload.windowToken,  0)
    }
}