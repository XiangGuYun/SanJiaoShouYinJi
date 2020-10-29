package android_serialport_api.cardwriteread.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android_serialport_api.cardwriteread.meal.AllCaiTypeModel
import android_serialport_api.cardwriteread.meal.MyListAdapter
import android_serialport_api.cardwriteread.meal.RV
import kotlinx.android.synthetic.main.activity_meal.*
import java.util.ArrayList

object KotlinHelper {

    fun initDishClassListView(ctx:Context, listview_right: ListView, listDishClass:List<AllCaiTypeModel.DataBean.ResultsBean>) {
//        val adapter = MyListAdapter(ctx, listDishClass)
//        listview_right.adapter = adapter
//        listview_right.onItemClickListener = AdapterView.OnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
//            selectedClassIndex = position
//            adapter.curPositon = position
//            adapter.notifyDataSetChanged()
//            listview_right.smoothScrollToPositionFromTop(position, (parent.height - view.height) / 2)
//            if (listDishClass.size == 0) {
//                return@OnItemClickListener
//            }
//            var childListBeanList = listDishClass[position].getmList()
//            if (childListBeanList == null) {
//                childListBeanList = ArrayList()
//            }
//            listDishes.clear()
//            listDishes.addAll(childListBeanList)
//            RV.update(gridView)
//        }
    }


}