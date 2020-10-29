package android_serialport_api.cardwriteread.net.bean;

public class CustomerInfo {
    private Integer customerId;
    private Integer shopId;
    private String cardNo;
    private String cardName;
    private Long accountBalance;

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "customerId=" + customerId +
                ", shopId=" + shopId +
                ", cardNo='" + cardNo + '\'' +
                ", cardName='" + cardName + '\'' +
                ", accountBalance=" + accountBalance +
                ", cardType=" + cardType +
                ", userStatus=" + userStatus +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", faceUrl='" + faceUrl + '\'' +
                '}';
    }

    private Integer cardType;
    private Integer userStatus;
    private String customerName;
    private String customerPhone;
    private String faceUrl;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Long getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
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

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

}
