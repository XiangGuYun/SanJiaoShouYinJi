package android_serialport_api.cardwriteread.jiedan

import android_serialport_api.cardwriteread.meal.BusUtils
import android_serialport_api.cardwriteread.view.MainActivity

/**
 * MainActivity帮助类
 */
class MainActHelper private constructor(private val mainActivity: MainActivity) {


    public var isShow: Boolean = false

    companion object {
        private var helper: MainActHelper? = null

        fun getInstance(mainActivity: MainActivity?=null): MainActHelper {
            if (helper == null && mainActivity != null) {
                helper = MainActHelper(mainActivity)
            }
            return helper!!
        }
    }

    fun showFragment(id:Int){
//        if(fu == null){
//            fu = FragmentUtils(mainActivity, frag, id)
//        } else {
//            fu?.show(frag)
//        }
        isShow = true
        BusUtils.sendMsg(0x124)
        if(mainActivity.isJieDan){
            mainActivity.fu.show(mainActivity.fragJieDan)
        } else {
            mainActivity.fu.show(mainActivity.fragWeight)
        }
    }

    fun hide(){
        isShow = false
        if(mainActivity.isJieDan){
            mainActivity.fu?.hide(mainActivity.fragJieDan)
        } else {
            mainActivity.fu?.hide(mainActivity.fragWeight)
        }
    }

    fun hideFragment(needShowShouYin:Boolean){
        isShow = false
        if(mainActivity.isJieDan){
            mainActivity.fu?.hide(mainActivity.fragJieDan)
        } else {
            mainActivity.fu?.hide(mainActivity.fragWeight)
        }
        if(needShowShouYin){
            mainActivity.showShouYin()
        } else {
            mainActivity.showDianCan()
        }
    }


}