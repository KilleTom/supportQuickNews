package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.delicacyDetails

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.alexvasilkov.foldablelayout.UnfoldableView
import com.r0adkll.slidr.Slidr
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.FindRecipes
import com.ypz.supportquicknews.rxUtil.RxToast
import kotlinx.android.synthetic.main.activity_delicacy_details.*

class DelicacyDetailsActivity : FragmentActivity(), DelicacyDetailsNetModule.DelicacyDetailsCallback, DelicacyDetailsAdapter.DelicacyDetailsAdapterFolderCallback {
    override fun openFloder(view: View, imageUrl: String, title: String, description: String) {
        runOnUiThread({
           /* Glide.with(this).load(imageUrl).into(details_image)
            details_title.text = title
            details_text.text = description
            unfoldable_view.unfold(view, details_layout)*/
        })
    }

    override fun sucessful(datum: FindRecipes.Datum?) {
        runOnUiThread({
            list_view.adapter = DelicacyDetailsAdapter(this, datum, this)
        })
    }

    override fun error(message: String?) {
        RxToast.error(this, message+"").show()
        Log.i("ypz",message+"")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delicacy_details)
        val layoutManager = LinearLayoutManager(this)
        list_view.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        list_view.layoutManager = layoutManager
        touch_interceptor_view.isClickable = false
        details_layout.visibility = View.INVISIBLE
        unfoldable_view.setOnFoldingListener(object : UnfoldableView.OnFoldingListener {
            override fun onUnfolded(unfoldableView: UnfoldableView?) {
                touch_interceptor_view.isClickable = false
            }

            override fun onFoldingBack(unfoldableView: UnfoldableView?) {
                touch_interceptor_view.isClickable = true
            }

            override fun onUnfolding(unfoldableView: UnfoldableView?) {
                details_layout.visibility = View.VISIBLE
                touch_interceptor_view.isClickable = true
            }

            override fun onFoldProgress(unfoldableView: UnfoldableView?, progress: Float) {

            }

            override fun onFoldedBack(unfoldableView: UnfoldableView?) {
                touch_interceptor_view.isClickable = false
                details_layout.visibility = View.INVISIBLE
            }
        })
        DelicacyDetailsNetModule(this).getDelicacyDetails(intent.getIntExtra("id",101))
        Slidr.attach(this)
    }

    override fun onBackPressed() {
        if (unfoldable_view != null && (unfoldable_view.isUnfolded || unfoldable_view.isUnfolding)) {
            unfoldable_view.foldBack()
        } else {
            finish()
        }
    }
}
