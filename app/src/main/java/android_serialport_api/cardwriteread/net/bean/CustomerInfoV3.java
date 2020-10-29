package android_serialport_api.cardwriteread.net.bean;

public class CustomerInfoV3 {
    private String customerId;
    private String customerName;
    private String faceId;
    private String faceUrl;

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

    @Override
    public String toString() {
        return "CustomerInfoV3{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", faceId='" + faceId + '\'' +
                ", faceUrl='" + faceUrl + '\'' +
                '}';
    }
}
