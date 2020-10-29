package android_serialport_api.cardwriteread.model.dao;


public class OrderTable {
    public static final String TAB_NAME = "orderinfo";

    public static final String ID = "id";
    public static final String SHOPID = "shopId";
    public static final String CASHIERDESKID = "cashierDeskId";
    public static final String ORDERNO = "orderNo";
    public static final String DATETIME = "dateTime";
    public static final String ORDERTYPE = "orderType";
    public static final String ORDERTYPESTR = "orderTypeStr";
    public static final String PRICE = "price";
    public static final String REALPRICE = "realPrice";
    public static final String DISCOUNTPRICE = "discountPrice";

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + ID + " Integer primary key autoincrement,"
            + SHOPID + " text,"
            + CASHIERDESKID + " text,"
            + ORDERNO + " text,"
            + DATETIME + " text,"
            + ORDERTYPE + " text,"
            + ORDERTYPESTR + " text,"
            + PRICE + " text,"
            + REALPRICE + " text,"
            + DISCOUNTPRICE + " text);";


}
