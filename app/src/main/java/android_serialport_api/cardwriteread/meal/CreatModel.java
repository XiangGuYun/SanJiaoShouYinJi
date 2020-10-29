package android_serialport_api.cardwriteread.meal;

import java.util.List;

/**
 * Created by Administrator on 2020/6/19.
 */

public class CreatModel {

    /**
     * cashierDeskId : 2354
     * totalFee : 9.0
     * packageId : 0.0
     * totalQuantity : 9.0
     * serialNumber : 4696964214388468040
     * openId : 138
     * productOrderNo : null
     * payType : 3
     * realFee : 9.0
     * items : [{"productId":"7336","quantity":9,"seq":"4767753083763006585","price":1,"productName":"快速收银菜品"}]
     * shopId : 154
     */

    private String cashierDeskId;
    private int totalFee;
    private double packageId;
    private double totalQuantity;
    private String serialNumber;
    private String openId;
    private Object productOrderNo;
    private int payType;
    private int realFee;
    private String shopId;
    private List<ItemsBean> items;

    private String cardNo;
    private String customerId;
    private String customerName;
    private String customerPhone;

    private String accountBalance;

    private String authCode;


    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCashierDeskId() {
        return cashierDeskId;
    }

    public void setCashierDeskId(String cashierDeskId) {
        this.cashierDeskId = cashierDeskId;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public double getPackageId() {
        return packageId;
    }

    public void setPackageId(double packageId) {
        this.packageId = packageId;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Object getProductOrderNo() {
        return productOrderNo;
    }

    public void setProductOrderNo(Object productOrderNo) {
        this.productOrderNo = productOrderNo;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getRealFee() {
        return realFee;
    }

    public void setRealFee(int realFee) {
        this.realFee = realFee;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * productId : 7336
         * quantity : 9.0
         * seq : 4767753083763006585
         * price : 1.0
         * productName : 快速收银菜品
         */

        private String productId;
        private double quantity;
        private String seq;
        private double price;
        private String productName;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }
}
