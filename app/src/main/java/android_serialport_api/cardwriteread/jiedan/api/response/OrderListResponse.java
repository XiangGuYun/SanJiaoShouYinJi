package android_serialport_api.cardwriteread.jiedan.api.response;

import java.util.List;

/**
 * Created by 18682 on 2018/11/11.
 */

public class OrderListResponse {
    private int code;
    private int pageCount;
    private int pageSize;
    private String message;
    private List<OrderVO> data;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderVO> getData() {
        return data;
    }

    public void setData(List<OrderVO> data) {
        this.data = data;
    }
}
