package android_serialport_api.cardwriteread.jiedan.api.response;

import java.io.Serializable;
import java.util.List;

public class OrderVO implements Serializable {
    private String orderNo;

    private String serial;

    private Integer customerId;
    private Integer itemCount;

    private String customerName;

    private String customerPhone;
    private String note;

    private String mealCode;

    private List<MeshOrderItemVO> orderItemList;

    private Long realfee;
    private Long totalfee;

    private Long paytime;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Long getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(Long totalfee) {
        this.totalfee = totalfee;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
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

    public List<MeshOrderItemVO> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<MeshOrderItemVO> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Long getRealfee() {
        return realfee;
    }

    public void setRealfee(Long realfee) {
        this.realfee = realfee;
    }

    public Long getPaytime() {
        return paytime;
    }

    public void setPaytime(Long paytime) {
        this.paytime = paytime;
    }

    public String getMealCode() {
        return mealCode;
    }

    public void setMealCode(String mealCode) {
        this.mealCode = mealCode;
    }

    @Override
    public String toString() {
        return "OrderVO{" +
                "orderNo='" + orderNo + '\'' +
                ", serial='" + serial + '\'' +
                ", customerId=" + customerId +
                ", itemCount=" + itemCount +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", note='" + note + '\'' +
                ", mealCode='" + mealCode + '\'' +
                ", orderItemList=" + orderItemList +
                ", realfee=" + realfee +
                ", totalfee=" + totalfee +
                ", paytime=" + paytime +
                '}';
    }
}
