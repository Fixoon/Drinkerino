package com.example.oskar.drinkerino.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox

class CheckBoxAdapter(var context: Context, private var checkBoxNames: Array<String>, private var checkedBoxes: BooleanArray) : BaseAdapter(){
    private var checkBoxes: ArrayList<CheckBox> = arrayListOf()

    override fun getCount(): Int {
        return checkBoxNames.size
    }

    override fun getItem(pos: Int): Any {
        return checkBoxes[pos]
    }

    override fun getItemId(pos: Int): Long {
        return 0
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val checkBox = CheckBox(context)

        if (convertView == null) {
            checkBox.text = checkBoxNames[pos]
            checkBox.isChecked = checkedBoxes[pos]
            checkBoxes.add(checkBox)

            convertView = checkBox

            convertView.tag = checkBox
        } else {
            convertView = convertView.tag as CheckBox
        }

        return convertView
    }
}
