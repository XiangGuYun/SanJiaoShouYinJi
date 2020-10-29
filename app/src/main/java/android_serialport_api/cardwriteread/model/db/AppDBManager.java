package android_serialport_api.cardwriteread.model.db;

import android.content.Context;

import android_serialport_api.cardwriteread.model.dao.OrderDao;


public class AppDBManager {

    private final AppDBHelper dbHelper;
    private final OrderDao orderDao;

    public AppDBManager(Context context, String name) {
        // 创建数据库
        dbHelper = new AppDBHelper(context, name);

        orderDao = new OrderDao(dbHelper);
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    // 关闭数据库的方法
    public void close() {
        dbHelper.close();
    }
}
