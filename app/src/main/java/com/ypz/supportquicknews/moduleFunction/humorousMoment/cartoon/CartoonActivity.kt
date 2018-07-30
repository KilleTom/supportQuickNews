package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import com.r0adkll.slidr.Slidr
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ComicBookResult
import java.util.*

class CartoonActivity : AppCompatActivity(), CartoonNetModule.CartoonNetModuleResult {

    private val comicTypes = ArrayList<TypeText>()
    private var bookLists: List<ComicBookResult.NetBookeList>? = null
    private val finshStatus = Arrays.asList(TypeText("全部", true),
            TypeText("连载", false), TypeText("完结", false))
    private var cartoonNetModule: CartoonNetModule? = null
    private var cartoonTypeAdapter: CartoonTypeAdapter? = null
    private var cartoonFinshStatusAdapter: CartoonTypeAdapter? = null
    private var comictype: RecyclerView? = null
    private var comicstatus: RecyclerView? = null
    private var comiclist: RecyclerView? = null
    private var comicBookAdapter: ComicBookAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartoon)
        try {
            comictype = findViewById(R.id.comictype)
            comicstatus = findViewById(R.id.comicstatus)
            comiclist = findViewById(R.id.comiclist)
            comictype!!.layoutManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
            comicstatus!!.layoutManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
            cartoonNetModule = CartoonNetModule(this, this)
            Slidr.attach(this)
        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }

    }

    override fun getComicType(comicTypes: List<String>) {
        if (comicTypes != null)
            for (type in comicTypes) {
                this.comicTypes.add(TypeText(type, false))
            }
        cartoonTypeAdapter = CartoonTypeAdapter(this.comicTypes, this, cartoonNetModule!!, 0)
        cartoonFinshStatusAdapter = CartoonTypeAdapter(finshStatus, this, cartoonNetModule!!, 1)
        runOnUiThread {
            Log.i("ypz", "runOnUiThread")
            comictype!!.adapter = cartoonTypeAdapter
            comicstatus!!.adapter = cartoonFinshStatusAdapter
        }
        Log.i("ypz", "seeeeeee")
    }


    override fun getBookList(bookLists: List<ComicBookResult.NetBookeList>) {
        Log.i("ypz", "重新设置数据源")
        this.bookLists = bookLists
        comicBookAdapter = ComicBookAdapter(this.bookLists, this)
        try {
            runOnUiThread {
                val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                comiclist!!.layoutManager = staggeredGridLayoutManager
                comiclist!!.adapter = comicBookAdapter
            }
        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }

    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {

    }
}
