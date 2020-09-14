package com.example.dealappand002

import android.R.*
import android.content.Context
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ViewUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailview)

        index = intent.getIntExtra("index",0)


        arrayAllProdTitle.set(0, "첫번째 타이틀fdsfdsafdsfdsfdsfdsfdsfdsfsadfdsfdsaf")
        arrayAllProdWriter.set(0, "이지용")
        arrayAllProdPrice.set(0, 2000)
        arrayAllProdDate.set(0, "20200808")
        arrayState.set(0, 1)

        arrayAllProdTitle.set(1, "두번 타이틀fdsfadsfdsafdsafdsfdsafsadfdsfdsafsdafsdfsdaf\nfsdafdsfasdf")
        arrayAllProdWriter.set(1, "째어용")
        arrayAllProdPrice.set(1, 300000)
        arrayAllProdDate.set(1, "20200808")
        arrayState.set(1, 0)


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


