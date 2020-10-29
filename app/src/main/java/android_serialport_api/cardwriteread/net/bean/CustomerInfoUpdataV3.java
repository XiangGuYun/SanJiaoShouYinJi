package android_serialport_api.cardwriteread.net.bean;

public class CustomerInfoUpdataV3 {
    private int msgType;
    private int type;
    private String customerId;
    private String customerName;
    private String faceId;
    private String faceUrl;
    private String msgContent;

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

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public String toString() {
        return "CustomerInfoUpdataV3{" +
                "msgType=" + msgType +
                ", type=" + type +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", faceId='" + faceId + '\'' +
                ", faceUrl='" + faceUrl + '\'' +
                ", msgContent='" + msgContent + '\'' +
                '}';
    }
}
