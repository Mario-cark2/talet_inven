package com.example.invenio_talet

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class CustomExpandableListAdapter(
    private val context: Context,
    private val expandableListTitle: List<String>,
    private val expandableListDetail: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {
    private var originalData: HashMap<String, List<String>> = expandableListDetail

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return expandableListDetail[expandableListTitle[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.expandedListItem)
        expandedListTextView.text = expandedListText
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        val groupTitle = expandableListTitle[listPosition]

        val childrenList = expandableListDetail[groupTitle]
        Log.e("asd",groupTitle+childrenList);
        return childrenList?.size ?: 0
    }


    override fun getGroup(listPosition: Int): Any {
        return expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int {
        return expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var groupView = convertView
        val listTitle = getGroup(listPosition) as String
        if (groupView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            groupView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val listTitleTextView = groupView!!.findViewById<TextView>(R.id.listTitle)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return groupView
    }
    fun updateData(newData: HashMap<String, List<String>>) {
        originalData = newData
        notifyDataSetChanged()
    }

    fun filterData(query: String?) {
        val filteredData: HashMap<String, List<String>> = HashMap()

        if (!query.isNullOrBlank()) {
            for (title in originalData.keys) {
                val filteredList: List<String> = originalData[title]!!.filter { item ->
                    // Filtra si el título, contenido o autor contiene la consulta (query)
                    item.contains(query, ignoreCase = true)
                }
                if (filteredList.isNotEmpty()) {
                    filteredData[title] = filteredList
                }
            }
        } else {
            filteredData.putAll(originalData)
        }

        // Actualiza directamente el adaptador con los datos filtrados
        updateData(filteredData)
        notifyDataSetChanged()
    }


    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
