package com.example.dealappand002.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dealappand002.MainPage
import com.example.dealappand002.R
import com.example.dealappand002.detailview
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var arrayAllProdTitle: Array<String> = Array(255, { i -> "" })
    var arrayAllProdWriter: Array<String> = Array(255, { i -> "" })
    var arrayAllProdDate: Array<String> = Array(255, { i -> "" })
    var arrayAllProdPrice: Array<Int> = Array(255, { i -> 0 })
    var arrayState: Array<Int> = Array(255, { i -> 0 })
    private val TAG = "HomeFrament"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val listView: ListView = root.findViewById(R.id.listviewAllProd)


        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        db.collection("TableData")
            .orderBy("Date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                var i = 0
                for (document in result) {
                    Log.d(TAG, "data" + i.toString() + "\n" + document.getString("Title"))
                    arrayAllProdTitle.set(i, document.getString("Title").toString())
                    arrayAllProdWriter.set(i, document.getString("Writer").toString())
                    document.getDouble("Price")?.toInt()?.let { arrayAllProdPrice.set(i, it) }
                    arrayAllProdDate.set(i, document.getString("Date").toString())
                    document.getDouble("State")?.toInt()?.let { arrayState.set(i, it) }
                    i++
                }
                homeViewModel.text.observe(viewLifecycleOwner, Observer {
                    listView.adapter = this.context?.let { it1 -> MyAdapoter(it1, arrayAllProdTitle,arrayAllProdWriter,arrayAllProdDate,arrayAllProdPrice) }

                    listView.setOnItemClickListener { adapterView, view, i, l ->
                        activity?.let{
                            val iT = Intent(context, detailview::class.java)
                            iT.putExtra("index",i)
                            startActivity(iT)
                        }
                    }

                })
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }




        return root
    }



    private class MyAdapoter(context: Context, arrayTitle: Array<String>, arrayWriter: Array<String>, arrayDate: Array<String>, arrayPrice: Array<Int>) : BaseAdapter() {
        private val mContext: Context
        private val TAG = "HomeFragment_Adapter"
        private val marrayTitle: Array<String>
        private val marrayWriter: Array<String>
        private val marrayDate: Array<String>
        private val marrayPrice: Array<Int>


        init {
            mContext = context
            marrayTitle = arrayTitle
            marrayWriter = arrayWriter
            marrayDate = arrayDate
            marrayPrice = arrayPrice
        }

        override fun getCount(): Int {
            var i = 0
            var countNum = 0

            for (i in 0..marrayTitle.size) {
                if (marrayTitle.get(i).length == 0) {
                    break
                }
                countNum++
            }

            return countNum
        }

        override fun getItem(p0: Int): Any {

            var title = marrayTitle.get(p0)

            return title
        }

        override fun getItemId(p0: Int): Long {

            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.rowlayout, p2, false)

            val image: ImageView = rowMain.findViewById(R.id.imageView2)
            val titleText: TextView = rowMain.findViewById(R.id.TitletextView)
            val writerText: TextView = rowMain.findViewById(R.id.WriterTextView)
            val dateText: TextView = rowMain.findViewById(R.id.DateTextView)
            val priceText: TextView = rowMain.findViewById(R.id.PriceTextView)

            image.setImageResource(R.drawable.ic_home_black_24dp)
            titleText.text = marrayTitle.get(p0)
            writerText.text = marrayWriter.get(p0)
            dateText.text = marrayDate.get(p0)
            priceText.text = marrayPrice.get(p0).toString() + "원"



            return rowMain
        }
    }
}