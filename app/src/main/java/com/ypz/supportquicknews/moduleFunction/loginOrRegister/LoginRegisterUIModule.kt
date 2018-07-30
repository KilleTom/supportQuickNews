package com.ypz.supportquicknews.moduleFunction.loginOrRegister

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import cn.ypz.com.killetomrxmateria.rxwidget.toast.RxToast
import cn.ypz.com.killetomrxmateria.rxwidget.toast.anim.RxToastType
import com.ypz.supportquicknews.BR
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.base.LR_Sucess
import com.ypz.supportquicknews.preservationData.PreservationDataSP
import com.ypz.supportquicknews.rxUtil.MyBmobUtil
import com.ypz.supportquicknews.rxUtil.RxConstTool
import com.ypz.supportquicknews.statistics.UserData
import org.greenrobot.eventbus.EventBus
import java.util.regex.Pattern

/**
 * Created by kingadmin on 2018/3/6.
 */

class LoginRegisterUIModule(private val loginRegisterActivity: LoginRegisterActivity, private val load: Load) : BaseObservable() {

    private val patter = Pattern.compile(RxConstTool.REGEX_MOBILE_EXACT)
    private var time = 60
    private val handler = Handler()
    private val runnable = Runnable {
        if (time > 0) {
            sendSmsString = "倒计时:" + (time - 1).toString() + "秒"
            time -= 1
            toTime()
        } else {
            sendSmsString = "再次获取"
            sendSmsenable = true
            return@Runnable
        }

    }

    private fun toTime() {
        handler.postDelayed(runnable, 1000)
    }

    @Bindable
    var singleCheckLoginRadio: Boolean = true
        set(value) {
            field = value
            isLoginCheck(value)
            notifyPropertyChanged(BR.singleCheckLoginRadio)
        }
    @Bindable
    var loginRegisterEnable = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loginRegisterEnable)
        }

    @Bindable
    var loginLoadVisisable = View.GONE
        set(value) {
            field = value
            notifyPropertyChanged(BR.loginLoadVisisable)
        }


    @Bindable
    var registerLoadVisisable = View.GONE
        set(value) {
            field = value
            notifyPropertyChanged(BR.registerLoadVisisable)
        }

    @Bindable
    var smsVisisable = View.GONE
        set(value) {
            field = value
            notifyPropertyChanged(BR.smsVisisable)
        }
    @Bindable
    var sendSmsenable = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.sendSmsenable)
        }

    private fun isLoginCheck(check: Boolean) {
        canLoginRegisterEnable()
        if (check) {
            smsVisisable = View.GONE
            btnTextString = R.string.login
            pwdHideText = R.string.scannerPassword
        } else {
            smsVisisable = View.VISIBLE
            btnTextString = R.string.register
            pwdHideText = R.string.scannerRegisterPassword
        }
    }

    @Bindable
    var btnTextString = R.string.login
        set(value) {
            field = value
            notifyPropertyChanged(BR.btnTextString)
        }

    @Bindable
    var phoneText: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.phoneText)
        }

    @Bindable
    var smsText: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.smsText)
        }

    @Bindable
    var pwdText: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.pwdText)
        }

    @Bindable
    var sendSmsString = "发送验证码"
        set(value) {
            field = value
            notifyPropertyChanged(BR.sendSmsString)
        }

    @Bindable
    var pwdHideText = R.string.scannerPassword
        set(value) {
            field = value
            notifyPropertyChanged(BR.pwdHideText)
        }

    val phoneWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            phoneText = p0!!.toString()
            var isReightPhone = if (!TextUtils.isEmpty(phoneText) && patter.matcher(phoneText).matches()) true
            else if (!TextUtils.isEmpty(phoneText) && phoneText!!.length == 11) {
                if (!patter.matcher(phoneText).matches())
                    RxToast.Config.getInstance().setUseAnim(true).show(RxToastType.RxToastWarningType, loginRegisterActivity, "手机格式不正确").apply()
                false
            } else false
            sendSmsenable = isReightPhone
            canLoginRegisterEnable()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }

    val smsWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            smsText = p0!!.toString()
            canLoginRegisterEnable()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }

    val pwdWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            pwdText = p0!!.toString()
            canLoginRegisterEnable()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }

    private fun login() {
        loginRegisterEnable = false
        loginLoadVisisable = View.VISIBLE
        load.loginLoad()
        Thread({
            MyBmobUtil.getMyBmobUtil(loginRegisterActivity).login(phoneText, pwdText, object : MyBmobUtil.LoginCallback {
                override fun sucessful(userData: UserData?) {
                    loginRegisterEnable = true
                    saveData(userData!!)
                    RxToast.choseType(RxToastType.RxToastSuccessType,loginRegisterActivity,"登陆成功").show()
                    loginLoadVisisable = View.GONE
                    load.loginCancle()
                }

                override fun error(message: String?) {
                    RxToast.choseType(RxToastType.RxToastErrorType,loginRegisterActivity, message!!).show()
                    loginLoadVisisable = View.GONE
                    loginRegisterEnable = true
                    load.loginCancle()
                }
            })
        }).start()
    }

    private fun register() {
        loginRegisterEnable = false
        registerLoadVisisable = View.VISIBLE
        load.registerLoad()
        Thread({
            MyBmobUtil.getMyBmobUtil(loginRegisterActivity).register(
                    UserData(phoneText, "男", phoneText, pwdText), smsText, object : MyBmobUtil.RegisterCallback {
                override fun sucessful(userData: UserData?) {
                    loginRegisterEnable = true
                    load.registerCancle()
                    RxToast.choseType(RxToastType.RxToastSuccessType,loginRegisterActivity, "注册成功正在登陆请稍候").show()
                    saveData(userData!!)
                    registerLoadVisisable = View.GONE
                }

                override fun error(message: String?) {
                    RxToast.choseType(RxToastType.RxToastErrorType,loginRegisterActivity, message!!).show()
                    loginRegisterEnable = true
                    load.registerCancle()
                    registerLoadVisisable = View.GONE
                }

            }
            )
        }).start()
    }

    private fun saveData(userData: UserData) {
        PreservationDataSP.getPreservationDataSP().saveUserId(userData.userId)
        PreservationDataSP.getPreservationDataSP().saveUserNickname(userData.nickName)
        PreservationDataSP.getPreservationDataSP().saveUserSex(userData.userSex)
        EventBus.getDefault().post(LR_Sucess())
        loginRegisterActivity.finish()
    }

    fun uiOnClick(view: View) {
        when (view.id) {
            R.id.login_register -> {
                try {
                    if (singleCheckLoginRadio) login()
                    else register()
                } catch (e: Exception) {
                    Log.i("ypz", e.message)
                }
            }
            R.id.sendsms_btn -> {
                Thread({
                    MyBmobUtil.getMyBmobUtil(loginRegisterActivity).sendSmsCode(phoneText, object : MyBmobUtil.SendSmsCodeCallback {
                        override fun sucessful() {
                            sendSmsenable = false
                            handler.post(runnable)
                        }

                        override fun error(message: String?) {
                            if (message != null) {
                                Log.i("ypz", message)
                                RxToast.choseType(RxToastType.RxToastErrorType,loginRegisterActivity, message).show()
                            } else RxToast.choseType(RxToastType.RxToastErrorType, loginRegisterActivity,"发送失败请稍候重试").show()
                        }
                    })
                }).start()
            }
            R.id.layout -> {
                val imm = loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isActive) imm.hideSoftInputFromWindow(loginRegisterActivity.window.peekDecorView().windowToken, 0)
            }

        }
    }

    fun canLoginRegisterEnable() {
        if (singleCheckLoginRadio) {
            if ((!TextUtils.isEmpty(phoneText)) && (!TextUtils.isEmpty(pwdText))) {
                loginRegisterEnable = patter.matcher(phoneText).matches() && pwdText!!.length >= 6
                Log.i("ypz", loginRegisterEnable.toString())
            } else loginRegisterEnable = false
        } else {
            if ((!TextUtils.isEmpty(phoneText)) && (!TextUtils.isEmpty(pwdText)) && (!(TextUtils.isEmpty(smsText)))) {
                loginRegisterEnable = patter.matcher(phoneText).matches() && pwdText!!.length >= 6
            } else loginRegisterEnable = false
        }
    }

    interface Load {

        fun loginLoad()

        fun loginCancle()

        fun registerLoad()

        fun registerCancle()
    }

}
