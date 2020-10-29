package android_serialport_api.cardwriteread.jiedan.api;


import android_serialport_api.cardwriteread.jiedan.api.request.InitRequest;
import android_serialport_api.cardwriteread.jiedan.api.request.OrderListRequest;
import android_serialport_api.cardwriteread.jiedan.api.response.InitResponse;
import android_serialport_api.cardwriteread.jiedan.api.response.OrderListResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 */

public interface Api {

    @POST("zjypg/shangmishouchiInit")
    Call<InitResponse> shangmishouchiInit(@Body InitRequest entity);


    @POST("zjypg/shangmishouchiOrderList")
    Call<OrderListResponse> shangmishouchiOrderList(@Body OrderListRequest entity);



    @POST("zjypg/shangmishouchiGetNewOrder")
    Call<OrderListResponse> shangmishouchiGetNewOrder(@Body OrderListRequest entity);


}