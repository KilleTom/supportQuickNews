package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.findRecipes

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.r0adkll.slidr.Slidr
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.FindRecipes
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.TagDetails
import com.ypz.supportquicknews.rxUtil.MyBmobUtil
import com.ypz.supportquicknews.rxUtil.RxToast
import com.ypz.supportquicknews.statistics.CycletimesFunctionStatistics
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_find_recipes.*
import kotlinx.android.synthetic.main.app_bar_find_recipes.*
import kotlinx.android.synthetic.main.content_find_recipes.*
import java.text.SimpleDateFormat
import java.util.*

class FindRecipesActivity : AppCompatActivity(), FindRecipesNetModule.FindRecipesCallback {
    private lateinit var findRecipesNetModule: FindRecipesNetModule
    private var isRefresh = false
    private var isLoadMore = false
    private var list :MutableList<FindRecipes.Datum> ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_recipes)
        findRecipesNetModule = FindRecipesNetModule(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        fab.setOnClickListener {
            Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action") {  }.show()
        }
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        findRecipesNetModule.getNetTag()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layoutManager
        refresh.setDisableContentWhenRefresh(true)
        refresh.setDisableContentWhenLoading(true)
        refresh.setOnRefreshListener {
            isRefresh = true
            isLoadMore = false
            findRecipesNetModule.refrseh()
        }
        refresh.setOnLoadmoreListener {
            isLoadMore = true
            isRefresh = false
            findRecipesNetModule.loadMore()
        }
        Slidr.attach(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.find_recipes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("ypz", "点击了")
        if (!TextUtils.isEmpty(name.editableText)){
            findRecipesNetModule.getNameSearch(name.editableText.toString(), true)
            comitFunctionStatistics()
        }
        return true
    }

    override fun sucessTag(tagItems: MutableList<TagDetails.TagItem>) = try {
        runOnUiThread({
            Log.i("ypz", "set")
            nav_view.adapter = FlowAdapter(tagItems, this)
            nav_view.setOnTagClickListener(TagFlowLayout.OnTagClickListener(
                    { view, position, parent ->
                             findRecipesNetModule.getTagSearch(tagItems[position].id, true)
                        Log.i("ypz", tagItems[position].id.toString() + "")
                        comitFunctionStatistics()
                        drawer_layout.closeDrawer(GravityCompat.START)
                        true
                    }))
        })
    } catch (e: Exception) {
        error(e.message + "")
    }

    override fun error(message: String) {

        runOnUiThread({
            try {
                if (isRefresh) {
                    finshRefreshStatus()
                } else if (isLoadMore) {
                    finshRefreshStatus()
                }
                RxToast.error(this, message + "").show()
                Log.i("ypz", message + ""+"......")
            } catch (e: Exception) {
                Log.i("ypz", e.message)
            }
        })
    }

    override fun secess(data: List<FindRecipes.Datum>) {
        if (isRefresh || isLoadMore) {
            if (isRefresh){
                Log.i("ypz",list!![0].id.toString())
                list = data as MutableList<FindRecipes.Datum>
                Log.i("ypz",list!![0].id.toString())
            }else if (isLoadMore){
                if (list!=null) list!!.addAll(data)
                else list = data as MutableList<FindRecipes.Datum>
            }
            finshRefreshStatus()
            runOnUiThread({rv.adapter = CookingItemAdapter(list, this) })
        }  else {
            list = data as MutableList<FindRecipes.Datum>
            runOnUiThread({rv.adapter = CookingItemAdapter(list, this) })
        }
    }

    private fun finshRefreshStatus() {
        isRefresh = false
        if (refresh.isRefreshing) runOnUiThread({refresh.finishRefresh()})
        isLoadMore = false
        if (refresh.isLoading) runOnUiThread({refresh.finishLoadmore()})
    }

    private fun comitFunctionStatistics(){
        Thread({
            val date = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(System.currentTimeMillis()))
            val yearMonth = date.substring(0,8)
            MyBmobUtil.getMyBmobUtil(this).updateFunctionStatistics(
                    1,
                    CycletimesFunctionStatistics(yearMonth,"All","所有功能统计数"),
                    CycletimesFunctionStatistics(yearMonth,"LifeHelper","FindRecipes"),
                    CycletimesFunctionStatistics(yearMonth,"LifeHelper","生活助手统计总数"))
        }).start()
    }
}
