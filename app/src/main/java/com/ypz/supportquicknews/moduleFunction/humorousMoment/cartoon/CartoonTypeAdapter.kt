package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox

import com.ypz.supportquicknews.R

/**
 * Created by kingadmin on 2018/3/11.
 */

class CartoonTypeAdapter(private val typeText: List<TypeText>?, private val context: Context, private val cartoonNetModule: CartoonNetModule, private val comictypecheck: Int) : RecyclerView.Adapter<CartoonTypeAdapter.TextViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TextViewHolder {
        return TextViewHolder(LayoutInflater.from(context).inflate(R.layout.comic_check_bg, viewGroup, false))
    }

    override fun onBindViewHolder(textViewHolder: TextViewHolder, i: Int) {
        textViewHolder.setCheckBoxChange(i)
    }

    override fun getItemCount(): Int {
        return typeText?.size ?: 0
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox

        init {
            checkBox = itemView.findViewById(R.id.comictypecheck)
        }

        fun setCheckBoxChange(index: Int) {
            checkBox.text = typeText!![index].text
            checkBox.isChecked = typeText[index].isIscheck
            checkBox.setOnCheckedChangeListener { compoundButton, b ->
                Log.i("ypz", "set$b")
                if (b) {
                    for (typeText in typeText) {
                        typeText.isIscheck = false
                    }
                    typeText[index].isIscheck = true
                    notifyDataSetChanged()
                    Log.i("ypz", comictypecheck.toString() + "")
                    when (comictypecheck) {
                        0 -> cartoonNetModule.setTypeKey(typeText[index].text)
                        1 -> cartoonNetModule.setMapKey(typeText[index].text)
                    }
                } else {
                    if (typeText[index].isIscheck) {
                        checkBox.isChecked = true
                    } else
                        checkBox.isChecked = b
                }
            }
        }
    }
}
