package android_serialport_api.cardwriteread.net.bean;

import java.util.List;

public class ShopConfigResponse {
    private int code;
    private int pageCount;
    private int pageSize;
    private String message;

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private Integer syncRate;

        private Integer repeatOrderTime;

        private Integer advertPollTime;

        private String shopName;

        private String startTime;

        private String endTime;

        private List<String> advertImgList;

        private List<String> showImgList;

        //
        private Integer cashierDeskStatus;

        private Integer defaultChargePrice;

        private Integer deskId;

        private Integer deskType;

        private Integer online;

        private List<TimeCharge> timeChargeList;

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public Integer getSyncRate() {
            return syncRate;
        }

        public void setSyncRate(Integer syncRate) {
            this.syncRate = syncRate;
        }

        public Integer getRepeatOrderTime() {
            return repeatOrderTime;
        }

        public void setRepeatOrderTime(Integer repeatOrderTime) {
            this.repeatOrderTime = repeatOrderTime;
        }

        public Integer getAdvertPollTime() {
            return advertPollTime;
        }

        public void setAdvertPollTime(Integer advertPollTime) {
            this.advertPollTime = advertPollTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public List<String> getAdvertImgList() {
            return advertImgList;
        }

        public void setAdvertImgList(List<String> advertImgList) {
            this.advertImgList = advertImgList;
        }

        public List<String> getShowImgList() {
            return showImgList;
        }

        public void setShowImgList(List<String> showImgList) {
            this.showImgList = showImgList;
        }

        public Integer getCashierDeskStatus() {
            return cashierDeskStatus;
        }

        public void setCashierDeskStatus(Integer cashierDeskStatus) {
            this.cashierDeskStatus = cashierDeskStatus;
        }

        public Integer getDefaultChargePrice() {
            return defaultChargePrice;
        }

        public void setDefaultChargePrice(Integer defaultChargePrice) {
            this.defaultChargePrice = defaultChargePrice;
        }

        public Integer getDeskId() {
            return deskId;
        }

        public void setDeskId(Integer deskId) {
            this.deskId = deskId;
        }

        public Integer getDeskType() {
            return deskType;
        }

        public void setDeskType(Integer deskType) {
            this.deskType = deskType;
        }

        public List<TimeCharge> getTimeChargeList() {
            return timeChargeList;
        }

        public void setTimeChargeList(List<TimeCharge> timeChargeList) {
            this.timeChargeList = timeChargeList;
        }

        public Integer getOnline() {
            return online;
        }

        public void setOnline(Integer online) {
            this.online = online;
        }
    }

    @Override
    public String toString() {
        return "ShopConfigResponse{" +
                "code=" + code +
                ", pageCount=" + pageCount +
                ", pageSize=" + pageSize +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
