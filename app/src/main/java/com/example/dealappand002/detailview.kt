package com.example.dealappand002

import android.R.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ViewUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.dealappand002.ui.home.HomeFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_detailview.*
import kotlinx.android.synthetic.main.pagerimageview.view.*
import kotlinx.android.synthetic.main.rowlayout.view.*
import java.util.zip.Inflater


class detailview : AppCompatActivity() {

    var arrayAllProdTitle: Array<String> = Array(255, { i -> "" })
    var arrayAllProdWriter: Array<String> = Array(255, { i -> "" })
    var arrayAllProdDate: Array<String> = Array(255, { i -> "" })
    var arrayAllProdPrice: Array<Int> = Array(255, { i -> 0 })
    var arrayState: Array<Int> = Array(255, { i -> 0 })
    var index = 0
    val TAG = "detailview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailview)

        index = intent.getIntExtra("index",0)

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        db.collection("TableData")
            .orderBy("Date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                var i = 0

                for (document in result) {
                    Log.d(TAG, "data" + document.getString("Title"))
                    arrayAllProdTitle.set(i, document.getString("Title").toString())
                    arrayAllProdWriter.set(i, document.getString("Writer").toString())
                    document.getDouble("Price")?.toInt()?.let { arrayAllProdPrice.set(i, it) }
                    arrayAllProdDate.set(i, document.getString("Date").toString())
                    document.getDouble("State")?.toInt()?.let { arrayState.set(i, it) }
                    i += 1
                }


                TitletextView.text = arrayAllProdTitle.get(index)
                WritertextView.text = arrayAllProdWriter.get(index) + "  작성일 : " + arrayAllProdDate.get(index)

                if(arrayState[index] == 1){
                    PricetextView.text = "(판매중)가격 : "+arrayAllProdPrice.get(index).toString() + "원"
                }
                else{
                    PricetextView.text = "(판매완료)"
                }



                viewPager2.adapter = PagerAdpater(this)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }





    }


    private class PagerAdpater(private val context: Context): PagerAdapter(){

        val arrayImage = arrayOf(
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_home_black_24dp
        )
        val arraycolor = arrayOf(
            Color.RED,
            Color.BLUE,
            Color.BLACK,
            Color.GREEN,
            Color.YELLOW
        )

        override fun getCount(): Int {
            return 5
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater = LayoutInflater.from(container.context)
            val view = layoutInflater.inflate(R.layout.pagerimageview, container, false)
            val imageView = view.findViewById<ImageView>(R.id.imageView)

            imageView.setImageResource(arrayImage[position])
            view.setBackgroundColor(arraycolor[position])

            container.addView(view)

            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View?)
        }

    }


}


