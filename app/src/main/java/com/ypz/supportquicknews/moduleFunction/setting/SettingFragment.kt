package com.ypz.supportquicknews.moduleFunction.setting

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.base.RecyclerViewDivider
import com.ypz.supportquicknews.databinding.FragmentSettingBinding
import com.ypz.supportquicknews.preservationData.PreservationDataSP
import java.util.*

/**
 * Created by kingadmin on 2018/3/5.
 */

class SettingFragment : Fragment() {

    private lateinit var settingBinding: FragmentSettingBinding
    private lateinit var settingUIModule: SettingUIModule
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        settingUIModule = SettingUIModule(context!!)
        settingBinding.uiModule = settingUIModule
        recyclerView = settingBinding.root.findViewById<RecyclerView>(R.id.setting_rv)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(RecyclerViewDivider(
                context, LinearLayoutManager.HORIZONTAL,
                4, Color.parseColor("#ffff8800")))
        recyclerView.addItemDecoration(
                RecyclerViewDivider(context, LinearLayoutManager.HORIZONTAL,
                        4, Color.parseColor("#9FDA69"))
                , 1)
        show()
        return settingBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(PreservationDataSP.getPreservationDataSP()==null) PreservationDataSP.init(context)
        if (PreservationDataSP.getPreservationDataSP().getUserId("") != null) {
            if (TextUtils.isEmpty(PreservationDataSP.getPreservationDataSP().getUserId("")))
                notUserDate()
            else {
                settingUIModule.icVis = View.VISIBLE
                settingUIModule.viis = View.GONE
                if (TextUtils.isEmpty(PreservationDataSP.getPreservationDataSP().userIcomUrl)) {
                    settingUIModule.iconUrl = "http://bmob-cdn-11590.b0.upaiyun.com/2018/04/08/4189a3ff407100b3805a2c76f36a8aa3.png"
                } else {
                    settingUIModule.iconUrl = PreservationDataSP.getPreservationDataSP().userIcomUrl
                }
            }
        }else notUserDate()


    }

    private fun notUserDate() {
        settingUIModule.icVis = View.GONE
        settingUIModule.viis = View.VISIBLE
    }

    fun show() {
        Log.i("ypz", "setAdapter")
        recyclerView.adapter = SettingAdapter(
                Arrays.asList(
                        SettingItem("统计历史", R.drawable.ic_his, SettingItem.ItemClickToDo { })
                ), context!!.applicationContext)
    }

    fun  showUi(){
        if(PreservationDataSP.getPreservationDataSP()==null) PreservationDataSP.init(context)
        if (PreservationDataSP.getPreservationDataSP().getUserId("") != null) {
            if (TextUtils.isEmpty(PreservationDataSP.getPreservationDataSP().getUserId("")))
                notUserDate()
            else {
                settingUIModule.icVis = View.VISIBLE
                settingUIModule.viis = View.GONE
                if (TextUtils.isEmpty(PreservationDataSP.getPreservationDataSP().userIcomUrl)) {
                    settingUIModule.iconUrl = "http://bmob-cdn-11590.b0.upaiyun.com/2018/04/08/4189a3ff407100b3805a2c76f36a8aa3.png"
                } else {
                    settingUIModule.iconUrl = PreservationDataSP.getPreservationDataSP().userIcomUrl
                }
            }
        }else notUserDate()
    }
}
