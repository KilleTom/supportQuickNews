package com.ypz.supportquicknews.moduleFunction.loginOrRegister

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.r0adkll.slidr.Slidr
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.databinding.ActivityLoginRegisterBinding
import kotlinx.android.synthetic.main.activity_login_register.*

class LoginRegisterActivity : AppCompatActivity(), LoginRegisterUIModule.Load {

    override fun loginLoad() = ghost.startAnim()

    override fun loginCancle() = ghost.stopAnim()

    override fun registerLoad() = block.startAnim()

    override fun registerCancle() = block.stopAnim()

    private lateinit var loginRegisterBinding: ActivityLoginRegisterBinding
    private lateinit var loginRegisterUIModule: LoginRegisterUIModule
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**通过DataBingdingUtil获取布局对象然后实例化布局对象对应的Module*/
        loginRegisterBinding=DataBindingUtil.setContentView(this, R.layout.activity_login_register)
        Slidr.attach(this)
        StatusBarUtil.setTranslucentForImageView(this,head)
        loginRegisterUIModule = LoginRegisterUIModule(this, this)
        loginRegisterBinding.uiModule = loginRegisterUIModule
        username.addTextChangedListener(loginRegisterUIModule.phoneWatcher)
        userpwd.addTextChangedListener(loginRegisterUIModule.pwdWatcher)
        usersms.addTextChangedListener(loginRegisterUIModule.smsWatcher)
    }

    override fun onBackPressed() = finish()
}
