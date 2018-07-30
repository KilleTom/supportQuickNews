package com.ypz.supportquicknews.moduleFunction.setting

import android.content.Context
import android.content.Intent
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import com.ypz.supportquicknews.BR
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.loginOrRegister.LoginRegisterActivity
import com.ypz.supportquicknews.preservationData.PreservationDataSP


/**
 * Created by kingadmin on 2018/3/5.
 */

class SettingUIModule(private val context: Context) : BaseObservable() {


    fun uiClick(view: View) {
        when (view.id) {
            R.id.login -> {
                startLogin()
            }
            R.id.useric -> {

            }
            R.id.sign -> {

            }
            R.id.exit -> {
                clearUserData()
            }
        }
    }

    fun startLogin() {
        context.startActivity(Intent(context, LoginRegisterActivity::class.java))
    }

    fun isNeedToStartLogin() {

    }

    @Bindable
    var viis = View.VISIBLE
        set(value) {
            field = value
            notifyPropertyChanged(BR.viis)
        }

    @Bindable
    var icVis = View.GONE
        set(value) {
            field = value
            notifyPropertyChanged(BR.icVis)
        }

    @Bindable
    var iconUrl = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.iconUrl)
        }

    private fun clearUserData() {
        icVis = View.GONE
        viis = View.VISIBLE
        PreservationDataSP.getPreservationDataSP().saveUserId("")
        PreservationDataSP.getPreservationDataSP().saveUserNickname("")
        PreservationDataSP.getPreservationDataSP().saveUserSex("")
        startLogin()
    }

}
