package android_serialport_api.cardwriteread.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.model.db.AppDBHelper;
import android_serialport_api.cardwriteread.net.bean.OrderDetail;


public class OrderDao {

    private AppDBHelper mHelper;
    private SQLiteDatabase mDatabase;

    public OrderDao(AppDBHelper helper) {
        mHelper = helper;
        mDatabase = mHelper.getReadableDatabase();
    }

    public void insertData(OrderDetail orderInfo) {
        ContentValues values = new ContentValues();

        values.put("shopId", orderInfo.getShopId());
        values.put("cashierDeskId", orderInfo.getCashierDeskId());
        values.put("orderNo", orderInfo.getOrderNo());
        values.put("dateTime", orderInfo.getDateTime());
        values.put("orderType", orderInfo.getOrderType());
        values.put("orderTypeStr", orderInfo.getOrderTypeStr());
        values.put("price", orderInfo.getPrice());
        values.put("realPrice", orderInfo.getRealPrice());
        values.put("discountPrice", orderInfo.getDiscountPrice());

        mDatabase.insert(OrderTable.TAB_NAME, null, values);
        System.out.println("写入完成");
    }

    public List<OrderDetail> queryOrderList() {
        String sql = "select * from "+OrderTable.TAB_NAME+" where shopId = '" + Constant.branchId + "' order by id desc limit 30";
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor == null) {
            return null;
        }

        List<OrderDetail> orderInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            OrderDetail orderInfo = new OrderDetail();

            orderInfo.setShopId(cursor.getInt(1));
            orderInfo.setCashierDeskId(cursor.getInt(2));
            orderInfo.setOrderNo(cursor.getString(3));
            orderInfo.setDateTime(cursor.getString(4));
            orderInfo.setOrderType(cursor.getInt(5));
            orderInfo.setOrderTypeStr(cursor.getString(6));
            orderInfo.setPrice(cursor.getString(7));
            orderInfo.setRealPrice(cursor.getString(8));
            orderInfo.setDiscountPrice(cursor.getString(9));

            orderInfoList.add(orderInfo);
        }

        return orderInfoList;
    }


}
